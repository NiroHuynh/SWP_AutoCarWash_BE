package com.swp.autocarwash.promotion.service.impl;

import com.swp.autocarwash.common.contract.promotion.VoucherContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.promotion.entity.Voucher;
import com.swp.autocarwash.promotion.mapper.VoucherMapper;
import com.swp.autocarwash.promotion.repository.VoucherRepository;
import com.swp.autocarwash.promotion.repository.VoucherUsageRepository;
import com.swp.autocarwash.promotion.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 *
 * Chức năng: VoucherServiceImpl triển khai các nghiệp vụ xử lý voucher.
 * Class này chịu trách nhiệm xử lý logic lấy voucher, validate điều kiện sử dụng,
 * kiểm tra giới hạn sử dụng và tính toán discount cho booking.
 *
 * @author Phong
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final VoucherUsageRepository usageRepository;
    private final VoucherMapper voucherMapper;

    /**
     *
     * Chức năng: Lấy danh sách voucher hợp lệ của customer.
     *
     * Quy trình:
     * - Nhận customerId cần lấy voucher.
     * - Truy vấn toàn bộ voucher từ database.
     * - Mapping Voucher entity sang VoucherContract.
     * - Trả về danh sách voucher contract.
     *
     * @param customerId id của customer cần lấy voucher
     *
     * @return danh sách VoucherContract
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    @Transactional(readOnly = true)
    public List<VoucherContract> getValidVouchers(Integer customerId) {

        List<Voucher> vouchers = voucherRepository.findAll();

        return vouchers.stream()
                .map(voucherMapper::toContract)
                .toList();
    }

    /**
     *
     * Chức năng: Lấy thông tin voucher theo code và kiểm tra điều kiện áp dụng.
     *
     * Quy trình:
     * - Nhận voucher code và giá trị đơn hàng.
     * - Tìm voucher theo voucher code trong database.
     * - Kiểm tra voucher đã hết hạn hay chưa.
     * - Kiểm tra giá trị đơn hàng có đạt điều kiện tối thiểu không.
     * - Mapping voucher entity sang contract.
     * - Trả về voucher hợp lệ.
     *
     * @param code mã voucher cần tìm
     * @param orderValue giá trị đơn hàng dùng để kiểm tra điều kiện voucher
     *
     * @return VoucherContract chứa thông tin voucher
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public VoucherContract getVoucher(String code, BigDecimal orderValue) {

        Voucher voucher = voucherRepository.findByVoucherCode(code)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.VOUCHER_NOT_FOUND)
                );

        if (voucher.getExpiryDate().isBefore(Instant.now())) {
            throw new BusinessException(ErrorCode.VOUCHER_EXPIRED);
        }

        if (orderValue.compareTo(voucher.getMinOrderValue()) < 0) {
            throw new BusinessException(ErrorCode.VOUCHER_NOT_APPLICABLE);
        }

        return voucherMapper.toContract(voucher);
    }

    /**
     *
     * Chức năng: Kiểm tra customer đã sử dụng voucher hay chưa.
     *
     * Quy trình:
     * - Nhận voucherId và customerId.
     * - Truy vấn số lần sử dụng voucher của customer.
     * - Nếu customer đã sử dụng voucher trước đó thì throw exception.
     * - Nếu chưa sử dụng thì xác nhận voucher hợp lệ.
     *
     * @param voucherId id của voucher cần kiểm tra
     * @param customerId id của customer sử dụng voucher
     *
     * @return true nếu customer được phép sử dụng voucher
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public boolean validate(Integer voucherId, Integer customerId) {

        long usage = usageRepository
                .countByVoucherIdAndCustomerId(voucherId, customerId);

        if (usage > 0) {
            throw new BusinessException(ErrorCode.VOUCHER_USAGE_LIMIT_REACHED);
        }

        return true;
    }

    /**
     *
     * Chức năng: Tính toán discount của voucher dựa trên code và giá trị đơn hàng.
     *
     * Quy trình:
     * - Nhận voucher code và amount.
     * - Tìm voucher trong database.
     * - Mapping voucher entity sang contract.
     * - Kiểm tra voucher có áp dụng được với amount hay không.
     * - Trả về contract chứa thông tin discount.
     *
     * @param code mã voucher cần tính discount
     * @param amount giá trị đơn hàng áp dụng voucher
     *
     * @return VoucherContract chứa thông tin giảm giá
     *
     * @author Phong
     * @version 1.0
     */
    @Override
    public VoucherContract calculateDiscount(String code, BigDecimal amount) {

        Voucher voucher = voucherRepository.findByVoucherCode(code)
                .orElseThrow(() ->
                        new BusinessException(ErrorCode.VOUCHER_NOT_FOUND)
                );

        VoucherContract contract = voucherMapper.toContract(voucher);

        if (!contract.isValid(amount)) {
            throw new BusinessException(ErrorCode.VOUCHER_NOT_APPLICABLE);
        }

        return contract;
    }
}