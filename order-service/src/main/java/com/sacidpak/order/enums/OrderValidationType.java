package com.sacidpak.order.enums;

import com.sacidpak.common.exception.BusinessValidation;
import org.apache.commons.lang.StringUtils;

public enum OrderValidationType implements BusinessValidation {
    ORDER_NOT_FOUND(""),
    PRODUCT_NOT_FOUND(""),
    PRODUCT_SERVICE_NOT_AVAILABLE(""),;

    private String code;
    private String description;

    OrderValidationType(String description) {
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
