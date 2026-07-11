package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    Optional<FamilyMember> findByVehicleId(Long vehicleId);

    // Hàm check xe đích dính gói Family
    @Query("SELECT COUNT(fs) > 0 FROM FamilyMember fm " +
            "JOIN FamilySubscription fs ON fm.familyGroup.id = fs.familyGroup.id " +
            "WHERE fm.vehicle.id = :vehicleId " +
            "AND fs.status = 'ACTIVE' " +
            "AND :today BETWEEN fs.startDate AND fs.endDate")
    boolean existsActiveFamilyByVehicleId(@Param("vehicleId") Long vehicleId, @Param("today") LocalDate today);

    // AC03: Kiểm tra khách hàng đã đi làm thành viên của nhóm nào khác chưa
    boolean existsByCustomerId(Long customerId);

    // AC04: Kiểm tra xe này đã bị trói vào nhóm gia đình nào khác chưa (Quy tắc UNIQUE xe)
    boolean existsByVehicleId(Long vehicleId);

}
