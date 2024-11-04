package com.sacidpak.product.enums;

import com.sacidpak.common.exception.BusinessValidation;
import org.apache.commons.lang.StringUtils;

public enum ProductValidationType implements BusinessValidation {
    PRODUCT_NOT_FOUND(""),
    DISCOUNT_CAN_NOT_BE_GRATER_THAN_PRICE("");

    private String code;
    private String description;

    ProductValidationType(String description) {
        this.description = description;
    }

    @Override
    public String getCode() {
        return this.name();
    }

    @Override
    public String getDescription() {
        return StringUtils.isEmpty(description) ? this.name() : description;
    }
}
