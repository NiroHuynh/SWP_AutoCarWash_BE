package com.swp.autocarwash.promotion.adapter;

import com.swp.autocarwash.booking.entity.Booking;
import com.swp.autocarwash.booking.port.VoucherUsagePort;
import com.swp.autocarwash.common.contract.customer.CustomerContract;
import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.promotion.entity.Voucher;
import com.swp.autocarwash.promotion.entity.VoucherUsage;
import com.swp.autocarwash.promotion.entity.enums.VoucherUsageStatus;
import com.swp.autocarwash.promotion.repository.VoucherRepository;
import com.swp.autocarwash.promotion.repository.VoucherUsageRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoucherUsageBookingAdapter implements VoucherUsagePort {

    private final VoucherRepository voucherRepository;
    private final VoucherUsageRepository voucherUsageRepository;
    private final ModelMapper modelMapper;

    @Override
    public void consumeVoucher(Long voucherId, Booking booking) {
        Voucher voucher = voucherRepository.findVoucherById(voucherId);

//        check usage nếu như voucher là loại chỉ dùng được 1 lần
        if(!voucher.getReusable()
            && voucherUsageRepository.existsByVoucherIdAndCustomerId(voucherId,booking.getCustomer().getId())){
            throw new BusinessException(ErrorCode.VOUCHER_USAGE_LIMIT_REACHED);
        }

//        check limit và tăng used count
        int updated = voucherRepository.increaseUsedCount(voucherId);

        if(updated==0){
            throw new BusinessException(ErrorCode.VOUCHER_USAGE_LIMIT_REACHED);
        }

//        tạo voucher usage
        VoucherUsage voucherUsage = VoucherUsage.builder()
                .voucher(voucher)
                .customer(booking.getCustomer())
                .booking(booking)
                .status(VoucherUsageStatus.APPLIED.toString())
                .build();

        voucherUsageRepository.save(voucherUsage);
    }

    @Override
    public void releaseVoucher(Long bookingId) {
        voucherUsageRepository.findByBooking_Id(bookingId).ifPresent(usage -> {
            voucherRepository.decreaseUsedCount(usage.getVoucher().getId());
            voucherUsageRepository.delete(usage);
        });
    }
}
