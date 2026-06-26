package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.FamilyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
