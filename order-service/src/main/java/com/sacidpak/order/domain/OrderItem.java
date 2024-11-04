package com.sacidpak.order.domain;

import com.sacidpak.clients.QuantityType;
import com.sacidpak.common.domain.BaseEntity;
import com.sacidpak.order.dto.OrderItemDto;
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
@Table(name = "order_item",
        indexes = {
                @Index(name = "ix_order_item_barcode", columnList = "barcode")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "ix_unique_order_item_order_id_barcode", columnNames = {"barcode","order_id"})
        })
public class OrderItem extends BaseEntity<OrderItem, OrderItemDto> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private Order order;

    @NotNull
    @Column(name = "barcode", nullable = false)
    private String barcode;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "quantity", nullable = false, precision = 18, scale = 3)
    private BigDecimal quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_type", nullable = false)
    private QuantityType quantityType;

    @NotNull
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;
}
