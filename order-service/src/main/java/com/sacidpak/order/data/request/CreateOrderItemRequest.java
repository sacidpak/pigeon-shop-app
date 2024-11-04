package com.sacidpak.order.data.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderItemRequest {

    private String barcode;

    private BigDecimal quantity;
}
