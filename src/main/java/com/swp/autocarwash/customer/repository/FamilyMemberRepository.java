package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    Optional<FamilyMember> findByVehicleId(Long vehicleId);
}
