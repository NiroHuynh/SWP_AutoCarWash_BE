package com.swp.autocarwash.booking.repository;

import com.swp.autocarwash.booking.entity.BookingAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository truy vấn dịch vụ bổ sung ({@link BookingAddon}) theo lịch đặt.
 *
 * @author KimNgan
 * @version 1.0
 */
@Repository
public interface BookingAddonRepository extends JpaRepository<BookingAddon, Long> {

    /**
     * Lấy danh sách addon của một booking, kèm thông tin tên và giá từ {@code AddonService}.
     *
     * @param bookingId mã định danh của lịch đặt
     * @return danh sách {@link BookingAddon} đã eager-fetch {@code addonService}
     */
    @Query("SELECT ba FROM BookingAddon ba " +
           "JOIN FETCH ba.addonService " +
           "WHERE ba.booking.id = :bookingId")
    public List<BookingAddon> findByBookingId(@Param("bookingId") Long bookingId);

    void deleteByBookingId(Long bookingId);

}
