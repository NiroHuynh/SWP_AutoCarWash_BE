package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.FamilyGroup;
import com.swp.autocarwash.customer.entity.FamilyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FamilyMemberRepository extends JpaRepository<FamilyMember, Long> {
    Optional<FamilyMember> findByVehicleId(Long vehicleId);

    //VÁ LỖ HỔNG: Tìm thành viên theo xe, kèm điều kiện Gói Family của nhóm phải đang ACTIVE và CÒN HẠN
    @Query("SELECT fm FROM FamilyMember fm " +
            "JOIN FamilySubscription fs ON fm.familyGroup.id = fs.familyGroup.id " +
            "WHERE fm.vehicle.id = :vehicleId " +
            "AND fs.status = 'ACTIVE' " +
            "AND fs.endDate >= :currentDate")
    Optional<FamilyMember> findActiveFamilyMemberByVehicleId(@Param("vehicleId") Long vehicleId,
                                                             @Param("currentDate") LocalDate currentDate);

    // Hàm check xe đích dính gói Family
    @Query("SELECT COUNT(fs) > 0 FROM FamilyMember fm " +
            "JOIN FamilySubscription fs ON fm.familyGroup.id = fs.familyGroup.id " +
            "WHERE fm.vehicle.id = :vehicleId " +
            "AND fs.status = 'ACTIVE' " +
            "AND :today BETWEEN fs.startDate AND fs.endDate")
    boolean existsActiveFamilyByVehicleId(@Param("vehicleId") Long vehicleId, @Param("today") LocalDate today);

    long countByFamilyGroupAndIsDeletedFalse(FamilyGroup familyGroup);

    Optional<FamilyMember> findByCustomerAndIsDeletedFalse(Customer customer);



    // AC03: Kiểm tra khách hàng đã đi làm thành viên của nhóm nào khác chưa
    boolean existsByCustomerId(Long customerId);

    // AC04: Kiểm tra xe này đã bị trói vào nhóm gia đình nào khác chưa (Quy tắc UNIQUE xe)
    boolean existsByVehicleId(Long vehicleId);

    long countByFamilyGroupId(Long familyGroupId);

    // Tìm xem khách hàng có đang nằm trong một nhóm nào không
    Optional<FamilyMember> findByCustomerId(Long customerId);

    // Kéo tất cả các thành viên đang thuộc về nhóm này
    List<FamilyMember> findByFamilyGroupId(Long familyGroupId);

    // Lệnh xóa sạch toàn bộ bản ghi có chung familyGroupId (Quét sạch cả Member phụ và Owner)
    void deleteByFamilyGroupId(Long familyGroupId);

}
