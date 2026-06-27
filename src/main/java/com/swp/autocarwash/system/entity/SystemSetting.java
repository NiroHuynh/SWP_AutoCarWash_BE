package com.swp.autocarwash.system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
/**
 * Bảng cấu hình dạng key-value dùng chung cho toàn hệ thống.
 * Dùng để lưu các con số CỐ ĐỊNH (không đổi theo từng booking/khách hàng),
 * ví dụ: mức cọc mặc định, số lần vi phạm tối đa, số ngày bị khóa...
 * Lưu trong DB (thay vì hardcode trong code Java) để Admin có thể chỉnh
 * mà không cần build lại / deploy lại ứng dụng.
 */
@Table(name = "system_setting", schema = "swp_auto_car_wash")
public class SystemSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /** Khóa định danh duy nhất của setting, ví dụ "DEFAULT_DEPOSIT_AMOUNT". */
    @Column(name = "setting_key", nullable = false, unique = true, length = 100)
    private String settingKey;

    /** Giá trị lưu dưới dạng chuỗi - Service sẽ tự parse sang kiểu cần dùng (BigDecimal, Integer...). */
    @Column(name = "setting_value", nullable = false, length = 255)
    private String settingValue;

    /** Mô tả ý nghĩa của setting này, giúp Admin dễ hiểu khi chỉnh trong DB. */
    @Column(name = "description", length = 255)
    private String description;

    @Size(max = 20)
    @NotNull
    @Column(name = "data_type", nullable = false, length = 20)
    private String dataType;

    @ColumnDefault("1")
    @Column(name = "is_active")
    private Boolean isActive;


}