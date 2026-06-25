package com.swp.autocarwash.staff.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.staff.dto.request.CalculateInvoiceRequest;
import com.swp.autocarwash.staff.dto.request.CreateWalkInRequest;
import com.swp.autocarwash.staff.dto.response.BookingSummaryResponse;
import com.swp.autocarwash.staff.dto.response.CheckPhoneResponse;
import com.swp.autocarwash.staff.dto.response.CreateWalkInResponse;
import com.swp.autocarwash.staff.service.impl.WalkInCheckInService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/staff/create-walkin")
@RequiredArgsConstructor
public class WalkInCheckInController {

    private final WalkInCheckInService walkInCheckInService;

    @GetMapping("/check-phone")
    public ResponseEntity<ApiResponse<CheckPhoneResponse>> checkCustomerPhone(@RequestParam String phone){

        CheckPhoneResponse response = walkInCheckInService.checkPhone(phone);

        String message = response.getExisted()
                ? "Member customer information found successfully."
                : "Phone number does not exist in the system. New walk-in customer.";

        return ResponseEntity.ok(ApiResponse.success(message, response));
    }

    /**
     *XEM TRƯỚC HÓA ĐƠN TẠM TÍNH & TỰ ĐỘNG LOAD SLOT TRỐNG REAL-TIME (AC02, AC03, AC04)
     * * API này gọi liên tục mỗi khi Staff thay đổi gói dịch vụ/addons hoặc gõ biển số xe để preview giá tiền.
     * * @param request Cục request xem trước (chứa customerId, licensePlate, servicePackageId, addonIds, stationId)
     * @return BookingSummaryResponse (Bảng tính tiền chi tiết + Rổ danh sách slot trống thời gian thực)
     */
    @PostMapping("/calculate-invoice")
    public ResponseEntity<ApiResponse<BookingSummaryResponse>> calculateInvoice(@Valid @RequestBody CalculateInvoiceRequest request) {
        BookingSummaryResponse response = walkInCheckInService.calculateInvoice(request);
        return ResponseEntity.ok(ApiResponse.success(response.getSystemNotice(), response));
    }

    /**
     * XÁC NHẬN BẤM NÚT [XÁC NHẬN CHECK-IN] - TẠO ĐƠN THẬT XUỐNG DB
     * * @param request Cục request tạo đơn chính thức (chứa thêm chosenSlotIds và cờ penaltyDepositCollected)
     * @return CreateWalkInResponse (Mã booking, mã vé, số thứ tự Wxxx)
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateWalkInResponse>> createWalkInBooking(@Valid @RequestBody CreateWalkInRequest request) {
        CreateWalkInResponse response = walkInCheckInService.createWalkInOrder(request);
        return ResponseEntity.ok(ApiResponse.success(response.getMessage(), response));
    }

}
