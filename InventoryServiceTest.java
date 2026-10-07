package com.bizengine;

import com.bizengine.domain.Order;
import com.bizengine.domain.Product;
import com.bizengine.exception.*;
import com.bizengine.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {
    private InventoryService inv;

    @BeforeEach
    void setUp() throws Exception {
        inv = new InventoryService();
        inv.addProduct(new Product("P1", "Keyboard", new BigDecimal("1000"), 10));
        inv.addProduct(new Product("P2", "Mouse", new BigDecimal("500"), 2));
    }

    @Test
    void orderDeductsStockAndComputesTotal() throws Exception {
        Order o = inv.placeOrder("Acme", Map.of("P1", 3));
        assertEquals(7, inv.getProduct("P1").stock());
        assertEquals(new BigDecimal("3000"), o.total());
        assertEquals(1, inv.orders().size());
    }

    @Test
    void insufficientStockThrowsCheckedExceptionWithDetails() {
        InsufficientStockException ex = assertThrows(InsufficientStockException.class,
                () -> inv.placeOrder("Acme", Map.of("P2", 5)));
        assertEquals("P2", ex.getSku());
        assertEquals(5, ex.getRequested());
        assertEquals(2, ex.getAvailable());
    }

    @Test
    void failedOrderLeavesStockUntouched() {
        Map<String, Integer> items = new LinkedHashMap<>();
        items.put("P1", 4);   // valid
        items.put("P2", 99);  // fails
        assertThrows(InsufficientStockException.class, () -> inv.placeOrder("Acme", items));
        assertDoesNotThrow(() -> assertEquals(10, inv.getProduct("P1").stock()));
        assertTrue(inv.orders().isEmpty());
    }

    @Test
    void unknownSkuThrowsNotFound() {
        assertThrows(EntityNotFoundException.class, () -> inv.placeOrder("Acme", Map.of("ZZZ", 1)));
    }

    @Test
    void duplicateSkuRejected() {
        assertThrows(DuplicateEntityException.class,
                () -> inv.addProduct(new Product("P1", "Dup", BigDecimal.ONE, 1)));
    }

    @Test
    void invalidQuantityIsUncheckedException() {
        assertThrows(InvalidBusinessDataException.class, () -> inv.placeOrder("Acme", Map.of("P1", 0)));
        assertThrows(InvalidBusinessDataException.class, () -> inv.placeOrder("Acme", Map.of()));
    }

    @Test
    void lowStockFilterAndInventoryValue() {
        assertEquals(1, inv.lowStock(5).size());
        assertEquals("P2", inv.lowStock(5).get(0).sku());
        assertEquals(new BigDecimal("11000"), inv.inventoryValue()); // 10*1000 + 2*500
    }

    @Test
    void checkedExceptionsShareBusinessExceptionRoot() {
        assertTrue(BusinessException.class.isAssignableFrom(InsufficientStockException.class));
        assertFalse(RuntimeException.class.isAssignableFrom(BusinessException.class));
    }
}
