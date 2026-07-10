package com.swp.autocarwash.customer.repository;

import com.swp.autocarwash.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 *
 * Chức năng: CustomerRepository cung cấp các phương thức truy cập dữ liệu
 * cho Customer entity. Repository này hỗ trợ thao tác CRUD và truy vấn customer
 * trong database.
 *
 * @author Phong
 * @version 1.0
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    /**
     *
     * Chức năng: Tìm kiếm customer theo customer id.
     *
     * Quy trình:
     * - Nhận customerId cần tìm kiếm.
     * - Thực hiện truy vấn customer trong database.
     * - Trả về Optional chứa Customer nếu tồn tại.
     * - Trả về Optional.empty() nếu không tìm thấy customer.
     *
     * @param id id của customer cần tìm
     *
     * @return Optional<Customer> chứa customer nếu tồn tại
     *
     * @author Phong
     * @version 1.0
     */
    Optional<Customer> findById(Long id);

    Optional<Customer> findCustomerByUserId(Long userId);

    Customer findByUserId(Long userId);

    @Query("SELECT c FROM Customer c WHERE c.user.phone = :phone")
    Optional<Customer> findByUserPhone(@Param("phone") String phone);

    // reset chủ động: restricted_until < now tự loại trừ NULL, không cần IS NOT NULL riêng
    List<Customer> findByRestrictedUntilBefore(Instant now);

    /**
     * Danh sách toàn bộ khách hàng trong hệ thống (không phân biệt tài khoản
     * active/inactive), kèm "lần ghé cuối" (MAX checkOutAt của booking CHECK_OUT
     * nếu có — NULL nếu chưa từng CHECK_OUT), mặc định sort giảm dần theo lần
     * ghé cuối, NULL rơi xuống cuối (FE-US-09).
     */
    @Query(value = """
            SELECT c.id AS customerId,
                   CONCAT(c.firstName, ' ', c.lastName) AS fullName,
                   u.email AS email,
                   u.phone AS phone,
                   u.isActive AS active,
                   ct.tierName AS tier,
                   MAX(b.checkOutAt) AS lastVisit
            FROM Customer c JOIN c.user u
                 LEFT JOIN c.customerTier ct
                 LEFT JOIN Booking b ON b.customer = c AND b.status = 'CHECK_OUT'
            WHERE u.isDeleted = false
              AND (:keyword IS NULL
                   OR CONCAT(c.firstName, ' ', c.lastName) LIKE CONCAT('%', :keyword, '%')
                   OR u.email LIKE CONCAT('%', :keyword, '%')
                   OR u.phone LIKE CONCAT('%', :keyword, '%'))
              AND (:year IS NULL OR YEAR(u.createdAt) = :year)
              AND (:month IS NULL OR MONTH(u.createdAt) = :month)
              AND (:tier IS NULL OR ct.tierName = :tier)
              AND (:active IS NULL OR u.isActive = :active)
            GROUP BY c.id, c.firstName, c.lastName, u.email, u.phone, u.isActive, ct.tierName
            ORDER BY MAX(b.checkOutAt) DESC
            """,
            countQuery = """
            SELECT COUNT(c.id) FROM Customer c JOIN c.user u LEFT JOIN c.customerTier ct
            WHERE u.isDeleted = false
              AND (:keyword IS NULL
                   OR CONCAT(c.firstName, ' ', c.lastName) LIKE CONCAT('%', :keyword, '%')
                   OR u.email LIKE CONCAT('%', :keyword, '%')
                   OR u.phone LIKE CONCAT('%', :keyword, '%'))
              AND (:year IS NULL OR YEAR(u.createdAt) = :year)
              AND (:month IS NULL OR MONTH(u.createdAt) = :month)
              AND (:tier IS NULL OR ct.tierName = :tier)
              AND (:active IS NULL OR u.isActive = :active)
            """)
    Page<CustomerListProjection> findCustomerList(
            @Param("keyword") String keyword,
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("tier") String tier,
            @Param("active") Boolean active,
            Pageable pageable);

    @Query("SELECT COUNT(c) FROM Customer c JOIN c.user u WHERE u.isDeleted = false")
    long countQualifiedCustomers();

    @Query("""
            SELECT COUNT(c.id) FROM Customer c JOIN c.user u
            WHERE u.isDeleted = false AND u.createdAt >= :monthStart AND u.createdAt < :monthEnd
            """)
    long countNewThisMonth(@Param("monthStart") LocalDateTime monthStart, @Param("monthEnd") LocalDateTime monthEnd);

    @Query("SELECT COUNT(c) FROM Customer c JOIN c.user u WHERE c.customerTier.tierName = 'GOLD' AND u.isDeleted = false")
    long countGoldMembers();

}
