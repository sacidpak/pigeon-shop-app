package com.sacidpak.clients.product;

import com.sacidpak.clients.QuantityType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoResponse {

    private String barcode;

    private String name;

    private BigDecimal price;

    private BigDecimal discount;

    private QuantityType quantityType;

    private BigDecimal quantity;
}
