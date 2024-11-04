package com.sacidpak.clients.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoRequest {

    private List<String> barcodes;
}
