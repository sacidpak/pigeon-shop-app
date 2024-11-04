package com.sacidpak.clients.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("product-service")
public interface ProductClient {

    @PostMapping("api/v1/product/info")
    ResponseEntity<List<ProductInfoResponse>> getProductInfo(@RequestBody ProductInfoRequest request);

    @PostMapping("api/v1/inventory/update")
    ResponseEntity<InventoryUpdateResponse> updateInventory(@RequestBody InventoryUpdateRequest request);
}
