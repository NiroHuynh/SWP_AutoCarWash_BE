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
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;


/**
 *
 * Chức năng: BookingSlotServiceImpl triển khai nghiệp vụ xử lý booking slot,
 * bao gồm việc tìm kiếm slot khả dụng dựa trên thời lượng service, addon
 * và ngày đặt lịch của khách hàng.
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

    /**
     *
     * Chức năng: Lấy danh sách các khung giờ có thể đặt tại một station
     * dựa trên service package, addon service và ngày được chọn.
     *
     * Quy trình:
     * - Lấy duration của service package.
     * - Lấy tổng duration của các addon service.
     * - Tính tổng thời gian cần thiết cho booking.
     * - Chuyển đổi thời gian thành số lượng slot cần sử dụng.
     * - Lấy danh sách slot của station theo ngày đặt lịch.
     * - Gửi danh sách slot cho SlotAvailabilityEngine để xây dựng các khoảng thời gian hợp lệ.
     * - Kiểm tra kết quả có slot khả dụng hay không.
     * - Trả về danh sách slot có thể đặt.
     *
     * @param stationId id của station cần lấy slot booking
     * @param request thông tin yêu cầu bao gồm ngày đặt lịch,
     *                service package và addon service
     *
     * @return BookingSlotResponse chứa danh sách các khoảng thời gian có thể đặt
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public BookingSlotResponse getAvailableSlots(Integer stationId, BookingSlotRequest request) {

        int serviceDuration = servicePackagePort.getDuration(request.getServicePackageId());
        int addonDuration = 0;

        if(request.getAddonServiceIds()!=null && !request.getAddonServiceIds().isEmpty()){
            addonDuration = addonPort.getTotalDuration(request.getAddonServiceIds());
        }

        int totalMinutes = serviceDuration + addonDuration;
        int requiredSlots = totalMinutes / 15;


        LocalDate appointmentDate =
                LocalDate.parse(request.getAppointmentDate());

        LocalDate today =
                LocalDate.now();

        LocalTime now =
                LocalTime.now(
                        ZoneId.of("Asia/Ho_Chi_Minh")
                );

        List<BookingSlot> slots =
                repository.findAvailableSlotsByStationAndDate(
                        stationId,
                        appointmentDate,
                        today,
                        now
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
