package com.sacidpak.order.dto;

import com.sacidpak.common.dto.BaseEntityDto;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto extends BaseEntityDto {

    private String email;

    private String phoneNumber;

    private String fullName;

    private String addressCode;
}
