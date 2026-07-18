package com.swp.autocarwash.queue.service;

import com.swp.autocarwash.customer.entity.Customer;

public interface QueueTicketService {
    public String generateTicketNumber(int stationId, boolean isBooked);

    /**
     * Tinh priority_score cho 1 queue_ticket moi = trong so tier cua khach + trong so booking (neu la ve tu booking).
     * Khach vang lai chua co tai khoan (customer == null) hoac chua co tier duoc mac dinh ve hang MEMBER.
     */
    Integer computePriorityScore(Customer customer, boolean isBooking);
}
