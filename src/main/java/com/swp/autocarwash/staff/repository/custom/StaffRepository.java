package com.swp.autocarwash.staff.repository.custom;

import com.swp.autocarwash.staff.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Staff findByUserId(Long userId);
}
