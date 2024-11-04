package com.sacidpak.product.domain;

import com.sacidpak.common.domain.BaseEntity;
import com.sacidpak.product.dto.InventoryTransactionDto;
import com.sacidpak.product.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventory_transaction",
        indexes = {
                @Index(name = "ix_inventory_transaction_order_number", columnList = "order_number"),
                @Index(name = "ix_inventory_transaction_status", columnList = "status")
        })
public class InventoryTransaction extends BaseEntity<InventoryTransaction, InventoryTransactionDto> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    private BigDecimal currentQuantity;

    private BigDecimal transactionQuantity;

    private BigDecimal quantity;

    private String orderNumber;

    private TransactionStatus status;
}
