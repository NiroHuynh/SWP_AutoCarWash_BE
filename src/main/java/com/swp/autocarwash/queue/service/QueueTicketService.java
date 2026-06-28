package com.swp.autocarwash.queue.service;

public interface QueueTicketService {
    public String generateTicketNumber(int stationId, boolean isBooked);
}
