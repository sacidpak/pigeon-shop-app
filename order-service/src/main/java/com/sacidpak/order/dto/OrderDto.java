package com.sacidpak.order.dto;

import com.sacidpak.common.dto.BaseEntityDto;
import com.sacidpak.order.enums.OrderStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto extends BaseEntityDto {

    private String orderNumber;

    private OrderStatus status;

    private String orderNote;

    private String customerName;

    private String customerPhoneNumber;

    private String customerAddressCode;

    private List<OrderItemDto> orderItems;
}
