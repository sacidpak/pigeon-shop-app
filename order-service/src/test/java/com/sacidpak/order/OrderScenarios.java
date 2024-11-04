package com.sacidpak.order;

import com.sacidpak.clients.QuantityType;
import com.sacidpak.clients.product.ProductInfoResponse;
import com.sacidpak.order.data.request.CreateOrderItemRequest;
import com.sacidpak.order.data.request.CreateOrderRequest;
import com.sacidpak.order.domain.Order;
import com.sacidpak.order.domain.OrderItem;
import com.sacidpak.order.dto.CustomerDto;
import com.sacidpak.order.dto.OrderDto;
import com.sacidpak.order.dto.OrderItemDto;
import com.sacidpak.order.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class OrderScenarios {

    public static CreateOrderRequest getCreateOrderRequest() {
        var customer = CustomerDto.builder()
                .fullName("John Doe")
                .phoneNumber("1234567890")
                .addressCode("ADDR123")
                .build();

        var orderItem1 = CreateOrderItemRequest.builder()
                .barcode("123456789")
                .quantity(BigDecimal.valueOf(2))
                .build();

        var orderItem2 = CreateOrderItemRequest.builder()
                .barcode("987654321")
                .quantity(BigDecimal.valueOf(1))
                .build();

        var orderItems = new ArrayList<CreateOrderItemRequest>();
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);

        return CreateOrderRequest.builder()
                .customer(customer)
                .orderNote("Please deliver after 5 PM.")
                .orderItems(orderItems)
                .build();
    }

    public static List<OrderItem> getOrderItems() {
        var order = getOrder();

        var orderItem1 = OrderItem.builder()
                .order(order)
                .barcode("123456789")
                .productName("Product A")
                .unitPrice(BigDecimal.valueOf(10))
                .totalPrice(BigDecimal.valueOf(20))
                .quantity(BigDecimal.valueOf(2))
                .quantityType(QuantityType.PIECE)
                .build();

        var orderItem2 = OrderItem.builder()
                .order(order)
                .barcode("987654321")
                .productName("Product B")
                .unitPrice(BigDecimal.valueOf(15))
                .totalPrice(BigDecimal.valueOf(15))
                .quantity(BigDecimal.valueOf(1))
                .quantityType(QuantityType.PIECE)
                .build();

        return new ArrayList<>(Arrays.asList(orderItem1, orderItem2));
    }

    public static Order getOrder() {
        return Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .customerName("John Doe")
                .customerPhoneNumber("1234567890")
                .customerAddressCode("ADDR123")
                .status(OrderStatus.NEW_ORDER)
                .build();
    }

    public static List<ProductInfoResponse> getProductInfoResponseList() {
        var productInfo1 = ProductInfoResponse.builder()
                .barcode("123456789")
                .name("Product A")
                .price(BigDecimal.valueOf(10))
                .quantity(BigDecimal.valueOf(5))
                .quantityType(QuantityType.PIECE)
                .discount(BigDecimal.ZERO)
                .build();

        var productInfo2 = ProductInfoResponse.builder()
                .barcode("987654321")
                .name("Product B")
                .price(BigDecimal.valueOf(15))
                .quantity(BigDecimal.valueOf(3))
                .quantityType(QuantityType.PIECE)
                .discount(BigDecimal.ZERO)
                .build();

        return new ArrayList<>(Arrays.asList(productInfo1, productInfo2));
    }

    public static OrderDto getOrderDto() {
        var order = getOrder();

        var orderItemDto1 = OrderItemDto.builder()
                .barcode("123456789")
                .productName("Product A")
                .unitPrice(BigDecimal.valueOf(10))
                .totalPrice(BigDecimal.valueOf(20))
                .quantity(BigDecimal.valueOf(2))
                .quantityType(QuantityType.PIECE)
                .build();

        var orderItemDto2 = OrderItemDto.builder()
                .barcode("987654321")
                .productName("Product B")
                .unitPrice(BigDecimal.valueOf(15))
                .totalPrice(BigDecimal.valueOf(15))
                .quantity(BigDecimal.valueOf(1))
                .quantityType(QuantityType.PIECE)
                .build();

        var orderItems = new ArrayList<OrderItemDto>();
        orderItems.add(orderItemDto1);
        orderItems.add(orderItemDto2);

        return OrderDto.builder()
                .orderNumber(order.getOrderNumber())
                .customerName(order.getCustomerName())
                .customerPhoneNumber(order.getCustomerPhoneNumber())
                .customerAddressCode(order.getCustomerAddressCode())
                .status(order.getStatus())
                .orderItems(orderItems)
                .build();
    }
}

