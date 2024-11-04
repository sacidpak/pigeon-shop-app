package com.sacidpak.product.repository;

import com.sacidpak.clients.product.ProductInfoResponse;
import com.sacidpak.common.repository.BaseRepository;
import com.sacidpak.product.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends BaseRepository<Product,Long> {

    @Query("""
            select new com.sacidpak.clients.product.ProductInfoResponse(
            p.barcode,
            p.name,
            p.price,
            p.discount,
            p.quantityType,
            i.quantity)
            from Product p
                join Inventory i on i.product.id = p.id
            where p.barcode in (:barcodes)
            and i.deleted = false
            and p.deleted = false
            """)
    List<ProductInfoResponse> findByBarcode(@Param("barcodes") List<String> barcodes);
}
