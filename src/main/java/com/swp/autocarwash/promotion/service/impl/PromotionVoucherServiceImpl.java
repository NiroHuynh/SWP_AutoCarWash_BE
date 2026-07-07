package com.swp.autocarwash.promotion.service.impl;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.promotion.dto.request.CreatePromotionVoucherRequest;
import com.swp.autocarwash.promotion.dto.response.CreatePromotionVoucherResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionBranchSummaryResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionDashboardListViewResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionTargetResponse;
import com.swp.autocarwash.promotion.entity.*;
import com.swp.autocarwash.promotion.entity.enums.PromotionVoucherStatus;
import com.swp.autocarwash.promotion.repository.*;
import com.swp.autocarwash.promotion.service.PromotionVoucherService;
import com.swp.autocarwash.station.entity.Station;
import com.swp.autocarwash.station.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionVoucherServiceImpl implements PromotionVoucherService {

    private final PromotionRepository promotionRepository;
    private final VoucherRepository voucherRepository;
    private final PromotionTargetMappingRepository promotionTargetMappingRepository;
    private final PromotionTargetRepository promotionTargetRepository;

    private final PromotionStationMappingRepository promotionStationMappingRepository;
    private final StationRepository stationRepository;


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

        //Lưu cấu hình chi nhánh áp dụng
        saveStationMappings(promotion.getId(), request.getStationIds());

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

        //Lưu cấu hình chi nhánh áp dụng
        saveStationMappings(promotion.getId(), request.getStationIds());

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

    /**
     * Hàm xử lý gác cổng, chặn lỗi chi nhánh không tồn tại và lưu bảng trung gian
     */
    private void saveStationMappings(Integer promotionId, List<Integer> stationIds) {
        // Chốt chặn 1: Chế độ 1 và 2 bắt buộc phải truyền danh sách chi nhánh lên
        if (stationIds == null || stationIds.isEmpty()) {
            throw new BusinessException(ErrorCode.STATION_LIST_CANNOT_BE_EMPTY);
        }

        for (Integer stationId : stationIds) {
            // Chốt chặn 2: Chặn lỗi nếu truyền trúng stationId không tồn tại hoặc đã bị xóa/ngừng hoạt động
            Station station = stationRepository.findById(stationId).orElse(null);
            if (station == null || station.getIsDeleted() || !station.getIsOperating()) {
                throw new BusinessException(ErrorCode.STATION_NOT_FOUND_OR_INACTIVE);
            }

            // Đúc khóa chính phức hợp
            PromotionStationMappingId mappingId = PromotionStationMappingId.builder()
                    .promotionId(promotionId)
                    .stationId(stationId)
                    .build();

            // Đóng gói lưu bảng trung gian liên kết Nhiều - Nhiều
            PromotionStationMapping mappingEntity = PromotionStationMapping.builder()
                    .id(mappingId)

                    .build();

            promotionStationMappingRepository.save(mappingEntity);
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

    @Override
    public List<PromotionBranchSummaryResponse> getBranchPromotionSummary(String status) {
        // Ép trạng thái về mặc định nếu FE truyền trống
        String filterStatus = (status == null || status.trim().isEmpty()) ? PromotionVoucherStatus.ACTIVE.name() : status.toUpperCase();

        List<Object[]> rawResults = stationRepository.countPromotionsPerBranch(filterStatus);
        List<PromotionBranchSummaryResponse> summaryList = new ArrayList<>();

        for (Object[] row : rawResults) {
            summaryList.add(PromotionBranchSummaryResponse.builder()
                    .stationId((Integer) row[0])
                    .stationName((String) row[1])
                    .totalActivePromotions(((Number) row[2]).longValue())
                    .build());
        }
        return summaryList;
    }

    @Override
    public List<PromotionDashboardListViewResponse> getPromotionDashboardList(Integer stationId, String status) {

        // 1. Chuẩn hóa trạng thái bộ lọc (Mặc định nếu FE gửi trống là "ALL")
        String filterStatus = "ALL";
        if (status != null && !status.trim().isEmpty()) {
            filterStatus = status.toUpperCase();
        }

        // Danh sách tổng hợp cuối cùng chứa toàn bộ dữ liệu trả về cho FE
        List<PromotionDashboardListViewResponse> allItems = new ArrayList<>();

        //PHẦN 1: BỐC DỮ LIỆU TỪ BẢNG PROMOTION (CHIẾN DỊCH VÀ VOUCHER CHẾ ĐỘ 1, 2)

        List<Promotion> promotions = promotionRepository.findAll();

        for (Promotion p : promotions) {
            // Điểm chặn 1: Lọc theo trạng thái chiến dịch
            if (!filterStatus.equals("ALL") && !p.getStatus().equalsIgnoreCase(filterStatus)) {
                continue; // Bỏ qua nếu không khớp trạng thái Admin chọn
            }

            // Bước A: Tìm danh sách ID các chi nhánh áp dụng của chiến dịch từ bảng trung gian
            List<PromotionStationMapping> stationMappings = promotionStationMappingRepository.findById_PromotionId(p.getId());
            List<Integer> assignedStationIds = new ArrayList<>();
            for (PromotionStationMapping mapping : stationMappings) {
                assignedStationIds.add(mapping.getId().getStationId());
            }

            // Điểm chặn 2: Lọc theo chi nhánh Admin chọn trên giao diện
            if (stationId != null && !assignedStationIds.contains(stationId)) {
                continue; // Bỏ qua nếu chiến dịch này không áp dụng cho chi nhánh đang chọn
            }

            // Bước B: Chuyển đổi danh sách ID chi nhánh sang danh sách "Tên chi nhánh" để hiển thị
            List<String> stationNames = new ArrayList<>();
            List<Station> stations = stationRepository.findAllById(assignedStationIds);
            for (Station s : stations) {
                stationNames.add(s.getStationName());
            }

            // Bước C: Tìm danh sách "Tên nhóm đối tượng khách hàng" (Ví dụ: Hạng Vàng, Khách mới)
            List<String> targetNames = new ArrayList<>();
            List<PromotionTargetMapping> targetMappings = promotionTargetMappingRepository.findById_PromotionId(p.getId());
            for (PromotionTargetMapping mapping : targetMappings) {
                PromotionTarget target = promotionTargetRepository.findById(mapping.getId().getPromotionTargetId()).orElse(null);
                if (target != null) {
                    targetNames.add(target.getTargetName());
                }
            }

            // Bước D: Đóng gói toàn bộ thông tin chiến dịch vào DTO
            PromotionDashboardListViewResponse dto = PromotionDashboardListViewResponse.builder()
                    .id(p.getId())
                    .type("CAMPAIGN")
                    .name(p.getTitle())
                    .appliedStations(stationNames)
                    .targetSegments(targetNames)
                    .startDate(p.getStartDate())
                    .endDate(p.getEndDate())
                    .status(p.getStatus())
                    .build();

            allItems.add(dto);
        }


        //PHẦN 2: BỐC DỮ LIỆU VOUCHER LẺ (CHẾ ĐỘ 3 - PROMOTION_ID LÀ NULL)

        List<Voucher> vouchers = voucherRepository.findAll();

        for (Voucher v : vouchers) {
            // Chỉ lấy những Voucher độc lập (Không thuộc chiến dịch nào)
            if (v.getPromotion() == null) {

                // Điểm chặn 1: Lọc trạng thái của Voucher lẻ
                if (!filterStatus.equals("ALL") && !v.getStatus().equalsIgnoreCase(filterStatus)) {
                    continue;
                }

                // Mặc định Voucher lẻ áp dụng công khai cho "Toàn hệ thống" và "Tất cả khách hàng"
                PromotionDashboardListViewResponse dto = PromotionDashboardListViewResponse.builder()
                        .id(v.getId().intValue()) // Ép kiểu Long của Voucher sang Integer để gom chung bảng
                        .type("STANDALONE_VOUCHER")
                        .name("Voucher lẻ: " + v.getVoucherCode())
                        .appliedStations(List.of("Toàn hệ thống"))
                        .targetSegments(List.of("Tất cả khách hàng"))
                        .startDate(v.getStartDate().toLocalDate()) // Ép LocalDateTime về LocalDate
                        .endDate(v.getExpiryDate().toLocalDate())
                        .status(v.getStatus())
                        .build();

                allItems.add(dto);
            }
        }

        //PHẦN 3: SẮP XẾP THEO NGÀY BẮT ĐẦU MỚI NHẤT (MỚI NHẤT LÊN ĐẦU)

        allItems.sort( new Comparator<PromotionDashboardListViewResponse>(){
            @Override
            public int compare(PromotionDashboardListViewResponse a, PromotionDashboardListViewResponse b) {
                return b.getStartDate().compareTo(a.getStartDate()); // Ngày lớn hơn (mới hơn) đứng trước
            }
        });

        // Trả thẳng nguyên cái List đã gộp và sắp xếp về cho Controller
        return allItems;
    }
}
