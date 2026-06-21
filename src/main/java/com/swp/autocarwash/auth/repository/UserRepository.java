package com.swp.autocarwash.auth.repository;

import com.swp.autocarwash.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailAndIsDeletedFalse(String email);
    User findByPhoneAndIsDeletedFalse(String phone);
}
