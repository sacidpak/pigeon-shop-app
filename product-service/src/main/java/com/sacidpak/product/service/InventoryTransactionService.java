package com.sacidpak.product.service;

import com.sacidpak.common.service.BaseService;
import com.sacidpak.product.domain.InventoryTransaction;
import com.sacidpak.product.dto.InventoryTransactionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryTransactionService extends BaseService<InventoryTransaction, InventoryTransactionDto,Long> {
}
