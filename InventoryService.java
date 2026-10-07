package com.bizengine.service;

import com.bizengine.domain.Order;
import com.bizengine.domain.OrderLine;
import com.bizengine.domain.Product;
import com.bizengine.exception.DuplicateEntityException;
import com.bizengine.exception.EntityNotFoundException;
import com.bizengine.exception.InsufficientStockException;
import com.bizengine.exception.InvalidBusinessDataException;
import com.bizengine.repository.InMemoryRepository;
import com.bizengine.repository.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryService {
    private final Repository<Product, String> products = new InMemoryRepository<>(Product::sku);
    private final List<Order> orders = new ArrayList<>();
    private final AtomicInteger orderSeq = new AtomicInteger(1000);

    public void addProduct(Product p) throws DuplicateEntityException {
        products.add(p);
    }

    public Product getProduct(String sku) throws EntityNotFoundException {
        return products.getById(sku);
    }

    public List<Product> lowStock(int threshold) {
        return CollectionUtils.filter(products.findAll(), p -> p.stock() <= threshold);
    }

    public BigDecimal inventoryValue() {
        return products.findAll().stream()
                .map(p -> p.price().multiply(BigDecimal.valueOf(p.stock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Order> orders() {
        return List.copyOf(orders);
    }

    /**
     * All-or-nothing: every line is validated before any stock is deducted.
     * @param items sku -> quantity
     */
    public Order placeOrder(String customer, Map<String, Integer> items)
            throws EntityNotFoundException, InsufficientStockException {
        if (items == null || items.isEmpty()) throw new InvalidBusinessDataException("Order needs at least one item");

        Map<String, Product> resolved = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> e : items.entrySet()) {
            Product p = products.getById(e.getKey());
            int qty = e.getValue();
            if (qty <= 0) throw new InvalidBusinessDataException("Quantity must be positive for " + e.getKey());
            if (p.stock() < qty) throw new InsufficientStockException(p.sku(), qty, p.stock());
            resolved.put(p.sku(), p);
        }

        List<OrderLine> lines = new ArrayList<>();
        for (Map.Entry<String, Integer> e : items.entrySet()) {
            Product p = resolved.get(e.getKey());
            products.update(p.withStock(p.stock() - e.getValue()));
            lines.add(new OrderLine(p.sku(), e.getValue(), p.price()));
        }

        Order order = new Order("ORD-" + orderSeq.getAndIncrement(), customer, lines);
        orders.add(order);
        return order;
    }
}
