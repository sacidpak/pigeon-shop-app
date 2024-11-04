package com.sacidpak.order.data.request;

import com.sacidpak.order.dto.CustomerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {

    private String orderNote;

    @NotNull
    private CustomerDto customer;

    @Valid
    @NotEmpty
    private List<CreateOrderItemRequest> orderItems;
}
