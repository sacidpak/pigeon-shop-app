package com.sacidpak.clients.product;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryOrderItemDto {

    @NotNull
    private String barcode;

    @NotNull
    private BigDecimal quantity;
}
