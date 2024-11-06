package com.sacidpak.order;

import com.sacidpak.clients.product.ProductClient;
import com.sacidpak.common.exception.BusinessException;
import com.sacidpak.order.domain.Order;
import com.sacidpak.order.domain.OrderHistory;
import com.sacidpak.order.enums.OrderStatus;
import com.sacidpak.order.repository.OrderHistoryRepository;
import com.sacidpak.order.repository.OrderItemRepository;
import com.sacidpak.order.repository.OrderRepository;
import com.sacidpak.order.service.InventoryUpdateRunnable;
import com.sacidpak.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class  OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @Mock
    private InventoryUpdateRunnable inventoryUpdateRunnable;

    @Mock
    private ProductClient productClient;


    @Test
    @DisplayName("Should create order and return response")
    void shouldCreateOrder_whenGivenValidRequest() {
        // given
        var request = OrderScenarios.getCreateOrderRequest();
        var customer = request.getCustomer();
        var orderNumber = UUID.randomUUID().toString();
        var order = Order.builder()
                .customerName(customer.getFullName())
                .orderNote(request.getOrderNote())
                .orderNumber(orderNumber)
                .customerPhoneNumber(customer.getPhoneNumber())
                .customerAddressCode(customer.getAddressCode())
                .status(OrderStatus.NEW_ORDER)
                .build();
        var orderItems = OrderScenarios.getOrderItems();
        var productInfoList = OrderScenarios.getProductInfoResponseList();

        when(orderRepository.saveAndFlush(any(Order.class))).thenReturn(order);
        when(orderItemRepository.saveAll(anyList())).thenReturn(orderItems);
        when(productClient.getProductInfo(any())).thenReturn(ResponseEntity.ok(productInfoList));

        // when
        var response = orderService.createOrder(request);

        // then
        assertThat(response).isNotNull();
        verify(orderRepository, times(1)).saveAndFlush(any(Order.class));
        verify(orderHistoryRepository, times(1)).save(any(OrderHistory.class));
        verify(inventoryUpdateRunnable, times(1)).updateInventory(any(Order.class), anyList());
    }

    @Test
    @DisplayName("Should cancel order when order number exists")
    void shouldCancelOrder_whenOrderNumberExists() {
        // given
        var orderNumber = "testOrder123";
        var order = Order.builder()
                .orderNumber(orderNumber)
                .status(OrderStatus.NEW_ORDER)
                .build();
        var orderItems = OrderScenarios.getOrderItems();
        var cancelableStatus = List.of(OrderStatus.NEW_ORDER, OrderStatus.PREPARING);

        when(orderRepository.findByOrderNumberAndStatuses(orderNumber, cancelableStatus)).thenReturn(Optional.of(order));
        when(orderItemRepository.findAllByOrderNumber(orderNumber)).thenReturn(orderItems);

        // when
        orderService.cancelOrder(orderNumber);

        // then

        verify(orderRepository, times(1)).save(order);
        verify(orderHistoryRepository, times(1)).save(any(OrderHistory.class));
        verify(inventoryUpdateRunnable, times(1)).updateInventory(order, orderItems);
    }

    @Test
    @DisplayName("Should throw BusinessException when order number does not exist")
    void shouldThrowException_whenOrderNumberDoesNotExist() {
        // given
        var orderNumber = "nonExistingOrder123";
        var cancelableStatus = List.of(OrderStatus.NEW_ORDER, OrderStatus.PREPARING);

        when(orderRepository.findByOrderNumberAndStatuses(orderNumber, cancelableStatus)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BusinessException.class, () -> orderService.cancelOrder(orderNumber));
    }

    @Test
    @DisplayName("Should get order details by order number")
    void shouldGetOrderDetails_whenOrderNumberExists() {
        // given
        var orderNumber = "testOrder123";
        var order = OrderScenarios.getOrder();
        var orderItems = OrderScenarios.getOrderItems();
        var orderDto = OrderScenarios.getOrderDto();

        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(order));
        when(orderItemRepository.findAllByOrderNumber(orderNumber)).thenReturn(orderItems);

        // when
        var result = orderService.getOrder(orderNumber);

        // then
        assertThat(result).isNotNull();
        verify(orderRepository, times(1)).findByOrderNumber(orderNumber);
        verify(orderItemRepository, times(1)).findAllByOrderNumber(orderNumber);
    }

    @Test
    @DisplayName("Should throw BusinessException when order not found by order number")
    void shouldThrowException_whenOrderNotFoundByOrderNumber() {
        // given
        var orderNumber = "nonExistingOrder123";

        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());

        // when & then
        assertThrows(BusinessException.class, () -> orderService.getOrder(orderNumber));
    }
}
