package com.sacidpak.product.dto;

import com.sacidpak.clients.QuantityType;
import com.sacidpak.common.dto.BaseEntityDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto extends BaseEntityDto {

    private String name;

    private String sku;

    private String barcode;

    private Boolean isFrozen;

    private BigDecimal price;

    private BigDecimal discount;

    private QuantityType quantityType;
}
