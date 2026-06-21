package com.swp.autocarwash.promotion.service;

import com.swp.autocarwash.common.contract.promotion.VoucherContract;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * Chức năng: VoucherService định nghĩa các nghiệp vụ xử lý liên quan đến voucher.
 * Interface này cung cấp các chức năng lấy voucher hợp lệ, kiểm tra voucher,
 * lấy thông tin voucher và tính toán giá trị giảm giá.
 *
 * @author Phong
 * @version 1.0
 */
public interface VoucherService {

    /**
     *
     * Chức năng: Lấy danh sách voucher hợp lệ mà customer có thể sử dụng.
     *
     * Quy trình:
     * - Nhận customerId cần lấy voucher.
     * - Kiểm tra các điều kiện voucher như trạng thái, thời gian hiệu lực
     *   và giới hạn sử dụng.
     * - Trả về danh sách voucher hợp lệ.
     *
     * @param customerId id của customer cần lấy voucher
     *
     * @return danh sách VoucherContract hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    List<VoucherContract> getValidVouchers(Integer customerId);

    /**
     *
     * Chức năng: Lấy thông tin voucher theo mã voucher và giá trị đơn hàng.
     *
     * Quy trình:
     * - Nhận voucher code và order value.
     * - Tìm kiếm voucher trong hệ thống.
     * - Kiểm tra điều kiện áp dụng voucher.
     * - Trả về thông tin voucher nếu hợp lệ.
     *
     * @param code mã voucher cần tìm
     * @param orderValue giá trị đơn hàng dùng để kiểm tra điều kiện voucher
     *
     * @return VoucherContract chứa thông tin voucher
     *
     * @author Phong
     * @version 1.0
     */
    VoucherContract getVoucher(String code, BigDecimal orderValue);

    /**
     *
     * Chức năng: Kiểm tra voucher có được phép sử dụng bởi customer hay không.
     *
     * Quy trình:
     * - Nhận voucherId và customerId cần kiểm tra.
     * - Kiểm tra voucher tồn tại.
     * - Kiểm tra trạng thái và giới hạn sử dụng voucher.
     * - Kiểm tra customer đã vượt quá số lần sử dụng chưa.
     * - Trả về kết quả validate.
     *
     * @param voucherId id của voucher cần kiểm tra
     * @param customerId id của customer sử dụng voucher
     *
     * @return true nếu voucher hợp lệ, false nếu không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    boolean validate(Integer voucherId, Integer customerId);

    /**
     *
     * Chức năng: Tính toán thông tin giảm giá dựa trên voucher code.
     *
     * Quy trình:
     * - Nhận voucher code và giá trị đơn hàng.
     * - Tìm voucher tương ứng.
     * - Kiểm tra voucher có hợp lệ với đơn hàng.
     * - Tính toán phần trăm giảm giá.
     * - Trả về VoucherContract chứa thông tin discount.
     *
     * @param code mã voucher cần tính giảm giá
     * @param amount giá trị đơn hàng áp dụng voucher
     *
     * @return VoucherContract chứa thông tin giảm giá
     *
     * @author Phong
     * @version 1.0
     */
    VoucherContract calculateDiscount(String code, BigDecimal amount);
}
