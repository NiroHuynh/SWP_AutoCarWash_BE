package com.swp.autocarwash.servicepackage.repository;

import com.swp.autocarwash.servicepackage.entity.PackageAddonMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageAddonMappingRepository extends JpaRepository<PackageAddonMapping, PackageAddonMapping.PackageAddonMappingKey> {
    @Query("SELECT m from PackageAddonMapping m")
    List<PackageAddonMapping> findAllMappings();
}
