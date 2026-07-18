package com.swp.autocarwash.staff.repository.custom;

import com.swp.autocarwash.staff.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Staff findByUserId(Long userId);

    @Query(value = """
            SELECT s.id AS employeeId,
                   CONCAT(s.firstName, ' ', s.lastName) AS fullName,
                   u.email AS email,
                   u.phone AS phone,
                   st.id AS stationId,
                   st.stationName AS stationName,
                   u.isActive AS active,
                   u.createdAt AS createdAt
            FROM Staff s JOIN s.user u JOIN s.station st
                 LEFT JOIN st.commune cm
                 LEFT JOIN cm.province pv
            WHERE u.isDeleted = false
              AND (:keyword IS NULL
                   OR CONCAT(s.firstName, ' ', s.lastName) LIKE CONCAT('%', :keyword, '%')
                   OR u.email LIKE CONCAT('%', :keyword, '%')
                   OR u.phone LIKE CONCAT('%', :keyword, '%'))
              AND (:active IS NULL OR u.isActive = :active)
              AND (:stationId IS NULL OR st.id = :stationId)
              AND (:communeId IS NULL OR cm.id = :communeId)
              AND (:provinceId IS NULL OR pv.id = :provinceId)
            ORDER BY s.id ASC
            """,
            countQuery = """
            SELECT COUNT(s.id) FROM Staff s JOIN s.user u JOIN s.station st
                 LEFT JOIN st.commune cm LEFT JOIN cm.province pv
            WHERE u.isDeleted = false
              AND (:keyword IS NULL
                   OR CONCAT(s.firstName, ' ', s.lastName) LIKE CONCAT('%', :keyword, '%')
                   OR u.email LIKE CONCAT('%', :keyword, '%')
                   OR u.phone LIKE CONCAT('%', :keyword, '%'))
              AND (:active IS NULL OR u.isActive = :active)
              AND (:stationId IS NULL OR st.id = :stationId)
              AND (:communeId IS NULL OR cm.id = :communeId)
              AND (:provinceId IS NULL OR pv.id = :provinceId)
            """)
    Page<EmployeeListProjection> findEmployeeList(
            @Param("keyword") String keyword,
            @Param("active") Boolean active,
            @Param("stationId") Integer stationId,
            @Param("communeId") Integer communeId,
            @Param("provinceId") Integer provinceId,
            Pageable pageable);

    @Query("""
            SELECT COUNT(s.id) FROM Staff s JOIN s.user u JOIN s.station st
                 LEFT JOIN st.commune cm LEFT JOIN cm.province pv
            WHERE u.isDeleted = false
              AND (:stationId IS NULL OR st.id = :stationId)
              AND (:communeId IS NULL OR cm.id = :communeId)
              AND (:provinceId IS NULL OR pv.id = :provinceId)
            """)
    long countQualifiedEmployees(
            @Param("stationId") Integer stationId,
            @Param("communeId") Integer communeId,
            @Param("provinceId") Integer provinceId);

    @Query("""
            SELECT COUNT(s.id) FROM Staff s JOIN s.user u JOIN s.station st
                 LEFT JOIN st.commune cm LEFT JOIN cm.province pv
            WHERE u.isDeleted = false AND u.createdAt >= :monthStart AND u.createdAt < :monthEnd
              AND (:stationId IS NULL OR st.id = :stationId)
              AND (:communeId IS NULL OR cm.id = :communeId)
              AND (:provinceId IS NULL OR pv.id = :provinceId)
            """)
    long countNewThisMonth(
            @Param("monthStart") LocalDateTime monthStart,
            @Param("monthEnd") LocalDateTime monthEnd,
            @Param("stationId") Integer stationId,
            @Param("communeId") Integer communeId,
            @Param("provinceId") Integer provinceId);

    @Query("SELECT s FROM Staff s JOIN FETCH s.user u JOIN FETCH s.station st WHERE s.id = :employeeId AND u.isDeleted = false")
    Optional<Staff> findEmployeeDetailById(@Param("employeeId") Long employeeId);
}
