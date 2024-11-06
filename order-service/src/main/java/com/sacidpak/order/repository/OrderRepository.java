package com.sacidpak.order.repository;

import com.sacidpak.common.repository.BaseRepository;
import com.sacidpak.order.domain.Order;
import com.sacidpak.order.enums.OrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends BaseRepository<Order, Long> {

    @Query("""
                select o from Order o
                where o.orderNumber = :orderNumber
                and o.deleted = false
            """)
    Optional<Order> findByOrderNumber(@Param("orderNumber") String orderNumber);

    @Query("""
                select o from Order o
                where o.orderNumber = :orderNumber
                and o.status in ( :statusList )
                and o.deleted = false
            """)
    Optional<Order> findByOrderNumberAndStatuses(@Param("orderNumber") String orderNumber,
                                                 @Param("statusList") List<OrderStatus> orderStatusList);
}
