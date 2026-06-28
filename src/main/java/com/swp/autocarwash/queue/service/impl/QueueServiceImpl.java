package com.swp.autocarwash.queue.service.impl;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.queue.dto.response.QueueBoardResponse;
import com.swp.autocarwash.queue.dto.response.QueueTicketResponse;
import com.swp.autocarwash.queue.entity.QueueTicket;
import com.swp.autocarwash.queue.mapper.QueueMapper;
import com.swp.autocarwash.queue.repository.custom.QueueTicketRepository;
import com.swp.autocarwash.queue.service.QueueService;
import com.swp.autocarwash.staff.entity.Staff;
import com.swp.autocarwash.staff.repository.custom.StaffRepository;
import com.swp.autocarwash.wash.entity.WashLane;
import com.swp.autocarwash.wash.entity.enums.WashLaneStatus;
import com.swp.autocarwash.station.repository.StationWashLaneRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {
    private static final List<String> ACTIVE_STATUSES = List.of("WAITING", "IN_SERVICE", "COMPLETED");
    private final QueueTicketRepository queueTicketRepository;
    private final QueueMapper queueMapper;
    private final StaffRepository staffRepository;
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final StationWashLaneRepository washLaneRepository;

    @Override
    @Transactional
    public QueueBoardResponse getActiveQueue(Long userId) {
        Staff staff = staffRepository.findByUserId(userId);
        Integer stationId = staff.getStation().getId();
        List<QueueTicketResponse> queue = queueTicketRepository
                .findActiveQueueByStation(stationId, ACTIVE_STATUSES) // vẫn WAITING, IN_SERVICE, COMPLETED
                .stream().map(queueMapper::toResponse).toList();

        long availableLaneCount = washLaneRepository
                .countByStation_IdAndStatusAndIsDeletedFalse(stationId, WashLaneStatus.AVAILABLE.name());

        return QueueBoardResponse.builder()
                .availableLaneCount(availableLaneCount)
                .queue(queue)
                .build();
    }

    @Override
    @Transactional
    public QueueTicketResponse cancelByTicketId(Long ticketId, Long actingUserId) {
        QueueTicket ticket = queueTicketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.QUEUE_TICKET_NOT_FOUND));

        if (!"WAITING".equals(ticket.getStatus())) {
            throw new BusinessException(ErrorCode.QUEUE_TICKET_NOT_WAITING);
        }

        if (ticket.getBooking() != null) {
            // cancelGuestLeftAtCheckIn handles: booking CANCELLED, slot freeing, event publishing,
            // AND sets this queue ticket to CANCELLED via findQueueTicketByBookingId internally
            bookingService.cancelGuestLeftAtCheckIn(ticket.getBooking().getId(), actingUserId);
        } else {
            // Walk-in ticket without booking: just cancel the ticket
            ticket.setStatus("CANCELLED");
            queueTicketRepository.save(ticket);
        }

        ticket.setStatus("CANCELLED");
        return queueMapper.toResponse(ticket);
    }

    @Override
    @Transactional
    public QueueTicketResponse startService(Long ticketId) {
        QueueTicket ticket = queueTicketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.QUEUE_TICKET_NOT_FOUND));

        if (!"WAITING".equals(ticket.getStatus())) {
            throw new BusinessException(ErrorCode.QUEUE_TICKET_NOT_WAITING);
        }

        Integer stationId = ticket.getStation().getId();

        // AC03 — phải còn làn trống mới cho xe vào làn.
        WashLane lane = washLaneRepository
                .findFirstByStation_IdAndStatusAndIsDeletedFalse(stationId, WashLaneStatus.AVAILABLE.name())
                .orElseThrow(() -> new BusinessException(ErrorCode.WASH_LANE_NONE_AVAILABLE));

        ticket.setStatus("IN_SERVICE");
        queueTicketRepository.save(ticket);

        if (ticket.getBooking() != null) {
            Booking booking = ticket.getBooking();
            booking.setStatus("WASHING");
            bookingRepository.save(booking);
        }

        lane.setStatus(WashLaneStatus.WASHING.name());
        washLaneRepository.save(lane);

        return queueMapper.toResponse(ticket);
    }

    @Override
    @Transactional
    public QueueTicketResponse completeService(Long ticketId) {
        QueueTicket ticket = queueTicketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.QUEUE_TICKET_NOT_FOUND));

        if (!"IN_SERVICE".equals(ticket.getStatus())) {
            throw new BusinessException(ErrorCode.QUEUE_TICKET_NOT_IN_SERVICE);
        }

        Integer stationId = ticket.getStation().getId();

        // Ticket nhảy sang cột COMPLETED trên board (không bị loại — ACTIVE_STATUSES vẫn chứa COMPLETED).
        ticket.setStatus("COMPLETED");
        queueTicketRepository.save(ticket);

        if (ticket.getBooking() != null) {
            Booking booking = ticket.getBooking();
            if (!"WASHING".equals(booking.getStatus())) {
                throw new BusinessException(ErrorCode.BOOKING_NOT_WASHING);
            }
            booking.setStatus("COMPLETED");
            booking.setCheckOutAt(LocalDateTime.now());
            bookingRepository.save(booking);
        }

        // Giải phóng 1 làn OCCUPIED -> AVAILABLE; GET /api/queue kế tiếp sẽ thấy availableLaneCount > 0
        // và đẩy xe vị trí #1 sang trạng thái chờ xác nhận (AC01 -> AC02).
        washLaneRepository
                .findFirstByStation_IdAndStatusAndIsDeletedFalse(stationId, WashLaneStatus.WASHING.name())
                .ifPresent(lane -> {
                    lane.setStatus(WashLaneStatus.AVAILABLE.name());
                    washLaneRepository.save(lane);
                });

        return queueMapper.toResponse(ticket);
    }
}
