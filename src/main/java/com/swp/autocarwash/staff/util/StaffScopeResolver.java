package com.swp.autocarwash.staff.util;

import com.swp.autocarwash.auth.security.principal.UserCustomerDetails;
import com.swp.autocarwash.staff.repository.custom.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Ép filter theo đúng station của staff đang đăng nhập — STAFF luôn bị ghi đè
 * stationId (bỏ qua giá trị FE truyền lên), ADMIN giữ nguyên hành vi filter tuỳ chọn.
 */
@Component
@RequiredArgsConstructor
public class StaffScopeResolver {

    private final StaffRepository staffRepository;

    public boolean isStaff(UserCustomerDetails principal) {
        return "ROLE_STAFF".equals(principal.getUser().getRole().getName());
    }

    public Integer staffStationId(UserCustomerDetails principal) {
        return staffRepository.findByUserId(principal.getUser().getId())
                .getStation().getId();
    }

    /** requestedStationId giữ nguyên nếu ADMIN, bị ép về station của staff nếu STAFF. */
    public Integer resolveStationId(UserCustomerDetails principal, Integer requestedStationId) {
        return isStaff(principal) ? staffStationId(principal) : requestedStationId;
    }
}
