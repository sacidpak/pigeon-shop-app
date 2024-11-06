package com.sacidpak.product.repository;

import com.sacidpak.common.repository.BaseRepository;
import com.sacidpak.product.domain.Inventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryRepository extends BaseRepository<Inventory, Long> {

    @Query("""
                    select i
                    from Inventory i join i.product p
                    where p.barcode in (:barcodes)
                        and i.deleted = false
                        and p.deleted = false
            """)
    List<Inventory> findByBarcodes(@Param("barcodes") List<String> barcodes);
}
