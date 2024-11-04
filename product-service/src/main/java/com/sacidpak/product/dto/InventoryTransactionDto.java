package com.sacidpak.product.dto;

import com.sacidpak.common.dto.BaseEntityDto;
import com.sacidpak.common.dto.LightEntityDto;
import com.sacidpak.product.enums.TransactionStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryTransactionDto extends BaseEntityDto {

    private LightEntityDto product;

    private BigDecimal quantity;

    private BigDecimal transactionQuantity;

    private String orderNumber;

    private TransactionStatus status;
}
