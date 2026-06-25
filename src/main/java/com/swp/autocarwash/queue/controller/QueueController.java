package com.swp.autocarwash.queue.controller;


import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.booking.dto.response.BookingDetailResponse;
import com.swp.autocarwash.booking.service.BookingService;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.queue.dto.response.QueueTicketResponse;
import com.swp.autocarwash.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Chức năng: Controller cho Queue Dashboard — staff xem danh sách xe đang chờ
 * và thực hiện các hành động  nhưu (hủy do khách bỏ về...).
 *
 * <p>Base URL: {@code /api/queue}</p>
 *
 * @author KimNgan
 * @version 1.0
 */
@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
public class QueueController {
    private final QueueService queueService;
    private final BookingService bookingService;

    /**
     * Chức năng: Lấy danh sách hàng chờ đang active, dùng cho lần load đầu của dashboard
     * <p><b>Ví dụ:</b> {@code GET /api/queue}</p>
     *
     * @return {@code 200 OK} với danh sách {@link QueueTicketResponse}
     */
    @GetMapping
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ApiResponse<List<QueueTicketResponse>>> getActiveQueue() {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách hàng chờ thành công", queueService.getActiveQueue())
        );
    }

    /**
     * Chức năng: Staff hủy booking đang CHECKED_IN trong hàng chờ vì khách bỏ về
     * (BL-QU-05 / FE-27-US-01). actingUserId được suy ra từ JWT principal, không
     *
     * <p><b>Ví dụ:</b> {@code PATCH /api/queue/6/cancel-guest-left}</p>
     *
     * @param bookingId id booking đang CHECKED_IN cần hủy
     * @param principal staff đang đăng nhập, lấy từ JWT đã xác thực
     * @return {@code 200 OK} với {@link BookingDetailResponse} (status = CANCELLED)
     * @author KimNgan
     * @version 1.0
     */
    @PatchMapping("/{bookingId}/cancel-guest-left")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ApiResponse<BookingDetailResponse>> cancelGuestLeft(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal UserCustomerDetails principal) {

        BookingDetailResponse result =
                bookingService.cancelGuestLeftAtCheckIn(bookingId, principal.getUser().getId());

        return ResponseEntity.ok(
                ApiResponse.success("Đã hủy do khách bỏ về", result)
        );
    }
}
