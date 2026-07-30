package com.swp.autocarwash.staff.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.staff.dto.request.ScanVehicleRequest;
import com.swp.autocarwash.staff.dto.response.CheckInResultResponse;
import com.swp.autocarwash.staff.dto.response.ScanVehicleResponse;
import com.swp.autocarwash.staff.service.StaffCheckinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/staff/checkin")
@RequiredArgsConstructor
public class StaffCheckInController {
    private final StaffCheckinService staffCheckinService;

    //endpoint quét biển số xe tại quầy
    // nhận vào licensePlate và trả về thông tin booking nếu có để staff xác nhận check in
    // hoặc báo hasBooking = false để FE mở màn hình tạo order walkin mới
    @PostMapping("/scan")
    public ResponseEntity<ApiResponse<ScanVehicleResponse>> scanVehicle
            (@Valid @RequestBody ScanVehicleRequest request){
        ScanVehicleResponse response = staffCheckinService.scanVehicle(request.getLicensePlate());
        return ResponseEntity.ok(ApiResponse.success("Checked booking in the system with the result above",response));
    }

    //endpoint xác nhận check-in khi Staff bấm nút [CONFIRM CHECK_IN]
    @PostMapping("/confirm/{bookingId}")
    public ResponseEntity<ApiResponse<CheckInResultResponse>> confirmCheckin(@PathVariable Long bookingId){
        CheckInResultResponse result = staffCheckinService.confirmCheckIn(bookingId);
        return ResponseEntity.ok(ApiResponse.success("The information of booking when [CONFIRM CHECK_IN]",
                result));
    }

}
