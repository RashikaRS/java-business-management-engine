package com.bizengine.domain;

import com.bizengine.exception.InvalidBusinessDataException;

import java.math.BigDecimal;

/** Immutable value object (record) with validation in the compact constructor. */
public record Product(String sku, String name, BigDecimal price, int stock) {
    public Product {
        if (sku == null || sku.isBlank()) throw new InvalidBusinessDataException("SKU is required");
        if (name == null || name.isBlank()) throw new InvalidBusinessDataException("Product name is required");
        if (price == null || price.signum() <= 0) throw new InvalidBusinessDataException("Price must be positive");
        if (stock < 0) throw new InvalidBusinessDataException("Stock cannot be negative");
    }

    public Product withStock(int newStock) {
        return new Product(sku, name, price, newStock);
    }
}
