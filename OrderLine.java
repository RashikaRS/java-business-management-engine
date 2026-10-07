package com.bizengine.domain;

import com.bizengine.exception.InvalidBusinessDataException;

import java.math.BigDecimal;

public record OrderLine(String sku, int quantity, BigDecimal unitPrice) {
    public OrderLine {
        if (quantity <= 0) throw new InvalidBusinessDataException("Quantity must be positive");
        if (unitPrice == null || unitPrice.signum() <= 0) throw new InvalidBusinessDataException("Unit price must be positive");
    }

    public BigDecimal total() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
