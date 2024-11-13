package com.sacidpak.product.service;

import com.sacidpak.clients.product.InventoryOrderItemDto;
import com.sacidpak.clients.product.InventoryUpdateRequest;
import com.sacidpak.clients.product.InventoryUpdateResponse;
import com.sacidpak.common.service.BaseService;
import com.sacidpak.product.domain.Inventory;
import com.sacidpak.product.domain.InventoryTransaction;
import com.sacidpak.product.dto.InventoryDto;
import com.sacidpak.product.enums.TransactionStatus;
import com.sacidpak.product.repository.InventoryRepository;
import com.sacidpak.product.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService extends BaseService<Inventory, InventoryDto,Long> {

    private final InventoryRepository inventoryRepository;

    private final InventoryTransactionRepository inventoryTransactionRepository;

    @Transactional
    public InventoryUpdateResponse inventoryUpdate(InventoryUpdateRequest request) {
        var barcodes = request.getProducts().stream()
                .map(InventoryOrderItemDto::getBarcode)
                .toList();
        var inventories = inventoryRepository.findByBarcodes(barcodes);
        var failedItems = new ArrayList<InventoryOrderItemDto>();
        var status = request.getStatus();

        request.getProducts().forEach(product -> {
            var noneMatch = inventories.stream()
                    .noneMatch(f -> f.getProduct().getBarcode().equals(product.getBarcode())
                            && checkQuantityWithOrderStatus(f.getQuantity(), product.getQuantity(), status));
            if (noneMatch) {
                failedItems.add(product);
            }
        });

        if(!failedItems.isEmpty()){
            log.error(failedItems.toString());
            return new InventoryUpdateResponse(false, failedItems.toString());
        }

        updateInventoryAndSaveTransaction(inventories, request);

        return new InventoryUpdateResponse(true, null);
    }

    private Boolean checkQuantityWithOrderStatus(BigDecimal inventoryQuantity,
                                                 BigDecimal orderItemQuantity, String status){
        if(TransactionStatus.NEW_ORDER.name().equals(status)){
            return inventoryQuantity.compareTo(orderItemQuantity) >= 0;
        }

        return TransactionStatus.CANCELLED.name().equals(status);
    }

    public List<InventoryDto> getInventoriesByBarcodes(List<String> barcodes) {
        var inventories = inventoryRepository.findByBarcodes(barcodes);
        var mapper = new ModelMapper();
        return inventories.stream()
                .map(element -> mapper.map(element, InventoryDto.class))
                .collect(Collectors.toList());
    }

    private void updateInventoryAndSaveTransaction(List<Inventory> inventories, InventoryUpdateRequest inventoryUpdateRequest) {
        var status = inventoryUpdateRequest.getStatus();
        inventories.forEach(inventory -> {
            var quantityBeforeTransaction = inventory.getQuantity();
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
                                .orderNumber(inventoryUpdateRequest.getOrderNumber())
                                .quantityBeforeTransaction(quantityBeforeTransaction)
                                .quantity(quantityResult)
                                .status(transactionStatus)
                                .build();

                        inventoryTransactionRepository.save(transaction);
                        log.info("Updated inventory product {} with quantity {}", product, productItem.getQuantity());
                    });
        });
    }
}
