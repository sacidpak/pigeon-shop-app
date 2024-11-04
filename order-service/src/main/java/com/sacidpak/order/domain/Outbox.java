package com.sacidpak.order.domain;

import com.sacidpak.common.domain.BaseEntity;
import com.sacidpak.order.dto.OutboxDto;
import com.sacidpak.order.enums.OutboxEventType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Outbox extends BaseEntity<Outbox, OutboxDto> {

    @NotNull
    @Column(name = "event_type", nullable = false)
    private OutboxEventType eventType;

    @NotNull
    @Column(name = "payload", nullable = false)
    private String payload;

    @NotNull
    @Builder.Default
    @Column(name = "processed", nullable = false)
    private Boolean processed = Boolean.FALSE;
}
