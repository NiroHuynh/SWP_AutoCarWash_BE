package com.swp.autocarwash.servicepackage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "package_addon_mapping", schema = "swp_auto_car_wash")
public class PackageAddonMapping {
    @EmbeddedId
    private PackageAddonMappingId id;

    @MapsId("servicePackageId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_package_id", nullable = false)
    private ServicePackage servicePackage;

    @MapsId("addonServiceId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "addon_service_id", nullable = false)
    private AddonService addonService;


}