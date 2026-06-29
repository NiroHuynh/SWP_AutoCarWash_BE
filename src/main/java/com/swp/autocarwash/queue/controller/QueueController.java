package com.swp.autocarwash.queue.controller;


import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.queue.dto.response.QueueBoardResponse;
import com.swp.autocarwash.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


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

    /**
     * Chức năng: Lấy danh sách hàng chờ đang active, dùng cho lần load đầu của dashboard
     * <p><b>Ví dụ:</b> {@code GET /api/queue}</p>
     *
     * @return {@code 200 OK} với danh sách {@link QueueBoardResponse}
     */
    @GetMapping
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ApiResponse<QueueBoardResponse>> getActiveQueue(
            @AuthenticationPrincipal UserCustomerDetails principal) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách hàng chờ thành công",
                        queueService.getActiveQueue(principal.getUser().getId()))
        );
    }


    /**
     * AC01 — Hoàn tất rửa: booking WASHING→COMPLETED, ticket→COMPLETED, 1 làn WASHING→AVAILABLE.
     * <p>{@code PATCH /api/queue/{bookingId}/complete}</p>
     *
     * @return {@code 200 OK} với {@link QueueBoardResponse} — board mới sau khi hoàn tất
     */
    @PatchMapping("/{bookingId}/complete")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ApiResponse<QueueBoardResponse>> completeService(
            @PathVariable Long bookingId) {
        return ResponseEntity.ok(
                ApiResponse.success("Đã hoàn tất rửa xe", queueService.completeService(bookingId))
        );
    }

    /**
     * Chức năng: Staff hủy queue ticket (do khách bỏ về) theo bookingId.
     * <p><b>Ví dụ:</b> {@code PATCH /api/queue/6/cancel-guest-left}</p>
     *
     * @return {@code 200 OK} với {@link QueueBoardResponse} — board mới sau khi hủy
     */
    @PatchMapping("/{bookingId}/cancel-guest-left")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ApiResponse<QueueBoardResponse>> cancelGuestLeft(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal UserCustomerDetails principal) {

        return ResponseEntity.ok(
                ApiResponse.success("Đã hủy do khách bỏ về",
                        queueService.cancelByBookingId(bookingId, principal.getUser().getId()))
        );
    }

    /**
     * Chức năng: Thêm xe vào làn rửa — booking CHECK_IN→WASHING, ticket→WASHING.
     * <p><b>Ví dụ:</b> {@code PATCH /api/queue/6/start}</p>
     *
     * @return {@code 200 OK} với {@link QueueBoardResponse} — board mới sau khi xe vào làn
     */
    @PatchMapping("/{bookingId}/start")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ApiResponse<QueueBoardResponse>> startService(
            @PathVariable Long bookingId) {

        return ResponseEntity.ok(
                ApiResponse.success("Xe đã vào làn rửa", queueService.startService(bookingId))
        );
    }
}
