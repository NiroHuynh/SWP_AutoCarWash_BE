package com.swp.autocarwash.staff.controller;

import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.staff.dto.request.CalculateInvoiceRequest;
import com.swp.autocarwash.staff.dto.response.BookingSummaryResponse;
import com.swp.autocarwash.staff.dto.response.CheckPhoneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/staff/create-walkin")
@RequiredArgsConstructor
public class WalkInCheckInController {

    @GetMapping("/check-phone")
    public ResponseEntity<ApiResponse<CheckPhoneResponse>> checkCustomerPhone(@RequestParam String phone){
        // Thực tế code chạy: CheckPhoneResponse response = walkInCheckInService.checkPhone(phone);

        // Đoạn code giả lập dữ liệu (Mock Data) để test API trước khi viết Service:
        CheckPhoneResponse mockResponse = CheckPhoneResponse.builder()
                .isExisted(true)
                .customerName("Nguyễn Văn A")
                .tierName("Hạng Vàng")
                .build();
        return ResponseEntity.ok(ApiResponse.success("Check phone successfully", mockResponse));
    }

    //* AC02 + AC03 + AC04: API tính toán hóa đơn tạm tính (Side-panel) theo thời gian thực
    @PostMapping("/calculate-invoice")
    public ResponseEntity<ApiResponse<BookingSummaryResponse>> calculateInvoice(@RequestBody CalculateInvoiceRequest request) {
        // Thực tế code chạy: BookingSummaryResponse response = walkInCheckInService.calculateInvoice(request);

        // Giả lập dữ liệu hiển thị khung hóa đơn tạm tính bên phải:
        BookingSummaryResponse mockResponse = BookingSummaryResponse.builder()
                .rawAmount(new java.math.BigDecimal("150000"))
                .packageDiscount(java.math.BigDecimal.ZERO)
                .penaltyDeposit(new java.math.BigDecimal("20000")) // Phát hiện xe bị phạt trễ hẹn (AC02)
                .transferredCredit(java.math.BigDecimal.ZERO)
                .remainingBalance(new java.math.BigDecimal("170000"))
                .systemNotice("Xe bị hạn chế thiết bị do vi phạm. Yêu cầu thu thêm 20,000 VND tiền cọc tại quầy!")
                .isActionBlock(true) // Khóa nút Check-in chính, ép Staff bấm nút thu cọc trước
                .build();

        return ResponseEntity.ok(ApiResponse.success("Tính toán hóa đơn tạm tính thành công", mockResponse));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createWalkInBooking(@RequestBody CalculateInvoiceRequest finalRequest) {
        // Thực tế code chạy: walkInCheckInService.createWalkInOrder(finalRequest);
        return ResponseEntity.ok(ApiResponse.success("Tạo đơn hàng vãng lai thành công. Xe đã vào hàng đợi!", null));
    }

}
