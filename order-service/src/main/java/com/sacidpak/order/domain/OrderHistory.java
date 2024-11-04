package com.sacidpak.order.domain;

import com.sacidpak.common.domain.BaseEntity;
import com.sacidpak.order.dto.OrderHistoryDto;
import com.sacidpak.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_history",
        indexes = {
                @Index(name = "ix_order_history_status", columnList = "status"),
        })
public class OrderHistory extends BaseEntity<OrderHistory, OrderHistoryDto> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;
}
