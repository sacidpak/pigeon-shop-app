package com.sacidpak.order.service;

import com.sacidpak.clients.product.InventoryOrderItemDto;
import com.sacidpak.clients.product.InventoryUpdateRequest;
import com.sacidpak.clients.product.ProductClient;
import com.sacidpak.order.domain.Order;
import com.sacidpak.order.domain.OrderItem;
import com.sacidpak.order.repository.OrderItemRepository;
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

    @Retryable(
            value = { Exception.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    @Async(value = "orderTaskExecutor")
    @Transactional(readOnly = true)
    public void updateInventory(Order order, List<OrderItem> orderItems) {
        try {
            var mapper = new ModelMapper();
            var products = orderItems.stream()
                    .map(element -> mapper.map(element, InventoryOrderItemDto.class))
                    .collect(Collectors.toList());

            var request = InventoryUpdateRequest.builder()
                    .orderNumber(order.getOrderNumber())
                    .status(order.getStatus().name())
                    .products(products)
                    .build();

            var response = productClient.updateInventory(request);

            if (!response.getBody().isSuccess()){
                //TODO: save and retry inventory request
                log.error("Error updating inventory");
            }
        }catch (Exception e) {
            //TODO: save and retry inventory request
            log.error(e.getMessage());
        }
    }

    @Recover
    public void recover(Exception e, Order order) {
        log.error("Inventory update failed after retries for order: " + order.getOrderNumber(), e);
    }
}
