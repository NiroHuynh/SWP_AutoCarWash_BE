package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.Customer;
import com.swp.autocarwash.customer.entity.FamilyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FamilyGroupRepository extends JpaRepository<FamilyGroup,Long> {
    @Query("""
        SELECT fg.ownerCustomer.id
        FROM FamilyGroup fg
        JOIN FamilyMember fm
            ON fm.familyGroup.id = fg.id
        WHERE fm.customer.id = :customerId
          AND fg.isDeleted = false
    """)
    Long findOwnerCustomerIdByMemberCustomerId(
            @Param("customerId") Long customerId
    );

    // AC03: Kiểm tra xem khách hàng này có đang là chủ của nhóm nào chưa bị xóa mềm không
    boolean existsByOwnerCustomerIdAndIsDeletedFalse(Long ownerCustomerId);

    //Tìm xem khách hàng có đang làm Owner của nhóm nào không.
    Optional<FamilyGroup> findByOwnerCustomerId(Long ownerCustomerId);


    Optional<FamilyGroup> findByOwnerCustomerAndIsDeletedFalse(Customer owner);

    Optional<FamilyGroup> findByIdAndIsDeletedFalse(Long id);

    //Chỉ lấy nhóm đang hoạt động, bỏ qua các nhóm cũ đã bị soft delete
    Optional<FamilyGroup> findByOwnerCustomerIdAndIsDeletedFalse(Long ownerCustomerId);
}
