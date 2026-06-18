package com.swp.autocarwash.booking.service.impl;


import com.swp.autocarwash.booking.dto.request.BookingSlotRequest;
import com.swp.autocarwash.booking.dto.response.BookingSlotResponse;
import com.swp.autocarwash.booking.dto.response.SlotWindowResponse;
import com.swp.autocarwash.booking.entity.BookingSlot;
import com.swp.autocarwash.booking.port.AddonServicePort;
import com.swp.autocarwash.booking.port.ServicePackagePort;
import com.swp.autocarwash.booking.repository.BookingSlotRepository;
import com.swp.autocarwash.booking.service.BookingSlotService;
import com.swp.autocarwash.booking.workflow.engine.SlotAvailabilityEngine;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


/**
 *
 * BookingSLotServiceImpl dùng để cung cấp các chức năng liên quan đến quản lý các khung giờ đặt lịch trong hệ thống
 *
 * @author Phong
 * @version 1.0
 */

@Service
@RequiredArgsConstructor
public class BookingSlotServiceImpl implements BookingSlotService {

    private final BookingSlotRepository repository;
    private final ServicePackagePort servicePackagePort;
    private final AddonServicePort addonPort;
    private final SlotAvailabilityEngine engine;

    @Override
    public BookingSlotResponse getAvailableSlots(Integer stationId, BookingSlotRequest request) {

        int serviceDuration = servicePackagePort.getDuration(request.getServicePackageId());
        int addonDuration = addonPort.getTotalDuration(request.getAddonServiceIds());

        int totalMinutes = serviceDuration + addonDuration;
        int requiredSlots = totalMinutes / 15;

        List<BookingSlot> slots =
                repository.findByStationIdAndDateOrderByStartTimeAsc(
                        stationId,
                        LocalDate.parse(request.getAppointmentDate())
                );

        List<SlotWindowResponse> windows =
                engine.buildWindows(slots, requiredSlots);

        if (windows.isEmpty()) {
            throw new BusinessException(ErrorCode.NO_AVAILABLE_SLOTS_FOR_DATE);
        }

        return BookingSlotResponse.builder()
                .slots(windows)
                .build();
    }
}
