package com.swp.autocarwash.booking.port;

import com.swp.autocarwash.common.contract.promotion.VoucherContract;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 *
 * Chức năng: VoucherPort định nghĩa các phương thức giao tiếp giữa booking module
 * và module quản lý voucher. Interface này cung cấp dữ liệu voucher, kiểm tra điều kiện
 * áp dụng và lấy thông tin giảm giá phục vụ quá trình tạo booking.
 *
 * @author Phong
 * @version 1.0
 */
public interface VoucherPort {

    /**
     *
     * Chức năng: Lấy danh sách voucher hợp lệ mà customer có thể sử dụng.
     *
     * Quy trình:
     * - Nhận customerId cần kiểm tra voucher.
     * - Gửi yêu cầu lấy voucher đến module voucher.
     * - Lọc các voucher còn hiệu lực và phù hợp với customer.
     * - Trả về danh sách voucher hợp lệ.
     *
     * @param customerId id của customer cần lấy voucher
     *
     * @return danh sách VoucherContract chứa các voucher hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    List<VoucherContract> getValidVouchers(Long customerId);

    /**
     *
     * Chức năng: Kiểm tra voucher có được phép sử dụng bởi customer hay không.
     *
     * Quy trình:
     * - Nhận voucherId và customerId cần kiểm tra.
     * - Kiểm tra voucher tồn tại, còn hiệu lực và thuộc điều kiện sử dụng.
     * - Trả về kết quả validate.
     *
     * @param voucherId id của voucher cần kiểm tra
     * @param customerId id của customer muốn sử dụng voucher
     *
     * @return true nếu voucher hợp lệ, false nếu voucher không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    boolean validate(Long voucherId, Long customerId);

    /**
     *
     * Chức năng: Kiểm tra voucher theo mã và lấy thông tin phần trăm giảm giá.
     *
     * Quy trình:
     * - Nhận voucher code và giá trị đơn hàng.
     * - Kiểm tra voucher có tồn tại và đủ điều kiện áp dụng.
     * - Lấy phần trăm giảm giá của voucher.
     * - Trả về VoucherContract chứa thông tin giảm giá.
     *
     * @param code mã voucher cần kiểm tra
     * @param amount giá trị đơn hàng cần áp dụng voucher
     *
     * @return VoucherContract chứa thông tin voucher và phần trăm giảm giá
     *
     * @author Phong
     * @version 1.0
     */
    VoucherContract getDiscountPercent(String code, Integer amount);


    /**
     *
     * Chức năng: Lấy thông tin voucher theo mã voucher và giá trị đơn hàng.
     *
     * Quy trình:
     * - Nhận voucher code và giá trị đơn hàng.
     * - Kiểm tra voucher tồn tại và điều kiện áp dụng.
     * - Trả về voucher nếu hợp lệ.
     * - Trả về Optional rỗng nếu không tìm thấy hoặc không hợp lệ.
     *
     * @param code mã voucher cần tìm
     * @param orderValue giá trị đơn hàng dùng để kiểm tra điều kiện voucher
     *
     * @return Optional chứa VoucherContract nếu voucher hợp lệ, ngược lại trả về Optional.empty()
     *
     * @author Phong
     * @version 1.0
     */
    Optional<VoucherContract> getVoucher(String code, BigDecimal orderValue);
}