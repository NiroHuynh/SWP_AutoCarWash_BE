package com.swp.autocarwash.auth.repository;

import com.swp.autocarwash.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Lấy role theo tên (vd "CUSTOMER") thay vì hardcode id,
     * vì id là AUTO_INCREMENT nên không cố định giữa các môi trường.
     */
    Optional<Role> findByName(String name);

}