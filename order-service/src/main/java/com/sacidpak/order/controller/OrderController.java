package com.sacidpak.order.controller;

import com.sacidpak.order.data.request.CreateOrderRequest;
import com.sacidpak.order.data.response.CreateOrderResponse;
import com.sacidpak.order.data.response.DetailedOrderResponse;
import com.sacidpak.order.data.response.OrderResponse;
import com.sacidpak.order.dto.OrderDto;
import com.sacidpak.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PatchMapping("/cancel/{order-number}")
    public ResponseEntity<Void> createOrder(@PathVariable("order-number") String orderNumber) {
        orderService.cancelOrder(orderNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{order-number}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("order-number") String orderNumber) {
        return ResponseEntity.ok(orderService.getOrder(orderNumber));
    }

    @GetMapping("/{phone-number}")
    public ResponseEntity<Page<OrderResponse>> getOrders(@PathVariable("phone-number") String phoneNumber,
                                                         @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(orderService.getOrders(phoneNumber));
    }
}
