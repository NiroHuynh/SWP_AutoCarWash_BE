package com.swp.autocarwash.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyGroupDetailsResponse {

    private Long familyGroupId;
    private String groupName;
    private LocalDateTime createdAt;
    private boolean isOwner; // Để FE phân quyền hiện nút [Thêm thành viên]
    private GroupSubscriptionDto subscription; // Thông tin gói cước chung
    private List<GroupMemberDto> members; // Danh sách gộp cả Chủ nhóm và Thành viên
}
