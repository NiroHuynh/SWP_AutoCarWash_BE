package com.swp.autocarwash.servicepackage.entity;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
@Entity
@Table(name="package_addon_mapping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageAddonMapping {

    @EmbeddedId
    private PackageAddonMappingKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="service_package_id")
    @MapsId("servicePackageId")
    private ServicePackage servicePackage;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="addon_service_id")
    @MapsId("addonServiceId")
    private AddonService addonService;



    @Embeddable
    @EqualsAndHashCode
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PackageAddonMappingKey implements Serializable {

        @Column(name ="service_package_id")
        private Integer servicePackageId;
        @Column(name = "addon_service_id")
        private Integer addonServiceId;
    }
}
