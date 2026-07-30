package com.swp.autocarwash.refund.service;

import com.swp.autocarwash.refund.dto.request.CreateRefundRequest;
import com.swp.autocarwash.refund.dto.response.AccountLookupResponse;
import com.swp.autocarwash.refund.dto.response.DepositAmountResponse;
import com.swp.autocarwash.refund.dto.response.PointsPreviewResponse;
import com.swp.autocarwash.refund.dto.response.RefundDetailResponse;
import com.swp.autocarwash.refund.dto.response.RefundListPageResponse;
import com.swp.autocarwash.refund.dto.response.RefundResponse;
import org.springframework.data.domain.Pageable;

/**
 * Chức năng: Nghiệp vụ hoàn tiền khi Customer hủy booking (US-04) và Admin xử lý hoàn tiền (US-05).
 *
 * @author KimNgan
 * @version 1.0
 */
public interface RefundService {

    /**
     * Tra cứu tên chủ tài khoản qua VietQR Lookup (AC2.1). Thất bại → ném lỗi để FE cho nhập tay (AC2c).
     *
     * @param bin           mã napas BIN ngân hàng
     * @param accountNumber số tài khoản
     * @return thông tin tài khoản kèm tên chủ TK
     */
    AccountLookupResponse lookupAccount(String bin, String accountNumber);

    /**
     * Customer xác nhận hủy booking và tạo yêu cầu hoàn tiền (AC3).
     *
     * <p>Nếu booking chưa từng đặt cọc ({@code isDepositPaid = false}, vd lượt rửa
     * subscription/family miễn phí), booking được hủy thẳng và method trả về {@code null}
     * (không có Refund nào được tạo).</p>
     *
     * @param request  thông tin nhận hoàn tiền
     * @param customerId id customer đang đăng nhập (từ JWT)
     * @return chi tiết refund vừa tạo, hoặc {@code null} nếu booking không có cọc để hoàn
     */
    RefundResponse createRefund(CreateRefundRequest request, Long customerId);

    /**
     * Số tiền cọc cố định toàn hệ thống — để FE hiển thị trước khi khách xác nhận hủy (form AC2),
     * không cần tạo Refund mới biết số tiền sẽ hoàn.
     */
    DepositAmountResponse getDepositAmount();

    /**
     * Xem trước số điểm tích lũy khách sẽ nhận nếu chọn quy đổi tiền cọc thành điểm
     * (refundMethod = LOYALTY_POINTS) — hiển thị TRƯỚC khi khách xác nhận hủy, không tạo
     * Refund, không cộng điểm thật.
     *
     * @param bookingId  mã booking
     * @param customerId id customer đang đăng nhập (từ JWT)
     */
    PointsPreviewResponse previewPointsConversion(Long bookingId, Long customerId);

    /**
     * Danh sách yêu cầu hoàn tiền cho Admin, có filter + search (US-05 AC1, AC6-AC10).
     */
    RefundListPageResponse listRefundsForAdmin(String status, Integer year, Integer month,
                                                Integer stationId, String keyword, Pageable pageable);

    /**
     * Chi tiết 1 yêu cầu hoàn tiền cho Admin, kèm ảnh QR VietQR nếu chưa xử lý (US-05 AC2, AC2b, AC2c).
     */
    RefundDetailResponse getRefundDetail(Long refundId);

    /**
     * Admin xác nhận đã chuyển khoản hoàn tiền xong (US-05 AC3, AC5).
     *
     * @param refundId        id refund
     * @param transactionCode mã giao dịch admin nhập tay
     * @param adminId         id admin đang đăng nhập (user.id)
     */
    RefundDetailResponse confirmRefund(Long refundId, String transactionCode, Long adminId);
}
