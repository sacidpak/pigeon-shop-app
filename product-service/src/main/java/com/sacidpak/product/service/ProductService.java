package com.sacidpak.product.service;

import com.sacidpak.clients.product.ProductInfoRequest;
import com.sacidpak.clients.product.ProductInfoResponse;
import com.sacidpak.common.exception.BusinessException;
import com.sacidpak.common.service.BaseService;
import com.sacidpak.product.domain.Product;
import com.sacidpak.product.dto.ProductDto;
import com.sacidpak.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sacidpak.product.enums.ProductValidationType.DISCOUNT_CAN_NOT_BE_GRATER_THAN_PRICE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService extends BaseService<Product, ProductDto, Long> {

    private final ProductRepository productRepository;

    @Override
    protected void validateSave(ProductDto productDto) {
        checkDiscount(productDto);
    }

    @Override
    protected void validateUpdate(ProductDto productDto) {
        checkDiscount(productDto);
    }

    private void checkDiscount(ProductDto productDto) {
        var discount = productDto.getDiscount();
        var price = productDto.getPrice();
        if(discount != null && discount.compareTo(price) >= 0){
            throw new BusinessException(DISCOUNT_CAN_NOT_BE_GRATER_THAN_PRICE);
        }
    }

    public List<ProductInfoResponse> getProductInfo(ProductInfoRequest request) {
        return productRepository.findByBarcode(request.getBarcodes());
    }
}
