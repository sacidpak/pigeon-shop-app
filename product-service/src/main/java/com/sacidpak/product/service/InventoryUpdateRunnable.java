package com.sacidpak.product.service;

import com.sacidpak.clients.product.InventoryUpdateRequest;
import com.sacidpak.product.domain.Inventory;
import com.sacidpak.product.domain.InventoryTransaction;
import com.sacidpak.product.enums.TransactionStatus;
import com.sacidpak.product.repository.InventoryRepository;
import com.sacidpak.product.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryUpdateRunnable {

    private final InventoryRepository inventoryRepository;

    private final InventoryTransactionRepository inventoryTransactionRepository;

    @Transactional
    @Async("productTaskExecutor")
    public void updateInventory(List<Inventory> inventories, InventoryUpdateRequest inventoryUpdateRequest) {
        var status = inventoryUpdateRequest.getStatus();
        inventories.forEach(inventory -> {
            var product = inventory.getProduct();
            inventoryUpdateRequest.getProducts().stream()
                    .filter(f -> f.getBarcode().equals(inventory.getProduct().getBarcode()))
                    .findFirst()
                    .ifPresent(productItem -> {
                        BigDecimal quantityResult = null;
                        TransactionStatus transactionStatus = null;
                        if(TransactionStatus.NEW_ORDER.name().equals(status)){
                            quantityResult = inventory.getQuantity().subtract(productItem.getQuantity());
                            transactionStatus = TransactionStatus.NEW_ORDER;
                        } else if(TransactionStatus.CANCELLED.name().equals(status)){
                            quantityResult = inventory.getQuantity().add(productItem.getQuantity());
                            transactionStatus = TransactionStatus.CANCELLED;
                        }

                        inventory.setQuantity(quantityResult);
                        inventoryRepository.save(inventory);

                        var transaction = InventoryTransaction.builder()
                                .product(product)
                                .transactionQuantity(productItem.getQuantity())
                                .currentQuantity(inventory.getQuantity())
                                .quantity(quantityResult)
                                .status(transactionStatus)
                                .build();

                        inventoryTransactionRepository.save(transaction);
                        log.info("Updated inventory product {} with quantity {}", product, productItem.getQuantity());
                    });
        });
    }
}
