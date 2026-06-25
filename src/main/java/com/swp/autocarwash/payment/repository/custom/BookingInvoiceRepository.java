package com.swp.autocarwash.payment.repository.custom;

import com.swp.autocarwash.payment.entity.BookingInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingInvoiceRepository extends JpaRepository<BookingInvoice,Long> {
}
