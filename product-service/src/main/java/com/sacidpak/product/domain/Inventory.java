package com.sacidpak.product.domain;

import com.sacidpak.common.domain.BaseEntity;
import com.sacidpak.product.dto.InventoryDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventory")
public class Inventory extends BaseEntity<Inventory, InventoryDto> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    private BigDecimal quantity;
}
