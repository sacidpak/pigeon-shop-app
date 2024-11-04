package com.sacidpak.order.domain;

import com.sacidpak.common.domain.BaseEntity;
import com.sacidpak.order.dto.OrderDto;
import com.sacidpak.order.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders",
        indexes = {
                @Index(name = "ix_order_order_number", columnList = "order_number"),
                @Index(name = "ix_order_status", columnList = "status"),
                @Index(name = "ix_customer_phone_number", columnList = "customer_phone_number")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "ix_unique_order_number", columnNames = "order_number")
        })
public class Order extends BaseEntity<Order, OrderDto> {

    @NotNull
    @Column(name = "order_number", nullable = false, length = 36)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "order_note", length = 500)
    private String orderNote;

    @Column(name = "customer_phone_number", nullable = false, length = 15)
    private String customerPhoneNumber;

    @Column(name = "customer_name", length = 100)
    private String customerName;

    @Column(name = "customer_address_code", length = 50)
    private String customerAddressCode;

    @NotNull
    @Builder.Default
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
