package com.sacidpak.order.dto;

import com.sacidpak.common.dto.BaseEntityDto;
import com.sacidpak.order.enums.OrderStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryDto extends BaseEntityDto {

    private OrderStatus status;
}
