package com.swp.autocarwash.queue.service.impl;

import com.swp.autocarwash.queue.dto.response.QueueTicketResponse;
import com.swp.autocarwash.queue.mapper.QueueMapper;
import com.swp.autocarwash.queue.repository.custom.QueueTicketRepository;
import com.swp.autocarwash.queue.service.QueueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class QueueServiceImpl implements QueueService {
    private static final List<String> ACTIVE_STATUSES = List.of("WAITING");
    private final QueueTicketRepository queueTicketRepository;
    private final QueueMapper queueMapper;

    @Override
    @Transactional
    public List<QueueTicketResponse> getActiveQueue() {
        return queueTicketRepository.findQueueTicketListByStatus(ACTIVE_STATUSES).stream().map(queueMapper :: toResponse).toList();
    }
}
