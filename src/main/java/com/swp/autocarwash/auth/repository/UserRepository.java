package com.swp.autocarwash.auth.repository;

import org.springframework.stereotype.Repository;
import com.swp.autocarwash.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 *
 * UserRepository dùng để truy xuất dữ liệu người dùng từ cơ sở dữ liệu.
 *
 * @author Phong
 * @version 1.0
 */

public interface UserRepository extends JpaRepository<User, Long> {


    /**
     * Check email existing in database
     *
     * @param email user email
     * @return true if email exists
     */
    boolean existsByEmail(String email);


    /**
     * Check phone existing in database
     *
     * @param phone user phone
     * @return true if phone exists
     */
    boolean existsByPhone(String phone);


    /**
     * Find user by email
     *
     * @param email user email
     * @return user information
     */
    Optional<User> findByEmail(String email);
    
      User findByEmailAndIsDeletedFalse(String email);
    User findByPhoneAndIsDeletedFalse(String phone);

}
