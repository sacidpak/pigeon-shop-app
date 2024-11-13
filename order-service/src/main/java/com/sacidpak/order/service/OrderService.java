package com.sacidpak.order.service;

import com.sacidpak.clients.QuantityType;
import com.sacidpak.clients.product.ProductClient;
import com.sacidpak.clients.product.ProductInfoRequest;
import com.sacidpak.clients.product.ProductInfoResponse;
import com.sacidpak.common.exception.BusinessException;
import com.sacidpak.order.data.request.CreateOrderItemRequest;
import com.sacidpak.order.data.request.CreateOrderRequest;
import com.sacidpak.order.data.response.CreateOrderResponse;
import com.sacidpak.order.data.response.DetailedOrderResponse;
import com.sacidpak.order.data.response.OrderResponse;
import com.sacidpak.order.domain.Order;
import com.sacidpak.order.domain.OrderHistory;
import com.sacidpak.order.domain.OrderItem;
import com.sacidpak.order.dto.OrderDto;
import com.sacidpak.order.dto.OrderItemDto;
import com.sacidpak.order.enums.OrderStatus;
import com.sacidpak.order.repository.OrderHistoryRepository;
import com.sacidpak.order.repository.OrderItemRepository;
import com.sacidpak.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.sacidpak.order.enums.OrderValidationType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final OrderHistoryRepository orderHistoryRepository;

    private final InventoryUpdateRunnable inventoryUpdateRunnable;

    private final ProductClient productClient;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
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

        order = orderRepository.saveAndFlush(order);
        var orderItems = saveOrderItems(order, request);
        saveOrderHistory(order, OrderStatus.NEW_ORDER);

        inventoryUpdateRunnable.updateInventory(order, orderItems);

        return CreateOrderResponse.builder().orderNumber(orderNumber).build();
    }

    @Transactional
    public void cancelOrder(String orderNumber) {
        var cancelableStatus = List.of(OrderStatus.NEW_ORDER, OrderStatus.PREPARING);
        orderRepository.findByOrderNumberAndStatuses(orderNumber, cancelableStatus)
                .ifPresentOrElse(order -> {
                    order.setStatus(OrderStatus.CANCELLED);
                    orderRepository.save(order);
                    saveOrderHistory(order, OrderStatus.CANCELLED);

                    var orderItems = orderItemRepository.findAllByOrderNumber(orderNumber);
                    inventoryUpdateRunnable.updateInventory(order, orderItems);
                }, () -> {
                    throw new BusinessException(ORDER_NOT_FOUND);
                });
    }

    private void saveOrderHistory(Order order, OrderStatus orderStatus) {
        var orderHistory = OrderHistory.builder()
                .order(order)
                .status(orderStatus)
                .build();
        orderHistoryRepository.save(orderHistory);
    }

    private List<OrderItem> saveOrderItems(Order order, CreateOrderRequest request) {
        var productInfoList = getProductInfo(request);
        var orderItems = new ArrayList<OrderItem>();
        request.getOrderItems().forEach(requestOrderItem -> {
            productInfoList.stream()
                    .filter(f -> f.getBarcode().equals(requestOrderItem.getBarcode())
                            && f.getQuantity().compareTo(requestOrderItem.getQuantity()) >= 0)
                    .findFirst()
                    .ifPresentOrElse(productInfo -> {
                        var unitPrice = getUnitePrice(productInfo);
                        var orderItem = OrderItem.builder()
                                .order(order)
                                .barcode(requestOrderItem.getBarcode())
                                .productName(productInfo.getName())
                                .unitPrice(unitPrice)
                                .totalPrice(requestOrderItem.getQuantity().multiply(unitPrice))
                                .quantity(requestOrderItem.getQuantity())
                                .quantityType(productInfo.getQuantityType())
                                .build();
                        orderItems.add(orderItem);
                    }, () -> {
                        throw new BusinessException(PRODUCT_NOT_FOUND);
                    });
        });
        return orderItemRepository.saveAll(orderItems);
    }

    private BigDecimal getUnitePrice(ProductInfoResponse productInfo) {
        if (productInfo.getDiscount() == null || productInfo.getQuantityType() == QuantityType.KG) {
            return productInfo.getPrice();
        }
        return productInfo.getPrice().subtract(productInfo.getDiscount());
    }

    public OrderDto getOrder(String orderNumber) {
        var optionalOrder = orderRepository.findByOrderNumber(orderNumber);
        if (optionalOrder.isPresent()) {
            var order = optionalOrder.get();
            var mapper = new ModelMapper();
            var orderDto = new ModelMapper().map(order, OrderDto.class);

            var orderItems = orderItemRepository.findAllByOrderNumber(orderNumber).stream()
                    .map(element -> mapper.map(element, OrderItemDto.class))
                    .collect(Collectors.toList());
            orderDto.setOrderItems(orderItems);

            return orderDto;
        }

        throw new BusinessException(ORDER_NOT_FOUND);
    }

    private List<ProductInfoResponse> getProductInfo(CreateOrderRequest request) {
        var barcodes = request.getOrderItems().stream().map(CreateOrderItemRequest::getBarcode).toList();
        var productInfoRequest = ProductInfoRequest.builder().barcodes(barcodes).build();
        var response = productClient.getProductInfo(productInfoRequest);

        if (response == null || response.getBody() == null) {
            throw new BusinessException(PRODUCT_SERVICE_NOT_AVAILABLE);
        }

        return response.getBody();
    }

    public Page<OrderResponse> getOrders(String phoneNumber) {
        //TODO: FIND ORDERS BY CUSTOMER PHONE NUMBER
        return null;
    }

    private Boolean checkCustomer(String phoneNumber) {
        // TODO: CHECK CUSTOMER FROM CUSTOMER SERVICE
        return Boolean.TRUE;
    }
}
