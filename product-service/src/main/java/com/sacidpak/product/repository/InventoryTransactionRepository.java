package com.sacidpak.product.repository;

import com.sacidpak.common.repository.BaseRepository;
import com.sacidpak.product.domain.InventoryTransaction;
import org.springframework.stereotype.Repository;

public interface InventoryTransactionRepository extends BaseRepository<InventoryTransaction,Long> {
}
