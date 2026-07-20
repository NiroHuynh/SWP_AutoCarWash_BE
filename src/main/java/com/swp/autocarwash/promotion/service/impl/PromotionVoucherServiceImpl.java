package com.swp.autocarwash.promotion.service.impl;

import com.swp.autocarwash.common.exception.BusinessException;
import com.swp.autocarwash.common.exception.code.ErrorCode;
import com.swp.autocarwash.promotion.dto.request.CreatePromotionVoucherRequest;
import com.swp.autocarwash.promotion.dto.request.UpdatePromotionRequest;
import com.swp.autocarwash.promotion.dto.request.UpdateVoucherRequest;
import com.swp.autocarwash.promotion.dto.response.CreatePromotionVoucherResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionBranchSummaryResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionDashboardListViewResponse;
import com.swp.autocarwash.promotion.dto.response.PromotionTargetResponse;
import com.swp.autocarwash.promotion.entity.*;
import com.swp.autocarwash.promotion.entity.enums.PromotionVoucherStatus;
import com.swp.autocarwash.promotion.entity.enums.VoucherStatus;
import com.swp.autocarwash.promotion.repository.*;
import com.swp.autocarwash.promotion.service.PromotionVoucherService;
import com.swp.autocarwash.station.entity.Station;
import com.swp.autocarwash.station.repository.StationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
                .isDeleted(false)
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
                .isDeleted(false)
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
        // Chốt chặn 1: Validate mảng voucher gửi lên không được rỗng
        if (request.getVouchers() == null || request.getVouchers().isEmpty()) {
            throw new BusinessException(ErrorCode.VOUCHER_LIST_CANNOT_BE_EMPTY);
        }

        // Chốt chặn 2: Quét chống trùng mã trên Form gửi lên và trùng dưới Database
        List<String> codesInRequest = new ArrayList<>();
        for (CreatePromotionVoucherRequest.VoucherSubRequest vReq : request.getVouchers()) {
            if (vReq.getVoucherCode() == null || vReq.getVoucherCode().trim().isEmpty()) {
                throw new BusinessException(ErrorCode.VOUCHER_CODE_CANNOT_BE_EMPTY);
            }

            String upperCode = vReq.getVoucherCode().trim().toUpperCase();

            // A. Check trùng nội bộ trong mảng gửi lên (Duplicate voucher code detected)
            if (codesInRequest.contains(upperCode)) {
                throw new BusinessException(ErrorCode.DUPLICATE_VOUCHER_CODE);
            }
            codesInRequest.add(upperCode);

            // B. Check trùng với các mã voucher đã tồn tại trước đó dưới Database
            if (voucherRepository.existsByVoucherCode(upperCode)) {
                throw new BusinessException(ErrorCode.VOUCHER_CODE_ALREADY_EXISTS);
            }
        }

        // 3. Vượt qua tất cả chốt chặn -> Tiến hành tạo 1 Chiến dịch cha duy nhất
        Promotion promotion = Promotion.builder()
                .title(request.getCampaignName())
                .description(request.getDescription())
                .startDate(request.getCampaignStartDate())
                .endDate(request.getCampaignEndDate())
                .status(calculateStatus(request.getCampaignStartDate()))
                .isDeleted(false)
                .build();
        promotion = promotionRepository.save(promotion); // Lưu lấy ID cha

        // Lưu cấu hình các chi nhánh áp dụng
        saveStationMappings(promotion.getId(), request.getStationIds());

        // Lưu bảng trung gian mapping đối tượng áp dụng (Hạng thành viên)
        saveTargetMappings(promotion.getId(), request.getTargetCustomerTierIds());

        // 4. VÒNG LẶP THẦN THÁNH: Duyệt mảng đúc hàng loạt Voucher con đính vào Cha
        List<String> savedVoucherCodes = new ArrayList<>();

        LocalDateTime voucherStart = request.getCampaignStartDate().atStartOfDay();
        LocalDateTime voucherExpiry = request.getCampaignEndDate().atTime(LocalTime.MAX);
        String initialStatus = calculateStatus(request.getCampaignStartDate());

        for (CreatePromotionVoucherRequest.VoucherSubRequest vReq : request.getVouchers()) {
            String upperCode = vReq.getVoucherCode().trim().toUpperCase();

            // Tính toán giá trị giảm giá tối đa dựa trên loại hình FIXED/PERCENTAGE của từng con
            BigDecimal maxDiscount = "FIXED".equalsIgnoreCase(vReq.getDiscountType())
                    ? vReq.getDiscountValue() : vReq.getMaxDiscountAmount();

            Integer percent = "PERCENTAGE".equalsIgnoreCase(vReq.getDiscountType())
                    ? vReq.getDiscountValue().intValue() : null;

            Voucher voucher = Voucher.builder()
                    .promotion(promotion) // Khóa ngoại đính kết vào chung 1 Promotion cha
                    .voucherCode(upperCode)
                    .minOrderValue(vReq.getMinOrderValue() != null ? vReq.getMinOrderValue() : BigDecimal.ZERO)
                    .maxDiscountAmount(maxDiscount)
                    .usageLimit(vReq.getUsageLimit())
                    .usedCount(0)
                    .startDate(voucherStart) // Kế thừa không gian thời gian của chiến dịch cha
                    .expiryDate(voucherExpiry)
                    .status(initialStatus)
                    .reusable(vReq.getReusable() != null && vReq.getReusable())
                    .discountPercentage(percent)
                    .isDeleted(false)
                    .build();

            voucherRepository.save(voucher);
            savedVoucherCodes.add(voucher.getVoucherCode());
        }

        // 5. Đóng gói Response khớp 100% API Contract SUCCESS mong đợi của Frontend
        return CreatePromotionVoucherResponse.builder()
                .promotionId(promotion.getId())
                .voucherCodes(savedVoucherCodes) // Trả về mảng chuỗi danh sách mã vừa tạo thành công
                .build();
    }
//        // 1. Chốt chặn chống trùng mã code Admin tự gõ
//        String upperCode = request.getVoucherCode().trim().toUpperCase();
//        if (voucherRepository.existsByVoucherCode(upperCode)) {
//            throw new BusinessException(ErrorCode.VOUCHER_CODE_ALREADY_EXISTS);
//        }
//
//        // 2. Tạo chiến dịch cha
//        Promotion promotion = Promotion.builder()
//                .title(request.getCampaignName())
//                .startDate(request.getCampaignStartDate())
//                .endDate(request.getCampaignEndDate())
//                .status(calculateStatus(request.getCampaignStartDate()))
//                .isDeleted(false)
//                .build();
//        promotion = promotionRepository.save(promotion);
//
//        //Lưu cấu hình chi nhánh áp dụng
//        saveStationMappings(promotion.getId(), request.getStationIds());
//
//        // 3. Lưu bảng trung gian mapping
//        saveTargetMappings(promotion.getId(), request.getTargetCustomerTierIds());
//
//        // 4. Tạo Voucher kế thừa hoàn toàn ngày tháng từ Chiến dịch cha (AC02)
//        BigDecimal maxDiscount = "FIXED".equalsIgnoreCase(request.getDiscountType())
//                ? request.getDiscountValue() : request.getMaxDiscountAmount();
//
//        Integer percent = "PERCENTAGE".equalsIgnoreCase(request.getDiscountType())
//                ? request.getDiscountValue().intValue() : null;
//
//        Voucher voucher = Voucher.builder()
//                .promotion(promotion)
//                .voucherCode(upperCode)
//                .minOrderValue(request.getMinOrderValue() != null ? request.getMinOrderValue() : BigDecimal.ZERO)
//                .maxDiscountAmount(maxDiscount)
//                .usageLimit(request.getUsageLimit())
//                .usedCount(0)
//                .startDate(request.getCampaignStartDate().atStartOfDay()) //Kế thừa ngày chiến dịch cha
//                .expiryDate(request.getCampaignEndDate().atTime(LocalTime.MAX))
//                .status(calculateStatus(request.getCampaignStartDate()))
//                .reusable(request.getReusable() != null && request.getReusable())
//                .discountPercentage(percent)
//                .isDeleted(false)
//                .build();
//
//        voucherRepository.save(voucher);
//
//        return CreatePromotionVoucherResponse.builder()
//                .promotionId(promotion.getId())
//                .voucherId(voucher.getId())
//                .voucherCode(voucher.getVoucherCode())
//                .build();


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
                .isDeleted(false)
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

        Promotion promotion = promotionRepository.getReferenceById(promotionId);


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
                    .promotion(promotion)
                    .station(station)
                    .build();

            promotionStationMappingRepository.save(mappingEntity);
        }
    }

    private void saveTargetMappings(Integer promotionId, List<Integer> targetIds) {
        if (targetIds != null && !targetIds.isEmpty()) {
            Promotion promotion = promotionRepository.getReferenceById(promotionId);
            for (Integer targetId : targetIds) {

                PromotionTarget promotionTarget = promotionTargetRepository.getReferenceById(targetId);
                // Đúc class Khóa chính phức hợp
                PromotionTargetMappingId mappingId = PromotionTargetMappingId.builder()
                        .promotionId(promotionId)
                        .promotionTargetId(targetId)
                        .build();

                // Đóng gói vào Entity trung gian
                PromotionTargetMapping mappingEntity = PromotionTargetMapping.builder()
                        .id(mappingId)
                        .promotion(promotion)
                        .promotionTarget(promotionTarget)
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
    public List<PromotionDashboardListViewResponse> getPromotionDashboardList(Integer provinceId, Integer communeId, Integer stationId) {

        // 1. CHỐT CHẶN BẢO VỆ BE: Chỉ cho phép truyền 1 trong 3 tham số lọc (Không được gửi kết hợp lung tung)
        int filterCount = 0;
        if (provinceId != null) filterCount++;
        if (communeId != null) filterCount++;
        if (stationId != null) filterCount++;

        if (filterCount > 1) {
            throw new BusinessException(ErrorCode.CANNOT_COMBINE_FILTERS); // Trả về COMMON_002
        }

        // 2. Xác định danh sách các Station ID hợp lệ cần filter
        List<Integer> validStationIds = null;

        // Nhánh 1: Lọc đích danh theo Chi nhánh
        if (stationId != null) {
            Station station = stationRepository.findById(stationId).orElse(null);
            if (station == null || Boolean.TRUE.equals(station.getIsDeleted())) {
                throw new BusinessException(ErrorCode.STATION_NOT_FOUND); // Trả về STATION_001
            }
            validStationIds = List.of(stationId);
        }
        //Lọc theo Phường/Xã (Commune)
        else if (communeId != null) {
            // Quét xem trong bảng Station có chi nhánh nào thuộc Commune này không
            List<Station> stationsInCommune = stationRepository.findByCommune_IdAndIsDeletedFalse(communeId);

            // Nếu không tìm thấy hoặc Xã này không có trong hệ thống địa lý -> Báo lỗi
            if (stationsInCommune == null || stationsInCommune.isEmpty()) {
                throw new BusinessException(ErrorCode.COMMUNE_NOT_FOUND);
            }
            validStationIds = stationsInCommune.stream().map(Station::getId).toList();
        }
        // Nhánh 3: Lọc theo Tỉnh/Thành phố
        else if (provinceId != null) {
            List<Station> stationsInProvince = stationRepository.findByProvinceIdThroughCommune(provinceId);
            if (stationsInProvince == null || stationsInProvince.isEmpty()) {
                throw new BusinessException(ErrorCode.PROVINCE_NOT_FOUND); // Trả về PROVINCE_001
            }
            validStationIds = stationsInProvince.stream().map(Station::getId).toList();
        }

        // 3. Bốc toàn bộ các Chiến dịch chưa bị xóa mềm (is_deleted = false)
        List<Promotion> promotions = promotionRepository.findByIsDeletedFalse();
        List<PromotionDashboardListViewResponse> resultList = new ArrayList<>();

        for (Promotion p : promotions) {

            // Bước A: Lấy danh sách Chi nhánh áp dụng từ bảng trung gian mapping
            List<PromotionStationMapping> stationMappings = promotionStationMappingRepository.findById_PromotionId(p.getId());
            List<Integer> assignedStationIds = stationMappings.stream()
                    .map(m -> m.getId().getStationId())
                    .toList();

            // BỘ LỌC CHI NHÁNH ĐA TẦNG
            if (validStationIds != null) {
                boolean isMatch = assignedStationIds.stream().anyMatch(validStationIds::contains);
                if (!isMatch) {
                    continue; // Bỏ qua chiến dịch nếu không nằm trong danh sách trúng filter
                }
            }

            // =========================================================================
            // ĐOẠN ĐÓNG GÓI STATIONS, TARGETS VÀ VOUCHERS (Giữ nguyên y hệt lượt trước)
            // =========================================================================
            List<PromotionDashboardListViewResponse.StationInfo> stationInfos = new ArrayList<>();
            List<Station> stations = stationRepository.findAllById(assignedStationIds);
            for (Station s : stations) {
                stationInfos.add(PromotionDashboardListViewResponse.StationInfo.builder()
                        .stationId(s.getId())
                        .stationName(s.getStationName())
                        .build());
            }

            List<PromotionDashboardListViewResponse.TargetInfo> targetInfos = new ArrayList<>();
            List<PromotionTargetMapping> targetMappings = promotionTargetMappingRepository.findById_PromotionId(p.getId());
            for (PromotionTargetMapping mapping : targetMappings) {
                PromotionTarget target = promotionTargetRepository.findById(mapping.getId().getPromotionTargetId()).orElse(null);
                if (target != null) {
                    targetInfos.add(PromotionDashboardListViewResponse.TargetInfo.builder()
                            .targetId(target.getId())
                            .targetName(target.getTargetName())
                            .targetCode(target.getTargetCode())
                            .build());
                }
            }

            List<PromotionDashboardListViewResponse.VoucherInfo> voucherInfos = new ArrayList<>();
            List<Voucher> linkedVouchers = voucherRepository.findByPromotionIdAndIsDeletedFalse(p.getId());
            for (Voucher v : linkedVouchers) {
                voucherInfos.add(PromotionDashboardListViewResponse.VoucherInfo.builder()
                        .id(v.getId())
                        .voucherCode(v.getVoucherCode())
                        .discountPercentage(v.getDiscountPercentage())
                        .maxDiscountAmount(v.getMaxDiscountAmount())
                        .minOrderValue(v.getMinOrderValue())
                        .usageLimit(v.getUsageLimit())
                        .usedCount(v.getUsedCount())
                        .startDate(v.getStartDate())
                        .expiryDate(v.getExpiryDate())
                        .reusable(v.getReusable())
                        .status(v.getStatus())
                        .build());
            }

            PromotionDashboardListViewResponse dto = PromotionDashboardListViewResponse.builder()
                    .id(p.getId())
                    .title(p.getTitle())
                    .description(p.getDescription() != null ? p.getDescription() : "Mô tả chiến dịch...")
                    .startDate(p.getStartDate())
                    .endDate(p.getEndDate())
                    .status(p.getStatus())
                    .createdAt(p.getCreatedAt() != null ? p.getCreatedAt() : LocalDateTime.now())
                    .stations(stationInfos)
                    .targets(targetInfos)
                    .vouchers(voucherInfos)
                    .build();

            resultList.add(dto);
        }

        // 4. SẮP XẾP THEO CREATED_AT MỚI NHẤT LÊN ĐẦU
        resultList.sort(new Comparator<PromotionDashboardListViewResponse>() {
            @Override
            public int compare(PromotionDashboardListViewResponse a, PromotionDashboardListViewResponse b) {
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
        });

        return resultList;
    }

    //CODE SERVICE PHỤC VỤ LOGIC CONFIG - UPDATE PROMOTION/VOUCHER

    public static String determinePromotionStatus(LocalDate start, LocalDate end) {
        LocalDate now = LocalDate.now();
        if (start.isAfter(now)) return PromotionVoucherStatus.UPCOMING.name();
        if (now.isAfter(end)) return PromotionVoucherStatus.EXPIRED.name();
        return PromotionVoucherStatus.ACTIVE.name();
    }

    public static String determineVoucherStatus(LocalDateTime start, LocalDateTime expiry, int usedCount, int limit) {
        if (usedCount >= limit) return VoucherStatus.USED_UP.name();
        LocalDateTime now = LocalDateTime.now();
        if (start.isAfter(now)) return VoucherStatus.UPCOMING.name();
        if (now.isAfter(expiry)) return VoucherStatus.EXPIRED.name();
        return VoucherStatus.ACTIVE.name();
    }

    @Override
    @Transactional
    public PromotionDashboardListViewResponse updatePromotion(Integer promotionId, UpdatePromotionRequest request) {
        // 1. Tìm và validate sự tồn tại của Chiến dịch cha
        Promotion promotion = promotionRepository.findByIdAndIsDeletedFalse(promotionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROMOTION_NOT_FOUND));

        // 2. Validate ràng buộc cơ bản của Chiến dịch
        if (request.getTitle() == null || request.getTitle().trim().isEmpty() || request.getTitle().length() > 150) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST_PARAMETERS);
        }
        if (request.getStartDate() == null || request.getEndDate() == null || request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST_PARAMETERS);
        }
        LocalDate today = LocalDate.now();
        if (request.getStartDate().isBefore(today) || request.getEndDate().isBefore(today)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST_PARAMETERS);
        }
        if (request.getStationIds() == null || request.getStationIds().isEmpty()) {
            throw new BusinessException(ErrorCode.STATION_LIST_CANNOT_BE_EMPTY);
        }

        // 3. Cập nhật thông tin gốc của Chiến dịch
        promotion.setTitle(request.getTitle().trim());
        promotion.setDescription(request.getDescription());
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setStatus(determinePromotionStatus(request.getStartDate(), request.getEndDate()));
        promotion = promotionRepository.save(promotion);

        // 4. Đồng bộ làm sạch và map lại danh sách Chi nhánh (Stations)
        promotionStationMappingRepository.deleteByPromotionId(promotionId);
        List<PromotionDashboardListViewResponse.StationInfo> savedStationInfos = new ArrayList<>();
        for (Integer stationId : request.getStationIds()) {
            Station station = stationRepository.findById(stationId).orElse(null);
            if (station == null || Boolean.TRUE.equals(station.getIsDeleted()) || !Boolean.TRUE.equals(station.getIsOperating())) {
                throw new BusinessException(ErrorCode.STATION_NOT_FOUND_OR_INACTIVE); // Ném lỗi STATION_001
            }

            PromotionStationMappingId mappingId = PromotionStationMappingId.builder()
                    .promotionId(promotionId)
                    .stationId(stationId)
                    .build();
            PromotionStationMapping mapping = PromotionStationMapping.builder()
                    .id(mappingId)
                    .promotion(promotion)
                    .station(station)
                    .build();
            promotionStationMappingRepository.save(mapping);

            savedStationInfos.add(PromotionDashboardListViewResponse.StationInfo.builder()
                    .stationId(station.getId())
                    .stationName(station.getStationName())
                    .build());
        }

        // 5. Đồng bộ làm sạch và map lại danh sách Đối tượng khách hàng (Targets)
        promotionTargetMappingRepository.deleteByPromotionId(promotionId);
        List<PromotionDashboardListViewResponse.TargetInfo> savedTargetInfos = new ArrayList<>();
        if (request.getTargetIds() != null) {
            for (Integer targetId : request.getTargetIds()) {
                PromotionTarget target = promotionTargetRepository.findById(targetId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.PROMOTION_TARGET_NOT_FOUND));

                PromotionTargetMappingId targetMappingId = PromotionTargetMappingId.builder()
                        .promotionId(promotionId)
                        .promotionTargetId(targetId)
                        .build();
                PromotionTargetMapping targetMapping = PromotionTargetMapping.builder()
                        .id(targetMappingId)
                        .promotion(promotion)
                        .promotionTarget(target)
                        .build();
                promotionTargetMappingRepository.save(targetMapping);

                savedTargetInfos.add(PromotionDashboardListViewResponse.TargetInfo.builder()
                        .targetId(target.getId())
                        .targetName(target.getTargetName())
                        .targetCode(target.getTargetCode())
                        .build());
            }
        }

        // 6. XỬ LÝ ĐỒNG BỘ DANH SÁCH VOUCHER CON (VÒNG LẶP NÂNG CAO)
        if (request.getVouchers() == null || request.getVouchers().isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST_PARAMETERS);
        }

        List<Long> incomingVoucherIds = new ArrayList<>();
        List<String> codesInRequest = new ArrayList<>();
        List<PromotionDashboardListViewResponse.VoucherInfo> savedVoucherInfos = new ArrayList<>();

        LocalDateTime voucherStart = request.getStartDate().atStartOfDay();
        LocalDateTime voucherExpiry = request.getEndDate().atTime(LocalTime.MAX);

        for (UpdatePromotionRequest.VoucherUpdateSubRequest vReq : request.getVouchers()) {
            if (vReq.getVoucherCode() == null || vReq.getVoucherCode().trim().isEmpty()) {
                throw new BusinessException(ErrorCode.INVALID_REQUEST_PARAMETERS);
            }
            String cleanCode = vReq.getVoucherCode().trim().toUpperCase();

            // Check trùng nội bộ request body gửi lên
            if (codesInRequest.contains(cleanCode)) {
                throw new BusinessException(ErrorCode.DUPLICATE_VOUCHER_CODE); // Ném lỗi VOUCHER_002
            }
            codesInRequest.add(cleanCode);

            Voucher voucher;
            // PHÂN NHÁNH A: Cập nhật Voucher hiện có
            if (vReq.getId() != null) {
                voucher = voucherRepository.findByIdAndIsDeletedFalse(vReq.getId())
                        .orElseThrow(() -> new BusinessException(ErrorCode.VOUCHER_NOT_FOUND)); // Ném lỗi VOUCHER_001

                // Validate trùng mã với bản ghi khác trong hệ thống
                Optional<Voucher> duplicateCheck = voucherRepository.findByVoucherCodeAndIsDeletedFalse(cleanCode);
                if (duplicateCheck.isPresent() && !duplicateCheck.get().getId().equals(voucher.getId())) {
                    throw new BusinessException(ErrorCode.VOUCHER_CODE_ALREADY_EXISTS); // Ném lỗi VOUCHER_002
                }

                // Validate giới hạn lượt dùng không được nhỏ hơn số lần thực tế đã dùng
                if (vReq.getUsageLimit() < voucher.getUsedCount()) {
                    throw new BusinessException(ErrorCode.USAGE_LIMIT_MUST_BE_GREATER_THAN_USED_COUNT); // Ném lỗi VOUCHER_003
                }

                incomingVoucherIds.add(voucher.getId());
            }
            // PHÂN NHÁNH B: Tạo mới hoàn toàn Voucher con đính kèm vào chiến dịch
            else {
                if (voucherRepository.existsByVoucherCode(cleanCode)) {
                    throw new BusinessException(ErrorCode.VOUCHER_CODE_ALREADY_EXISTS); // Ném lỗi VOUCHER_002
                }
                voucher = new Voucher();
                voucher.setPromotion(promotion);
                voucher.setUsedCount(0);
                voucher.setIsDeleted(false);
            }

            // Ghi nhận / Cấu hình lại các quy tắc tài chính mới
            voucher.setVoucherCode(cleanCode);
            voucher.setDiscountPercentage(vReq.getDiscountPercentage());
            voucher.setMaxDiscountAmount(vReq.getMaxDiscountAmount());
            voucher.setMinOrderValue(vReq.getMinOrderValue() != null ? vReq.getMinOrderValue() : BigDecimal.ZERO);
            voucher.setUsageLimit(vReq.getUsageLimit());
            voucher.setReusable(vReq.getReusable() != null && vReq.getReusable());
            voucher.setStartDate(voucherStart);
            voucher.setExpiryDate(voucherExpiry);

            // Tự động tính toán lại trạng thái Voucher dựa trên dòng thời gian mới
            voucher.setStatus(determineVoucherStatus(voucherStart, voucherExpiry, voucher.getUsedCount(), vReq.getUsageLimit()));
            voucher = voucherRepository.save(voucher);

            if (vReq.getId() == null) {
                incomingVoucherIds.add(voucher.getId()); // Ghi nhận ID của thằng vừa chèn mới tinh
            }

            savedVoucherInfos.add(PromotionDashboardListViewResponse.VoucherInfo.builder()
                    .id(voucher.getId())
                    .voucherCode(voucher.getVoucherCode())
                    .discountPercentage(voucher.getDiscountPercentage())
                    .maxDiscountAmount(voucher.getMaxDiscountAmount())
                    .minOrderValue(voucher.getMinOrderValue())
                    .usageLimit(voucher.getUsageLimit())
                    .usedCount(voucher.getUsedCount())
                    .startDate(voucher.getStartDate())
                    .expiryDate(voucher.getExpiryDate())
                    .reusable(voucher.getReusable() != null && voucher.getReusable())
                    .status(voucher.getStatus())
                    .build());
        }

        // Xóa mềm các voucher cũ của Promotion này nhưng không được truyền lên Form
        List<Voucher> currentVouchersInDb = voucherRepository.findByPromotionIdAndIsDeletedFalse(promotionId);
        for (Voucher dbVoucher : currentVouchersInDb) {
            if (!incomingVoucherIds.contains(dbVoucher.getId())) {
                dbVoucher.setIsDeleted(true);
                dbVoucher.setStatus(VoucherStatus.EXPIRED.name());
                dbVoucher.setExpiryDate(LocalDateTime.now());
                voucherRepository.save(dbVoucher);
            }
        }

        // 7. Đóng gói object trả về khớp 100% cấu hình JSON mẫu SUCCESS của Frontend
        return PromotionDashboardListViewResponse.builder()
                .id(promotion.getId())
                .title(promotion.getTitle())
                .description(promotion.getDescription())
                .startDate(promotion.getStartDate())
                .endDate(promotion.getEndDate())
                .status(promotion.getStatus())
                .createdAt(promotion.getCreatedAt())
                .stations(savedStationInfos)
                .targets(savedTargetInfos)
                .vouchers(savedVoucherInfos)
                .build();
    }

    @Transactional
    public void updateVoucherFinancialRules(Integer voucherId, UpdateVoucherRequest request) {
        // Kiểm tra tồn tại Voucher
        Voucher voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new BusinessException(ErrorCode.VOUCHER_NOT_FOUND));

        //Chốt chặn 1: Bảo vệ hệ thống (Cấm sửa Voucher Chế độ 1)
        if (voucher.getVoucherCode().startsWith("AUTO_PROMO_")) {
            throw new BusinessException(ErrorCode.CANNOT_EDIT_AUTO_PROMOTION_RULES);
        }

        //Chốt chặn 2: Bảo vệ Trạng thái (Hết hạn thì không cho sửa)
        if ("EXPIRED".equalsIgnoreCase(voucher.getStatus())) {
            throw new BusinessException(ErrorCode.CANNOT_EDIT_EXPIRED_VOUCHER);
        }

        //Chốt chặn 3: Kiểm tra Trùng mã Code khi thay đổi tên mã
        String cleanNewCode = request.getVoucherCode().trim().toUpperCase();
        if (!voucher.getVoucherCode().equals(cleanNewCode)) {
            if (voucherRepository.existsByVoucherCode(cleanNewCode)) {
                throw new BusinessException(ErrorCode.VOUCHER_CODE_ALREADY_EXISTS);
            }
            voucher.setVoucherCode(cleanNewCode);
        }

        //Chốt chặn 4: Kiểm soát Giới hạn lượt dùng (Usage Limit Guard - Ép buộc lớn hơn)
        if (request.getUsageLimit() <= voucher.getUsedCount()) {
            throw new BusinessException(ErrorCode.USAGE_LIMIT_MUST_BE_GREATER_THAN_USED_COUNT);
        }

        //Chốt chặn 5: Kiểm tra Tính nhất quán Thời gian
        if (voucher.getPromotion() == null) {
            LocalDateTime now = LocalDateTime.now();

            // 5.1: Ngày hết hạn mới không được ở quá khứ
            if (request.getExpiryDate().isBefore(now)) {
                throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
            }

            // 5.3: Gác cổng ngày bắt đầu (Chỉ check nếu Admin chủ động THAY ĐỔI ngày bắt đầu)
            if (!voucher.getStartDate().isEqual(request.getStartDate())) {
                // Nếu Admin cố tình đổi ngày bắt đầu sang một mốc mới, thì mốc mới đó không được ở quá khứ
                if (request.getStartDate().isBefore(now)) {
                    throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
                }
            }

            if (request.getExpiryDate().isBefore(request.getStartDate())) {
                throw new BusinessException(ErrorCode.INVALID_DATE_RANGE);
            }
        }

        // 6. Ghi nhận dữ liệu tài chính mới sau khi vượt qua tất cả chốt chặn
        voucher.setDiscountPercentage(request.getDiscountPercentage());
        voucher.setMaxDiscountAmount(request.getMaxDiscountAmount());
        voucher.setMinOrderValue(request.getMinOrderValue());
        voucher.setUsageLimit(request.getUsageLimit());
        voucher.setStartDate(request.getStartDate());
        voucher.setExpiryDate(request.getExpiryDate());
        voucher.setReusable(request.getReusable());

        // Tự động tính toán lại trạng thái Voucher mới
        String updatedStatus = determineVoucherStatus(
                request.getStartDate(), request.getExpiryDate(), voucher.getUsedCount(), request.getUsageLimit()
        );
        voucher.setStatus(updatedStatus);

        voucherRepository.save(voucher);
    }

    //CODE SERVICE PHỤC VỤ SOFT-DELETE PROMOTION/VOUCHER
    /**
     * AC01: Xóa mềm Chiến dịch kéo theo toàn bộ Voucher con liên kết (Chế độ 1 & 2)
     */
    @Transactional
    public void softDeletePromotion(Integer promotionId) {
        // 1. Kiểm tra chiến dịch có tồn tại hay không (Cờ is_deleted = true sẽ bị lọc bỏ, nên nếu đã xóa sẽ trả về Empty)
        Promotion promotion = promotionRepository.findByIdAndIsDeletedFalse(promotionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROMOTION_NOT_FOUND));

        // 2. Kích hoạt xóa mềm cho Chiến dịch cha bằng SETTER thuần JPA
        promotion.setIsDeleted(true);
        promotion.setStatus(PromotionVoucherStatus.EXPIRED.name());
        promotionRepository.save(promotion); // Lưu trạng thái mới xuống DB

        //3. ĐỒNG BỘ CON (Crucial BE Rule): Tìm các voucher con CHƯA BỊ XÓA của chiến dịch này
        List<Voucher> linkedVouchers = voucherRepository.findByPromotionIdAndIsDeletedFalse(promotionId);

        if (!linkedVouchers.isEmpty()) {
            for (Voucher voucher : linkedVouchers) {
                // Kích hoạt lệnh xóa mềm cho từng voucher con bằng SETTER
                voucher.setIsDeleted(true);
                voucher.setStatus(VoucherStatus.EXPIRED.name());
                voucher.setExpiryDate(LocalDateTime.now()); // Chốt thời gian hết hạn ngay lập tức
                voucherRepository.save(voucher); // Lưu từng voucher con xuống DB
            }
        }
    }

    /**
     * AC02: Xóa mềm Voucher lẻ độc lập bằng Setter thuần JPA (Chế độ 3)
     */
    @Transactional
    public void softDeleteStandaloneVoucher(Long voucherId) {
        // 1. Kiểm tra tồn tại bản ghi Voucher lẻ và đảm bảo nó chưa bị xóa mềm trước đó
        Voucher voucher = voucherRepository.findByIdAndIsDeletedFalse(voucherId)
                .orElseThrow(() -> new BusinessException(ErrorCode.VOUCHER_NOT_FOUND));

        // 2. Tiến hành xóa mềm và vô hiệu hóa thời gian thực bằng SETTER
        voucher.setIsDeleted(true);
        voucher.setStatus(VoucherStatus.EXPIRED.name()); // Ép trạng thái về hết hạn ngay lập tức
        voucher.setExpiryDate(LocalDateTime.now()); // Chốt mốc thời gian hết hạn là thời điểm bấm nút Xóa

        // 3. Lưu lại trạng thái mới xuống Database
        voucherRepository.save(voucher);
    }


     //Thêm một mã Voucher con mới đính kèm vào Chiến dịch cha ĐÃ TỒN TẠI
    @Transactional
    public CreatePromotionVoucherResponse addVoucherToExistingPromotion(Integer promotionId, CreatePromotionVoucherRequest request) {

        // 1. Kiểm tra xem chiến dịch cha có tồn tại và chưa bị xóa mềm không
        Promotion promotion = promotionRepository.findByIdAndIsDeletedFalse(promotionId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROMOTION_NOT_FOUND));

        String finalVoucherCode;

        // 2. Phân luồng xử lý mã Code theo Chế độ của chiến dịch cha
        if (request.getConfigMode() == 1) {
            // Nếu cha là Chế độ 1 (Giảm sàn tự động): Tự đúc mã ngầm tiếp theo dạng AUTO_PROMO_[ID]_[STT]
            List<Voucher> existingVouchers = voucherRepository.findByPromotionId(promotionId);
            finalVoucherCode = "AUTO_PROMO_" + promotionId + "_" + (existingVouchers.size() + 1);
        } else if (request.getConfigMode() == 2) {
            // Nếu cha là Chế độ 2 (Chiến dịch nhập mã): Lấy mã Admin tự gõ trên giao diện và check trùng
            if (request.getVoucherCode() == null || request.getVoucherCode().trim().isEmpty()) {
                    throw new BusinessException(ErrorCode.VOUCHER_CODE_CANNOT_BE_EMPTY);
            }
            finalVoucherCode = request.getVoucherCode().trim().toUpperCase();
            if (voucherRepository.existsByVoucherCode(finalVoucherCode)) {
                throw new BusinessException(ErrorCode.VOUCHER_CODE_ALREADY_EXISTS);
            }
        } else {
            // Chế độ 3 độc lập thì không có chiến dịch cha nên không được phép dùng API này
            throw new BusinessException(ErrorCode.INVALID_CONFIG_MODE);
        }

        // 3. Tính toán số tiền giảm tối đa (Khống chế theo loại FIXED hoặc PERCENTAGE)
        BigDecimal maxDiscount = "FIXED".equalsIgnoreCase(request.getDiscountType())
                ? request.getDiscountValue() : request.getMaxDiscountAmount();

        Integer percent = "PERCENTAGE".equalsIgnoreCase(request.getDiscountType())
                ? request.getDiscountValue().intValue() : null;

        // 4. Đúc thực thể Voucher con mới: Kế thừa toàn bộ Không gian, Thời gian và Trạng thái của Cha
        Voucher voucher = Voucher.builder()
                .promotion(promotion) // Khóa ngoại đính thẳng vào ID của cha cũ
                .voucherCode(finalVoucherCode)
                .minOrderValue(request.getMinOrderValue() != null ? request.getMinOrderValue() : BigDecimal.ZERO)
                .maxDiscountAmount(maxDiscount)
                // Chế độ 1 cho chạy thoải mái (999999), Chế độ 2 giới hạn theo số lượng Admin nhập
                .usageLimit(request.getConfigMode() == 1 ? 999999 : request.getUsageLimit())
                .usedCount(0)
                .startDate(promotion.getStartDate().atStartOfDay()) // Auto kế thừa ngày bắt đầu của cha
                .expiryDate(promotion.getEndDate().atTime(LocalTime.MAX)) // Auto kế thừa ngày kết thúc của cha
                .status(promotion.getStatus()) // Đồng bộ trạng thái (ACTIVE/INACTIVE) của cha
                .reusable(request.getConfigMode() == 1 || (request.getReusable() != null && request.getReusable()))
                .discountPercentage(percent)
                .isDeleted(false)
                .build();

        Voucher savedVoucher = voucherRepository.save(voucher);

        // Trả kết quả về cho Frontend
        return CreatePromotionVoucherResponse.builder()
                .promotionId(promotion.getId())
                .voucherId(savedVoucher.getId())
                .voucherCode(savedVoucher.getVoucherCode())
                .build();
    }
}
