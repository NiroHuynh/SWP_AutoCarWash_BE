package com.swp.autocarwash.promotion.adapter;

import com.swp.autocarwash.booking.port.VoucherPort;
import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import com.swp.autocarwash.promotion.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 *
 * Chức năng: VoucherBookingAdapter là adapter triển khai VoucherPort trong môi trường
 * production. Class này đóng vai trò cầu nối giữa booking module và voucher module
 * thông qua VoucherService.
 *
 * Adapter cung cấp các chức năng lấy voucher hợp lệ, validate voucher và tính toán
 * discount voucher cho booking flow.
 *
 * @author Phong
 * @version 1.0
 */
@Component
@Profile("pro")
@RequiredArgsConstructor
public class VoucherBookingAdapter implements VoucherPort {

    private final VoucherService voucherService;

    /**
     *
     * Chức năng: Lấy danh sách voucher hợp lệ của customer từ voucher module.
     *
     * Quy trình:
     * - Nhận customerId cần lấy voucher.
     * - Gọi VoucherService để truy vấn voucher hợp lệ.
     * - Trả về danh sách VoucherContract cho booking module.
     *
     * @param customerId id của customer cần lấy voucher
     *
     * @return danh sách VoucherContract hợp lệ của customer
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public List<VoucherContract> getValidVouchers(Integer customerId) {
        return voucherService.getValidVouchers(customerId);
    }

    /**
     *
     * Chức năng: Kiểm tra voucher có hợp lệ với customer hay không.
     *
     * Quy trình:
     * - Nhận voucherId và customerId cần kiểm tra.
     * - Gọi VoucherService xử lý rule validate voucher.
     * - Trả về kết quả kiểm tra.
     *
     * @param voucherId id của voucher cần validate
     * @param customerId id của customer sử dụng voucher
     *
     * @return true nếu voucher hợp lệ, false nếu không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public boolean validate(Integer voucherId, Integer customerId) {
        return voucherService.validate(voucherId, customerId);
    }

    /**
     *
     * Chức năng: Lấy voucher theo mã code và giá trị đơn hàng.
     *
     * Quy trình:
     * - Nhận voucher code và order value.
     * - Gọi VoucherService tìm kiếm voucher phù hợp.
     * - Convert kết quả thành Optional để xử lý trường hợp không tồn tại.
     * - Trả về voucher contract.
     *
     * @param code mã voucher cần tìm
     * @param orderValue giá trị đơn hàng dùng để kiểm tra điều kiện voucher
     *
     * @return Optional<VoucherContract> chứa voucher nếu tồn tại
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public Optional<VoucherContract> getVoucher(String code,BigDecimal orderValue) {
        return Optional.of(
                voucherService.getVoucher(code, orderValue)
        );
    }

    /**
     *
     * Chức năng: Tính toán phần trăm giảm giá của voucher theo code.
     *
     * Quy trình:
     * - Nhận voucher code và số tiền đơn hàng.
     * - Chuyển đổi amount sang BigDecimal.
     * - Gọi VoucherService thực hiện tính discount.
     * - Trả về VoucherContract chứa thông tin giảm giá.
     *
     * @param code mã voucher cần tính giảm giá
     * @param amount giá trị đơn hàng áp dụng voucher
     *
     * @return VoucherContract chứa thông tin discount voucher
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public VoucherContract getDiscountPercent(String code, Integer amount) {
        return voucherService.calculateDiscount(code, BigDecimal.valueOf(amount));
    }
}
