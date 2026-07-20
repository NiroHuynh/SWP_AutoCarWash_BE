package com.swp.autocarwash.servicepackage.repository;

import com.swp.autocarwash.servicepackage.entity.PackageAddonMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageAddonMappingRepository extends JpaRepository<PackageAddonMapping, PackageAddonMapping.PackageAddonMappingKey> {
    @Query("SELECT m from PackageAddonMapping m")
    List<PackageAddonMapping> findAllMappings();

    /**
     * Lấy toàn bộ mapping của danh sách package.
     */
    @Query("""
                SELECT m
                FROM PackageAddonMapping m
                WHERE m.servicePackage.id IN :packageIds
            """)
    List<PackageAddonMapping> findByServicePackageIdIn(
            @Param("packageIds") List<Integer> packageIds
    );

    /**
     * Xoá toàn bộ mapping cũ để thay tooàn bộ mapping theo addonServiceIds mới
     */
    void deleteByServicePackage_Id(Integer servicePackageId);


    @Query("""
            SELECT pam.addonService.id
            FROM PackageAddonMapping pam
            WHERE pam.servicePackage.id = :servicePackageId
            """)
    List<Integer> findAddonIds(Integer servicePackageId);
}
