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
import java.util.ArrayList;
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
        bookingService.cancelGuestLeftAtCheckIn(bookingId, actingUserId);

        return buildBoard(stationId);
    }

    @Override
    @Transactional
    public QueueBoardResponse startService(Long bookingId, Integer laneId) {
        QueueTicket ticket = queueTicketRepository.findQueueTicketByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.QUEUE_TICKET_NOT_FOUND));

        Booking booking = ticket.getBooking();
        if (!BookingStatus.CHECK_IN.name().equals(booking.getStatus())) {
            throw new BusinessException(ErrorCode.QUEUE_TICKET_NOT_WAITING);
        }

        Integer stationId = ticket.getStation().getId();

        // Nếu FE gửi laneId thì dùng làn đó, không thì lấy làn trống đầu tiên (auto).
        WashLane lane;
        if (laneId != null) {
            lane = washLaneRepository.findById(laneId)
                    .filter(l -> WashLaneStatus.AVAILABLE.name().equals(l.getStatus()) && !Boolean.TRUE.equals(l.getIsDeleted()))
                    .orElseThrow(() -> new BusinessException(ErrorCode.WASH_LANE_NONE_AVAILABLE));
        } else {
            lane = washLaneRepository
                    .findFirstByStation_IdAndStatusAndIsDeletedFalse(stationId, WashLaneStatus.AVAILABLE.name())
                    .orElseThrow(() -> new BusinessException(ErrorCode.WASH_LANE_NONE_AVAILABLE));
        }

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
    public QueueBoardResponse completeService(Long bookingId, Integer laneId) {
        QueueTicket ticket = queueTicketRepository.findQueueTicketByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.QUEUE_TICKET_NOT_FOUND));

        Booking booking = ticket.getBooking();
        if (!BookingStatus.WASHING.name().equals(booking.getStatus())) {
            throw new BusinessException(ErrorCode.BOOKING_NOT_WASHING);
        }

        Integer stationId = ticket.getStation().getId();

        booking.setStatus(BookingStatus.COMPLETED.name());
        bookingRepository.save(booking);

        ticket.setStatus(QueueStatus.COMPLETED.name());
        queueTicketRepository.save(ticket);

        // Giải phóng đúng làn: FE gửi laneId → dùng trực tiếp.
        // Fallback về findFirst nếu FE không gửi (backward compat).
        if (laneId != null) {
            washLaneRepository.findById(laneId).ifPresent(lane -> {
                if (WashLaneStatus.WASHING.name().equals(lane.getStatus())) {
                    lane.setStatus(WashLaneStatus.AVAILABLE.name());
                    washLaneRepository.save(lane);
                }
            });
        } else {
            washLaneRepository
                    .findFirstByStation_IdAndStatusAndIsDeletedFalse(stationId, WashLaneStatus.WASHING.name())
                    .ifPresent(lane -> {
                        lane.setStatus(WashLaneStatus.AVAILABLE.name());
                        washLaneRepository.save(lane);
                    });
        }

        return buildBoard(stationId);
    }

    private QueueBoardResponse buildBoard(Integer stationId) {
        List<QueueTicketResponse> queue = queueTicketRepository
                .findActiveQueueByStation(stationId, ACTIVE_STATUSES)
                .stream().map(queueMapper::toResponse).toList();

        long availableLaneCount = washLaneRepository
                .countByStation_IdAndStatusAndIsDeletedFalse(stationId, WashLaneStatus.AVAILABLE.name());

        // WASHING tickets theo thứ tự ưu tiên (cùng thứ tự startService gán vào làn)
        List<Long> washingBookingIds = new ArrayList<>();
        for (QueueTicketResponse t : queue) {
            if (BookingStatus.WASHING.name().equals(t.getStatus())) {
                washingBookingIds.add(t.getBookingId());
            }
        }

        // Ghép WASHING lanes (sorted by id) với WASHING tickets theo thứ tự → currentBookingId
        List<WashLane> allLanes = washLaneRepository.findByStation_IdAndIsDeletedFalseOrderById(stationId);
        int washIdx = 0;
        List<WashLaneResponse> lanes = new ArrayList<>();
        for (WashLane lane : allLanes) {
            Long currentBookingId = null;
            if (WashLaneStatus.WASHING.name().equals(lane.getStatus()) && washIdx < washingBookingIds.size()) {
                currentBookingId = washingBookingIds.get(washIdx++);
            }
            lanes.add(WashLaneResponse.builder()
                    .id(lane.getId())
                    .laneName(lane.getLaneName())
                    .status(lane.getStatus())
                    .currentBookingId(currentBookingId)
                    .build());
        }

        return QueueBoardResponse.builder()
                .availableLaneCount(availableLaneCount)
                .activeLaneCount(lanes.size())
                .lanes(lanes)
                .queue(queue)
                .build();
    }
}
