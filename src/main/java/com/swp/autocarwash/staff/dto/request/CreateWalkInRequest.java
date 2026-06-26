package com.swp.autocarwash.staff.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWalkInRequest {
    /** Chỉ có giá trị nếu khách ĐÃ có Account (Trường hợp 1 ở AC01). Null nếu khách vãng lai mới. */
    private Long customerId;
    /**
     * Chỉ có giá trị nếu Staff CHỌN xe có sẵn trong Dropdown.
     * Nếu null, hệ thống hiểu là phải TẠO MỚI 1 Vehicle, dựa vào 3 field dưới đây.
     */
    private Long existingVehicleId;

    /** Chỉ cần điền khi existingVehicleId = null (đang tạo xe mới). */
    private String licensePlate;

    /** Chỉ cần điền khi existingVehicleId = null. */
    private String brandName;

    /** Chỉ cần điền khi existingVehicleId = null. */
    private String color;

    @NotNull(message = "servicePackageId must not be null")
    private Integer servicePackageId;

    /** Danh sách id các dịch vụ đính kèm (addon). Có thể rỗng, không bắt buộc. */
    private List<Integer> addonIds;

    //Trả về danh sách ID của các khung giờ (Slot) được chọn tại quầy
    private List<Integer> chosenSlotIds;

    // Frontend truyền lên để Backend biết đang tạo đơn cho chi nhánh nào
    private Integer stationId;


    /**
     * AC02 Nhánh 2.1: true nếu Staff ĐÃ bấm nút [Xác nhận đóng cọc 20,000đ]
     * (sau khi khách nộp tiền tại quầy). Service sẽ dùng cờ này để xác nhận
     * lại lần cuối trước khi cho phép tạo đơn, đề phòng Frontend bị lỗi/bypass
     * cho phép bấm [XÁC NHẬN CHECK-IN] khi chưa thực sự thu cọc.
     *
     * Nếu xe không bị phạt (không cần thu cọc) thì cờ này không có ý nghĩa,
     * để false hay true đều không ảnh hưởng.
     */
    @Builder.Default
    private Boolean penaltyDepositCollected = false;

}
