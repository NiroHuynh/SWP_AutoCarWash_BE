package com.swp.autocarwash.queue.controller;


import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.common.response.ApiResponse;
import com.swp.autocarwash.queue.dto.response.QueueBoardResponse;
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

    /**
     * Chức năng: Lấy danh sách hàng chờ đang active, dùng cho lần load đầu của dashboard
     * <p><b>Ví dụ:</b> {@code GET /api/queue}</p>
     *
     * @return {@code 200 OK} với danh sách {@link QueueTicketResponse}
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
     * AC01 — Hoàn tất rửa: ticket IN_SERVICE→COMPLETED, booking WASHING→COMPLETED, 1 làn OCCUPIED→AVAILABLE.
     * <p>{@code PATCH /api/queue/{ticketId}/complete}</p>
     */
    @PatchMapping("/{ticketId}/complete")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ApiResponse<QueueTicketResponse>> completeService(
            @PathVariable Long ticketId) {
        return ResponseEntity.ok(
                ApiResponse.success("Đã hoàn tất rửa xe", queueService.completeService(ticketId))
        );
    }
    /**
     * Chức năng: Staff hủy queue ticket (do khách bỏ về) theo ticketId.
     *
     * <p><b>Ví dụ:</b> {@code PATCH /api/queue/6/cancel-guest-left}</p>
     *
     * @param ticketId id của queue ticket cần hủy
     * @param principal staff đang đăng nhập, lấy từ JWT đã xác thực
     * @return {@code 200 OK} với {@link QueueTicketResponse} (status = CANCELED)
     * @author KimNgan
     * @version 1.0
     */
    @PatchMapping("/{ticketId}/cancel-guest-left")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ApiResponse<QueueTicketResponse>> cancelGuestLeft(
            @PathVariable Long ticketId,
            @AuthenticationPrincipal UserCustomerDetails principal) {

        return ResponseEntity.ok(
                ApiResponse.success("Đã hủy do khách bỏ về",
                        queueService.cancelByTicketId(ticketId, principal.getUser().getId()))
        );
    }

    /**
     * Chức năng: Thêm xe vào làn rửa — chuyển queue ticket từ WAITING sang IN_SERVICE.
     *
     * <p><b>Ví dụ:</b> {@code PATCH /api/queue/6/start}</p>
     *
     * @param ticketId id của queue ticket cần chuyển sang IN_SERVICE
     * @return {@code 200 OK} với {@link QueueTicketResponse} (status = IN_SERVICE)
     * @author KimNgan
     * @version 1.0
     */
    @PatchMapping("/{ticketId}/start")
    @PreAuthorize("hasAuthority('STAFF')")
    public ResponseEntity<ApiResponse<QueueTicketResponse>> startService(
            @PathVariable Long ticketId) {

        return ResponseEntity.ok(
                ApiResponse.success("Xe đã vào làn rửa", queueService.startService(ticketId))
        );
    }
}
