package com.swp.autocarwash.promotion.adapter.mock;

import com.swp.autocarwash.booking.port.VoucherPort;
import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 *
 * Chức năng: MockVoucherBookingAdapter là adapter giả lập triển khai VoucherPort
 * trong môi trường development. Class này cung cấp dữ liệu voucher mẫu và mô phỏng
 * các nghiệp vụ validate, lấy thông tin voucher phục vụ booking flow.
 *
 * Class này được sử dụng thay thế voucher module thật trong quá trình phát triển
 * và kiểm thử.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("dev")
public class MockVoucherBookingAdapter implements VoucherPort {

    /**
     *
     * Chức năng: Lấy danh sách voucher hợp lệ giả lập của customer.
     *
     * Quy trình:
     * - Nhận customerId cần lấy voucher.
     * - Tạo danh sách voucher mẫu.
     * - Trả về danh sách VoucherContract giả lập.
     *
     * @param customerId id của customer cần lấy voucher
     *
     * @return danh sách VoucherContract hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<VoucherContract> getValidVouchers(Integer customerId) {
        return List.of(
                new VoucherContract(1, "NEW10", 10, new BigDecimal("50000")),
                new VoucherContract(2, "GOLD20", 20, new BigDecimal("150000")),
                new VoucherContract(3, "WELCOME5", 5, new BigDecimal("30000"))
        );
    }

    /**
     *
     * Chức năng: Kiểm tra voucher có hợp lệ với customer hay không trong môi trường mock.
     *
     * Quy trình:
     * - Nhận voucherId và customerId cần kiểm tra.
     * - Thực hiện rule validate giả lập.
     * - Trả về kết quả kiểm tra.
     *
     * @param voucherId id của voucher cần kiểm tra
     * @param customerId id của customer sử dụng voucher
     *
     * @return true nếu voucher hợp lệ, false nếu không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public boolean validate(Integer voucherId, Integer customerId) {
        return false;
    }

    /**
     *
     * Chức năng: Lấy thông tin voucher theo code và giá trị đơn hàng.
     *
     * Quy trình:
     * - Kiểm tra voucher code có tồn tại hay không.
     * - Nếu code null thì trả về Optional.empty().
     * - Tạo voucher giả lập với discount mặc định.
     * - Trả về voucher contract.
     *
     * @param code mã voucher cần tìm
     * @param orderValue giá trị đơn hàng cần áp dụng voucher
     *
     * @return Optional<VoucherContract> chứa voucher nếu tìm thấy
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public Optional<VoucherContract> getVoucher(String code,BigDecimal orderValue) {

        if (code == null) return Optional.empty();

        VoucherContract v = new VoucherContract();
        v.setVoucherCode(code);
        v.setDiscountPercentage(15);

        return Optional.of(v);
    }

    /**
     *
     * Chức năng: Lấy thông tin phần trăm giảm giá của voucher theo code.
     *
     * Quy trình:
     * - Nhận voucher code và giá trị đơn hàng.
     * - Tạo voucher giả lập.
     * - Gán phần trăm giảm giá mặc định.
     * - Đánh dấu voucher hợp lệ.
     * - Trả về VoucherContract.
     *
     * @param code mã voucher cần lấy thông tin giảm giá
     * @param amount giá trị đơn hàng áp dụng voucher
     *
     * @return VoucherContract chứa thông tin discount voucher
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public VoucherContract getDiscountPercent(String code, Integer amount) {
        VoucherContract v = new VoucherContract();
        v.setVoucherCode(code);
        v.setDiscountPercentage(10);
        v.setValid(true);
        return v;
    }
}
