package com.sacidpak.order.repository;

import com.sacidpak.common.repository.BaseRepository;
import com.sacidpak.order.domain.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends BaseRepository<OrderItem, Long> {

    @Query("""
                select oi
                from OrderItem oi join oi.order o
                where o.orderNumber = :orderNumber
                    and o.deleted = false
                    and oi.deleted = false
            """)
    List<OrderItem> findAllByOrderNumber(@Param("orderNumber") String orderNumber);
}
