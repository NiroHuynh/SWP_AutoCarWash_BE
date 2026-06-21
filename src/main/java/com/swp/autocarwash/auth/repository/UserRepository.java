package com.swp.autocarwash.auth.repository;

import com.swp.autocarwash.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Code gốc (derived query, không JOIN FETCH role):
    // User findByEmailAndIsDeletedFalse(String email);
    // User findByPhoneAndIsDeletedFalse(String phone);

    // Lý do đổi: JwtAuthenticationFilter gọi 2 method này để load User TRƯỚC khi DispatcherServlet
    // mở Open-Session-In-View, nên session Hibernate đóng ngay sau khi query xong. Vì User.role là
    // @ManyToOne(LAZY), gọi user.getRole().getName() sau đó (trong UserCustomerDetails.getAuthorities())
    // bị lỗi LazyInitializationException: "no session" -> mọi API cần JWT đều trả về 403 rỗng.
    // JOIN FETCH role ngay trong query để role được load cùng lúc với user, không cần session riêng.
    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.email = :email AND u.isDeleted = false")
    User findByEmailAndIsDeletedFalse(@Param("email") String email);

    @Query("SELECT u FROM User u JOIN FETCH u.role WHERE u.phone = :phone AND u.isDeleted = false")
    User findByPhoneAndIsDeletedFalse(@Param("phone") String phone);
}
