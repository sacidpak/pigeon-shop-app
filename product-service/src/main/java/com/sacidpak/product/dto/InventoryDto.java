package com.sacidpak.product.dto;

import com.sacidpak.common.dto.BaseEntityDto;
import com.sacidpak.common.dto.LightEntityDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDto extends BaseEntityDto {

    private LightEntityDto product;

    private BigDecimal quantity;
}
