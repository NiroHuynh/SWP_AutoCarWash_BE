package com.swp.autocarwash.servicepackage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class PackageAddonMappingId implements Serializable {
    private static final long serialVersionUID = 8130711055507301682L;
    @NotNull
    @Column(name = "service_package_id", nullable = false)
    private Integer servicePackageId;

    @NotNull
    @Column(name = "addon_service_id", nullable = false)
    private Integer addonServiceId;


}