package com.sacidpak.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sacidpak.clients.product.InventoryOrderItemDto;
import com.sacidpak.clients.product.InventoryUpdateRequest;
import com.sacidpak.clients.product.ProductClient;
import com.sacidpak.order.domain.Order;
import com.sacidpak.order.domain.OrderItem;
import com.sacidpak.order.domain.Outbox;
import com.sacidpak.order.enums.OutboxEventType;
import com.sacidpak.order.repository.OrderItemRepository;
import com.sacidpak.order.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryUpdateRunnable {

    private final ProductClient productClient;

    private final OrderItemRepository orderItemRepository;

    private final OutboxRepository outboxRepository;

    @Retryable(
            value = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Async(value = "orderTaskExecutor")
    @Transactional(readOnly = true)
    public void updateInventory(Order order, List<OrderItem> orderItems) {
        InventoryUpdateRequest request = null;
        try {
            var mapper = new ModelMapper();
            var products = orderItems.stream()
                    .map(element -> mapper.map(element, InventoryOrderItemDto.class))
                    .collect(Collectors.toList());

            request = InventoryUpdateRequest.builder()
                    .orderNumber(order.getOrderNumber())
                    .status(order.getStatus().name())
                    .products(products)
                    .build();

            var response = productClient.updateInventory(request);

            if (!response.getBody().isSuccess()){
                saveOutboxMessage(request);
                log.error("Error updating inventory");
            }
        }catch (Exception e) {
            saveOutboxMessage(request);
            log.error(e.getMessage());
        }
    }

    @Recover
    public void recover(Exception e, Order order) {
        log.error("Inventory update failed after retries for order: " + order.getOrderNumber(), e);
    }

    private void saveOutboxMessage(InventoryUpdateRequest request) {
        var outbox = Outbox.builder()
                .payload(createPayload(request))
                .eventType(OutboxEventType.INVENTORY)
                .build();

        outboxRepository.save(outbox);
    }

    private String createPayload(InventoryUpdateRequest request) {
        try {
            var objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
