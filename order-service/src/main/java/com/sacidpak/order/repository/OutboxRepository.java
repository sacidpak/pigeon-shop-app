package com.sacidpak.order.repository;

import com.sacidpak.common.repository.BaseRepository;
import com.sacidpak.order.domain.Outbox;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutboxRepository extends BaseRepository<Outbox, Long> {

    @Query("""
                select o from Outbox o
                where o.eventType = com.sacidpak.order.enums.OutboxEventType.INVENTORY
                and o.processed = false
            """)
    List<Outbox> findFailedInventoryMessage();
}
