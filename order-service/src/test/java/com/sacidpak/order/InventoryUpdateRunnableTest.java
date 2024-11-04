package com.sacidpak.order;

import com.sacidpak.clients.product.InventoryOrderItemDto;
import com.sacidpak.clients.product.InventoryUpdateRequest;
import com.sacidpak.clients.product.InventoryUpdateResponse;
import com.sacidpak.clients.product.ProductClient;
import com.sacidpak.order.domain.Order;
import com.sacidpak.order.domain.OrderItem;
import com.sacidpak.order.enums.OrderStatus;
import com.sacidpak.order.repository.OrderItemRepository;
import com.sacidpak.order.service.InventoryUpdateRunnable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryUpdateRunnableTest {

    @Mock
    private ProductClient productClient;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private InventoryUpdateRunnable inventoryUpdateRunnable;

    private Order order;

    private List<OrderItem> orderItems;

    private InventoryUpdateRequest request;

    @BeforeEach
    void setUp() {
        order = Order.builder().orderNumber("12345").status(OrderStatus.NEW_ORDER).build();
        orderItems = List.of(
                OrderItem.builder().barcode("123456789").quantity(BigDecimal.valueOf(2)).build(),
                OrderItem.builder().barcode("987654321").quantity(BigDecimal.valueOf(1)).build()
        );

        var mapper = new ModelMapper();
        var products = orderItems.stream()
                .map(item -> mapper.map(item, InventoryOrderItemDto.class))
                .collect(Collectors.toList());

        request = InventoryUpdateRequest.builder()
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus().name())
                .products(products)
                .build();
    }

    @Test
    void testUpdateInventory_SuccessfulInventoryUpdate() {
        when(productClient.updateInventory(any(InventoryUpdateRequest.class)))
                .thenReturn(ResponseEntity.ok(new InventoryUpdateResponse(true, null)));

        inventoryUpdateRunnable.updateInventory(order, orderItems);

        verify(productClient, times(1)).updateInventory(any(InventoryUpdateRequest.class));

    }

    @Test
    void testUpdateInventory_FailedInventoryUpdate() {
        when(productClient.updateInventory(any(InventoryUpdateRequest.class)))
                .thenReturn(ResponseEntity.ok(new InventoryUpdateResponse(false, null)));

        inventoryUpdateRunnable.updateInventory(order, orderItems);

        verify(productClient, times(1)).updateInventory(any(InventoryUpdateRequest.class));
    }

    @Test
    void testUpdateInventory_ExceptionThrownDuringUpdate() {
        when(productClient.updateInventory(any(InventoryUpdateRequest.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        inventoryUpdateRunnable.updateInventory(order, orderItems);

        verify(productClient, times(1)).updateInventory(any(InventoryUpdateRequest.class));
    }
}
