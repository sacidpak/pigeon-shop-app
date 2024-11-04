package com.sacidpak.order.dto;

import com.sacidpak.common.dto.BaseEntityDto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto  extends BaseEntityDto {

    private String code;

    private String name;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String district;

    private String neighborhood;

    private String street;

    private String buildingNumber;

    private String flatNumber;

    private String floorNumber;

    private String city;

    private String directions;
}
