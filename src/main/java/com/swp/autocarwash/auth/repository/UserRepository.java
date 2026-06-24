package com.swp.autocarwash.auth.repository;

import com.swp.autocarwash.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.email = :email AND u.isDeleted = false")
    User findByEmailAndIsDeletedFalse(@Param("email") String email);

    User findByPhoneAndIsDeletedFalse(String phone);


}
