package com.sacidpak.product.controller;

import com.sacidpak.clients.product.InventoryUpdateRequest;
import com.sacidpak.clients.product.InventoryUpdateResponse;
import com.sacidpak.common.controller.BaseController;
import com.sacidpak.product.dto.InventoryDto;
import com.sacidpak.product.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/inventory")
public class InventoryController extends BaseController<InventoryService, InventoryDto> {

    @PostMapping("/update")
    @Operation(summary = "Updates inventories and returns failed/succeeded message")
    public ResponseEntity<InventoryUpdateResponse> inventoryUpdate(@RequestBody @Valid InventoryUpdateRequest request) {
        return ResponseEntity.ok(entityService.inventoryUpdate(request));
    }

    @PostMapping("/search-by-barcode")
    @Operation(summary = "Get inventory by product barcodes")
    public ResponseEntity<List<InventoryDto>> getInventoryByBarcodes(@RequestBody @NotEmpty List<String> barcode) {
        return ResponseEntity.ok(entityService.getInventoriesByBarcodes(barcode));
    }
}
