package com.sacidpak.clients.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryUpdateRequest {

    @NotBlank
    private String orderNumber;

    @NotBlank
    private String status;

    @Valid
    @NotEmpty
    private List<InventoryOrderItemDto> products;
}
