package com.swp.autocarwash.queue.service.impl;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.repository.BookingRepository;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.ResourceNotFoundException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.queue.dto.response.QueueTicketResponse;
import com.swp.autocarwash.queue.entity.QueueTicket;
import com.swp.autocarwash.queue.mapper.QueueMapper;
import com.swp.autocarwash.queue.repository.custom.QueueTicketRepository;
import com.swp.autocarwash.queue.service.QueueService;
import com.swp.autocarwash.staff.entity.Staff;
import com.swp.autocarwash.staff.repository.custom.StaffRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    @Transactional
    public List<QueueTicketResponse> getActiveQueue(Long userId) {
        Staff staff = staffRepository.findByUserId(userId);
        Integer stationId = staff.getStation().getId();
        return queueTicketRepository.findActiveQueueByStation(stationId, ACTIVE_STATUSES)
                .stream().map(queueMapper::toResponse).toList();
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

        ticket.setStatus("IN_SERVICE");
        queueTicketRepository.save(ticket);

        if (ticket.getBooking() != null) {
            Booking booking = ticket.getBooking();
            booking.setStatus("WASHING");
            bookingRepository.save(booking);
        }

        return queueMapper.toResponse(ticket);
    }
}
