package com.swp.autocarwash.auth.repository;
import com.swp.autocarwash.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
     * Check email đã thuộc về user KHÁC hay chưa. Bản existsByEmail ở trên dùng cho
     * luồng register nên không loại trừ được chính mình - khi update, user giữ nguyên
     * email cũ của chính họ sẽ bị báo trùng oan.
     *
     * @param email email cần kiểm tra
     * @param id    id của user đang được cập nhật (loại trừ khỏi phép kiểm tra)
     * @return true nếu email đã được user khác dùng
     */
    boolean existsByEmailAndIdNot(String email, Long id);


    /**
     * Check phone đã thuộc về user KHÁC hay chưa (xem existsByEmailAndIdNot).
     *
     * @param phone phone cần kiểm tra
     * @param id    id của user đang được cập nhật (loại trừ khỏi phép kiểm tra)
     * @return true nếu phone đã được user khác dùng
     */
    boolean existsByPhoneAndIdNot(String phone, Long id);


    /**
     * Find user by email
     *
     * @param email user email
     * @return user information
     */
    Optional<User> findByEmail(String email);


}
