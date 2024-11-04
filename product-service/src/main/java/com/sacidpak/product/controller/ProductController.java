package com.sacidpak.product.controller;

import com.sacidpak.clients.product.ProductInfoRequest;
import com.sacidpak.clients.product.ProductInfoResponse;
import com.sacidpak.common.controller.BaseController;
import com.sacidpak.product.dto.ProductDto;
import com.sacidpak.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
public class ProductController extends BaseController<ProductService, ProductDto> {

    @PostMapping("/info")
    public ResponseEntity<List<ProductInfoResponse>> productInfo(@RequestBody @Valid ProductInfoRequest request) {
        var result = entityService.getProductInfo(request);
        return ResponseEntity.ok(result);
    }
}
