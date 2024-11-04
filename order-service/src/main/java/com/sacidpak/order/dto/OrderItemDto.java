package com.sacidpak.order.dto;

import com.sacidpak.clients.QuantityType;
import com.sacidpak.common.dto.BaseEntityDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto extends BaseEntityDto {

    private String barcode;

    private String productName;

    private BigDecimal quantity;

    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    private QuantityType quantityType;
}
