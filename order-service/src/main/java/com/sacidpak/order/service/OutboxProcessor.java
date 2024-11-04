package com.sacidpak.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sacidpak.clients.product.InventoryUpdateRequest;
import com.sacidpak.clients.product.ProductClient;
import com.sacidpak.order.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxProcessor {

    private final OutboxRepository outboxMessageRepository;

    private final ProductClient productClient;

    @Scheduled(fixedRate = 3000000) //5min
    public void processOutboxMessages() {
        var messages = outboxMessageRepository.findFailedInventoryMessage();
        messages.forEach(outboxMessage -> {
            try {
                var objectMapper = new ObjectMapper();
                var request = objectMapper.readValue(outboxMessage.getPayload(), InventoryUpdateRequest.class);
                productClient.updateInventory(request);

                outboxMessage.setProcessed(Boolean.TRUE);
                outboxMessageRepository.save(outboxMessage);

            } catch (Exception e) {
                log.error("Inventory processing failed outbox message id: {}", outboxMessage.getId());
            }
        });
    }
}
