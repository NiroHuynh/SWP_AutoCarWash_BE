package com.swp.autocarwash.refund.adapter;

import com.swp.autocarwash.booking.port.RefundPort;
import com.swp.autocarwash.common.contract.refund.RefundContract;
import com.swp.autocarwash.refund.entity.Refund;
import com.swp.autocarwash.refund.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Chức năng: Adapter phía module refund cung cấp {@link RefundPort} cho module booking,
 * để hiển thị thông tin hoàn tiền trên booking card/detail.
 *
 * @author KimNgan
 * @version 1.0
 */
@Component
@Profile("pro")
@RequiredArgsConstructor
public class RefundBookingAdapter implements RefundPort {

    private final RefundRepository refundRepository;

    @Override
    public Optional<RefundContract> findByBookingId(Long bookingId) {
        return refundRepository.findByBookingId(bookingId)
                .map(this::toContract);
    }

    private RefundContract toContract(Refund refund) {
        return RefundContract.builder()
                .bankName(refund.getRefundBankName())
                .accountNumber(refund.getRefundAccountNumber())
                .amount(refund.getRefundAmount())
                .refundedAt(refund.getRefundedAt())
                .build();
    }
}
