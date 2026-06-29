package com.swp.autocarwash.queue.service.impl;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.entity.enums.BookingStatus;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.queue.dto.response.QueueBoardResponse;
import com.swp.autocarwash.queue.dto.response.QueueTicketResponse;
import com.swp.autocarwash.queue.dto.response.WashLaneResponse;
import com.swp.autocarwash.queue.entity.QueueTicket;
import com.swp.autocarwash.queue.entity.enums.QueueStatus;
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
    // Filter board bằng booking.status (nguồn sự thật); CHECK_IN tương đương cột Waiting
    private static final List<String> ACTIVE_STATUSES = List.of(
            BookingStatus.CHECK_IN.name(),
            BookingStatus.WASHING.name(),
            BookingStatus.COMPLETED.name()
    );
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
        return buildBoard(staff.getStation().getId());
    }

    @Override
    @Transactional
    public QueueBoardResponse cancelByBookingId(Long bookingId, Long actingUserId) {
        QueueTicket ticket = queueTicketRepository.findQueueTicketByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.QUEUE_TICKET_NOT_FOUND));

        Booking booking = ticket.getBooking();
        if (!BookingStatus.CHECK_IN.name().equals(booking.getStatus())) {
            throw new BusinessException(ErrorCode.QUEUE_TICKET_NOT_WAITING);
        }

        Integer stationId = ticket.getStation().getId();
        // cancelGuestLeftAtCheckIn: booking→CANCELED, slot freeing, event, ticket→CANCELED
        bookingService.cancelGuestLeftAtCheckIn(bookingId, actingUserId);

        return buildBoard(stationId);
    }

    @Override
    @Transactional
    public QueueBoardResponse startService(Long bookingId) {
        QueueTicket ticket = queueTicketRepository.findQueueTicketByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.QUEUE_TICKET_NOT_FOUND));

        Booking booking = ticket.getBooking();
        if (!BookingStatus.CHECK_IN.name().equals(booking.getStatus())) {
            throw new BusinessException(ErrorCode.QUEUE_TICKET_NOT_WAITING);
        }

        Integer stationId = ticket.getStation().getId();

        // AC03 — phải còn làn trống mới cho xe vào làn.
        WashLane lane = washLaneRepository
                .findFirstByStation_IdAndStatusAndIsDeletedFalse(stationId, WashLaneStatus.AVAILABLE.name())
                .orElseThrow(() -> new BusinessException(ErrorCode.WASH_LANE_NONE_AVAILABLE));

        // booking là nguồn sự thật; ticket mirror theo
        booking.setStatus(BookingStatus.WASHING.name());
        bookingRepository.save(booking);

        ticket.setStatus(QueueStatus.WASHING.name());
        queueTicketRepository.save(ticket);

        lane.setStatus(WashLaneStatus.WASHING.name());
        washLaneRepository.save(lane);

        return buildBoard(stationId);
    }

    @Override
    @Transactional
    public QueueBoardResponse completeService(Long bookingId) {
        QueueTicket ticket = queueTicketRepository.findQueueTicketByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.QUEUE_TICKET_NOT_FOUND));

        Booking booking = ticket.getBooking();
        if (!BookingStatus.WASHING.name().equals(booking.getStatus())) {
            throw new BusinessException(ErrorCode.BOOKING_NOT_WASHING);
        }

        Integer stationId = ticket.getStation().getId();

        // booking là nguồn sự thật; ticket mirror theo
        booking.setStatus(BookingStatus.COMPLETED.name());
        booking.setCheckOutAt(LocalDateTime.now());
        bookingRepository.save(booking);

        ticket.setStatus(QueueStatus.COMPLETED.name());
        queueTicketRepository.save(ticket);

        washLaneRepository
                .findFirstByStation_IdAndStatusAndIsDeletedFalse(stationId, WashLaneStatus.WASHING.name())
                .ifPresent(lane -> {
                    lane.setStatus(WashLaneStatus.AVAILABLE.name());
                    washLaneRepository.save(lane);
                });

        return buildBoard(stationId);
    }

    private QueueBoardResponse buildBoard(Integer stationId) {
        List<QueueTicketResponse> queue = queueTicketRepository
                .findActiveQueueByStation(stationId, ACTIVE_STATUSES)
                .stream().map(queueMapper::toResponse).toList();

        long availableLaneCount = washLaneRepository
                .countByStation_IdAndStatusAndIsDeletedFalse(stationId, WashLaneStatus.AVAILABLE.name());

        List<WashLaneResponse> lanes = washLaneRepository
                .findByStation_IdAndIsDeletedFalseOrderById(stationId)
                .stream()
                .map(lane -> WashLaneResponse.builder()
                        .id(lane.getId())
                        .laneName(lane.getLaneName())
                        .status(lane.getStatus())
                        .build())
                .toList();

        return QueueBoardResponse.builder()
                .availableLaneCount(availableLaneCount)
                .activeLaneCount(lanes.size())
                .lanes(lanes)
                .queue(queue)
                .build();
    }
}
