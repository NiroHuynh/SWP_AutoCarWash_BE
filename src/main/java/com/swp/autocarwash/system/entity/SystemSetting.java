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
@Table(name = "system_setting", schema = "swp_auto_car_wash")
public class SystemSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "setting_key", nullable = false, length = 50)
    private String settingKey;

    @Size(max = 255)
    @NotNull
    @Column(name = "setting_value", nullable = false)
    private String settingValue;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 20)
    @NotNull
    @Column(name = "data_type", nullable = false, length = 20)
    private String dataType;

    @ColumnDefault("1")
    @Column(name = "is_active")
    private Boolean isActive;


}