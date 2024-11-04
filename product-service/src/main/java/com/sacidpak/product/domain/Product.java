package com.sacidpak.product.domain;

import com.sacidpak.clients.QuantityType;
import com.sacidpak.common.domain.BaseEntity;
import com.sacidpak.product.dto.ProductDto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product",
        indexes = {
                @Index(name = "ix_product_barcode", columnList = "barcode")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "ix_unique_product_barcode", columnNames = "barcode")
        })
public class Product extends BaseEntity<Product, ProductDto> {

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "sku", nullable = false)
    private String sku;

    @NotNull
    @Column(name = "barcode", nullable = false)
    private String barcode;

    @NotNull
    @Column(name = "is_frozen", nullable = false)
    private Boolean isFrozen;

    @NotNull
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "discount")
    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "quantity_type", nullable = false)
    private QuantityType quantityType;

}
