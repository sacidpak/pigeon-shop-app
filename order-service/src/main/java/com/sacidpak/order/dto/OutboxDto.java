package com.sacidpak.order.dto;

import com.sacidpak.common.dto.BaseEntityDto;
import com.sacidpak.order.enums.OutboxEventType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutboxDto extends BaseEntityDto {

    private OutboxEventType eventType;

    private String payload;

    private Boolean processed;
}
