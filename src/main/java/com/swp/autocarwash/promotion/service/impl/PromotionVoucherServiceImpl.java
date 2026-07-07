package com.swp.autocarwash.promotion.service.impl;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.promotion.dto.request.CreatePromotionVoucherRequest;
import com.swp.autocarwash.promotion.dto.response.CreatePromotionVoucherResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionTargetResponse;
import com.swp.autocarwash.promotion.entity.*;
import com.swp.autocarwash.promotion.entity.enums.PromotionVoucherStatus;
import com.swp.autocarwash.promotion.repository.PromotionRepository;
import com.swp.autocarwash.promotion.repository.PromotionTargetMappingRepository;
import com.swp.autocarwash.promotion.repository.PromotionTargetRepository;
import com.swp.autocarwash.promotion.repository.VoucherRepository;
import com.swp.autocarwash.promotion.service.PromotionVoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionVoucherServiceImpl implements PromotionVoucherService {

    private final PromotionRepository promotionRepository;
    private final VoucherRepository voucherRepository;
    private final PromotionTargetMappingRepository promotionTargetMappingRepository;
    private final PromotionTargetRepository promotionTargetRepository;


    @Override
    public CreatePromotionVoucherResponse createPromotionOrVoucher(CreatePromotionVoucherRequest request) {
        //Valid date cho cả 3 chế độ
        validateDateRange(request);

        //Phân luồng xử lý theo 3 Chế độ
        switch (request.getConfigMode()) {
            case 1:
                return handleDirectPromotion(request);
            case 2:
                return handleCampaignLinkedVoucher(request);
            case 3:
                return handleStandaloneVoucher(request);
            default:
                throw new BusinessException(ErrorCode.INVALID_CONFIG_MODE);
        }

    }

    //TRƯỜNG HỢP 1: Giảm giá trực tiếp hệ thống
    private CreatePromotionVoucherResponse handleDirectPromotion(CreatePromotionVoucherRequest request) {
        // 1. Tạo và lưu Promotion trước để lấy ID
        Promotion promotion = Promotion.builder()
                .title(request.getCampaignName())
                .description("Hệ thống tự động giảm giá trực tiếp theo chiến dịch")
                .startDate(request.getCampaignStartDate())
                .endDate(request.getCampaignEndDate())
                .status(calculateStatus(request.getCampaignStartDate()))
                .build();
        promotion = promotionRepository.save(promotion); // Sinh ra ID thực tế

        // 2. Lưu bảng trung gian mapping đối tượng áp dụng
        saveTargetMappings(promotion.getId(), request.getTargetCustomerTierIds());

        // 3. Tự sinh ngầm mã voucher dạng AUTO_PROMO_[ID]
        BigDecimal maxDiscount = "FIXED".equalsIgnoreCase(request.getDiscountType())
                ? request.getDiscountValue() : request.getMaxDiscountAmount();

        Integer percent = "PERCENTAGE".equalsIgnoreCase(request.getDiscountType())
                ? request.getDiscountValue().intValue() : null;

        Voucher autoVoucher = Voucher.builder()
                .promotion(promotion) // Liên kết khóa ngoại
                .voucherCode("AUTO_PROMO_" + promotion.getId()) // Ghép chuỗi chuẩn AC03
                .minOrderValue(request.getMinOrderValue() != null ? request.getMinOrderValue() : BigDecimal.ZERO)
                .maxDiscountAmount(maxDiscount)
                .usageLimit(999999) // Chế độ trực tiếp hệ thống cho chạy thoải mái
                .usedCount(0)
                .startDate(request.getCampaignStartDate().atStartOfDay()) // Ép LocalDate -> LocalDateTime (00:00:00)
                .expiryDate(request.getCampaignEndDate().atTime(LocalTime.MAX)) // (23:59:59)
                .status(calculateStatus(request.getCampaignStartDate()))
                .reusable(true)
                .discountPercentage(percent)
                .build();

        voucherRepository.save(autoVoucher);
        return CreatePromotionVoucherResponse.builder()
                .promotionId(promotion.getId())
                .voucherId(autoVoucher.getId())
                .voucherCode(autoVoucher.getVoucherCode())
                .build();
    }

    // TRƯỜNG HỢP 2: Tạo mã Voucher theo Chiến dịch
    private CreatePromotionVoucherResponse handleCampaignLinkedVoucher(CreatePromotionVoucherRequest request) {
        // 1. Chốt chặn chống trùng mã code Admin tự gõ
        String upperCode = request.getVoucherCode().trim().toUpperCase();
        if (voucherRepository.existsByVoucherCode(upperCode)) {
            throw new BusinessException(ErrorCode.VOUCHER_CODE_ALREADY_EXISTS);
        }

        // 2. Tạo chiến dịch cha
        Promotion promotion = Promotion.builder()
                .title(request.getCampaignName())
                .startDate(request.getCampaignStartDate())
                .endDate(request.getCampaignEndDate())
                .status(calculateStatus(request.getCampaignStartDate()))
                .build();
        promotion = promotionRepository.save(promotion);

        // 3. Lưu bảng trung gian mapping
        saveTargetMappings(promotion.getId(), request.getTargetCustomerTierIds());

        // 4. Tạo Voucher kế thừa hoàn toàn ngày tháng từ Chiến dịch cha (AC02)
        BigDecimal maxDiscount = "FIXED".equalsIgnoreCase(request.getDiscountType())
                ? request.getDiscountValue() : request.getMaxDiscountAmount();

        Integer percent = "PERCENTAGE".equalsIgnoreCase(request.getDiscountType())
                ? request.getDiscountValue().intValue() : null;

        Voucher voucher = Voucher.builder()
                .promotion(promotion)
                .voucherCode(upperCode)
                .minOrderValue(request.getMinOrderValue() != null ? request.getMinOrderValue() : BigDecimal.ZERO)
                .maxDiscountAmount(maxDiscount)
                .usageLimit(request.getUsageLimit())
                .usedCount(0)
                .startDate(request.getCampaignStartDate().atStartOfDay()) //Kế thừa ngày chiến dịch cha
                .expiryDate(request.getCampaignEndDate().atTime(LocalTime.MAX))
                .status(calculateStatus(request.getCampaignStartDate()))
                .reusable(request.getReusable() != null && request.getReusable())
                .discountPercentage(percent)
                .build();

        voucherRepository.save(voucher);

        return CreatePromotionVoucherResponse.builder()
                .promotionId(promotion.getId())
                .voucherId(voucher.getId())
                .voucherCode(voucher.getVoucherCode())
                .build();
    }

    //TRƯỜNG HỢP 3: Tạo mã Voucher lẻ độc lập
    private CreatePromotionVoucherResponse handleStandaloneVoucher(CreatePromotionVoucherRequest request) {
        // 1. Chốt chặn trùng mã code độc lập
        String upperCode = request.getVoucherCode().trim().toUpperCase();
        if (voucherRepository.existsByVoucherCode(upperCode)) {
            throw new BusinessException(ErrorCode.VOUCHER_CODE_ALREADY_EXISTS);
        }

        // 2. Lưu duy nhất bản ghi voucher, promotion_id bắt buộc là NULL (AC03)
        BigDecimal maxDiscount = "FIXED".equalsIgnoreCase(request.getDiscountType())
                ? request.getDiscountValue() : request.getMaxDiscountAmount();

        Integer percent = "PERCENTAGE".equalsIgnoreCase(request.getDiscountType())
                ? request.getDiscountValue().intValue() : null;

        Voucher standaloneVoucher = Voucher.builder()
                .promotion(null) //Lưu NULL theo đúng thiết kế DB
                .voucherCode(upperCode)
                .minOrderValue(request.getMinOrderValue() != null ? request.getMinOrderValue() : BigDecimal.ZERO)
                .maxDiscountAmount(maxDiscount)
                .usageLimit(request.getUsageLimit())
                .usedCount(0)
                .startDate(request.getVoucherStartDate().atStartOfDay()) // Lấy theo ngày tự nhập tay riêng biệt
                .expiryDate(request.getVoucherEndDate().atTime(LocalTime.MAX))
                .status(calculateStatus(request.getVoucherStartDate()))
                .reusable(request.getReusable() != null && request.getReusable())
                .discountPercentage(percent)
                .build();

        voucherRepository.save(standaloneVoucher);

        return CreatePromotionVoucherResponse.builder()
                .promotionId(null)
                .voucherId(standaloneVoucher.getId())
                .voucherCode(standaloneVoucher.getVoucherCode())
                .build();
    }

    public void validateDateRange(CreatePromotionVoucherRequest request) {
        LocalDate start = request.getConfigMode() == 3 ? request.getVoucherStartDate() : request.getCampaignStartDate();
        LocalDate end = request.getConfigMode() == 3 ? request.getVoucherEndDate() : request.getCampaignEndDate();

        // 1. Kiểm tra null hoặc ngày kết thúc trước ngày bắt đầu
        if (start == null || end == null || end.isBefore(start)) {
            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
        }

        //Chặn tuyệt đối cả ngày bắt đầu lẫn ngày kết thúc ở trong quá khứ
        LocalDate today = LocalDate.now();
        if (start.isBefore(today) || end.isBefore(today)) {
            throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
        }
    }

    private void saveTargetMappings(Integer promotionId, List<Integer> targetIds) {
        if (targetIds != null && !targetIds.isEmpty()) {
            for (Integer targetId : targetIds) {
                // Đúc class Khóa chính phức hợp
                        PromotionTargetMappingId mappingId = PromotionTargetMappingId.builder()
                        .promotionId(promotionId)
                        .promotionTargetId(targetId)
                        .build();

                        // Đóng gói vào Entity trung gian
                PromotionTargetMapping mappingEntity = PromotionTargetMapping.builder()
                        .id(mappingId)
                        .build();

                // Cứ mỗi vòng lặp gọi repo để ném 1 cặp (promotionId, targetId) xuống DB
                promotionTargetMappingRepository.save(mappingEntity);
            }
        }
    }

    private String calculateStatus(LocalDate startDate) {
        // Khớp với thống nhất: Nếu ngày bắt đầu ở tương lai -> UPCOMING, ngược lại -> ACTIVE
        if (startDate.isAfter(LocalDate.now())) {
            return PromotionVoucherStatus.UPCOMING.name();
        }
        return PromotionVoucherStatus.ACTIVE.name();
    }

    @Override
    public List<PromotionTargetResponse> getAllPromotionTargets() {
        // 1. Lấy toàn bộ danh sách từ bảng promotion_target dưới DB
        List<PromotionTarget> targets = promotionTargetRepository.findAll();

        // 2. Chuyển đổi sang DTO
        List<PromotionTargetResponse> result = new ArrayList<>();
        for (PromotionTarget t : targets) {
            result.add(PromotionTargetResponse.builder()
                    .id(t.getId())
                    .targetName(t.getTargetName())
                    .targetCode(t.getTargetCode())
                    .description(t.getDescription())
                    .build());
        }

        return result;
    }
}
