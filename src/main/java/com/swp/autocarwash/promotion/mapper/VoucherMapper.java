package com.swp.autocarwash.promotion.mapper;

import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import com.swp.autocarwash.promotion.entity.Voucher;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 *
 * Chức năng: VoucherMapper dùng để chuyển đổi dữ liệu giữa Voucher entity và
 * VoucherContract. Class này hỗ trợ expose dữ liệu voucher giữa các module
 * thông qua contract layer và xử lý kiểm tra trạng thái hợp lệ của voucher.
 *
 * @author Phong
 * @version 1.0
 */
@Component
public class VoucherMapper {

    private final ModelMapper modelMapper;

    public VoucherMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     *
     * Chức năng: Chuyển đổi Voucher entity sang VoucherContract để sử dụng
     * trong giao tiếp giữa voucher module và các module khác.
     *
     * Quy trình:
     * - Nhận Voucher entity từ service layer.
     * - Sử dụng ModelMapper để mapping dữ liệu sang VoucherContract.
     * - Kiểm tra voucher có đáp ứng các điều kiện nghiệp vụ hay không.
     * - Gán trạng thái valid cho contract.
     * - Trả về VoucherContract hoàn chỉnh.
     *
     * @param voucher entity Voucher cần chuyển đổi
     *
     * @return VoucherContract chứa thông tin voucher
     *
     * @author Phong
     * @version 1.0
     */
    public VoucherContract toContract(Voucher voucher) {
        VoucherContract contract = modelMapper.map(voucher, VoucherContract.class);

        contract.setValid(isValid(voucher));

        return contract;
    }

    /**
     *
     * Chức năng: Kiểm tra voucher có hợp lệ theo các rule nghiệp vụ hay không.
     *
     * Quy trình:
     * - Lấy thời gian hiện tại.
     * - Kiểm tra trạng thái voucher có ACTIVE hay không.
     * - Kiểm tra voucher đã bắt đầu sử dụng chưa.
     * - Kiểm tra voucher còn hạn sử dụng hay không.
     * - Kiểm tra số lần sử dụng chưa vượt quá giới hạn.
     * - Trả về kết quả validate.
     *
     * @param voucher voucher entity cần kiểm tra
     *
     * @return true nếu voucher hợp lệ, false nếu voucher không hợp lệ
     *
     * @author Phong
     * @version 1.0
     */
    private boolean isValid(Voucher voucher) {

        Instant now = Instant.now();

        return voucher.getStatus().equals("ACTIVE")
                && voucher.getStartDate().isBefore(now)
                && voucher.getExpiryDate().isAfter(now)
                && voucher.getUsedCount() < voucher.getUsageLimit();
    }
}