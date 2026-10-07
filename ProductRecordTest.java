package com.bizengine;

import com.bizengine.domain.*;
import com.bizengine.exception.InvalidBusinessDataException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductRecordTest {
    @Test
    void negativeStockRejected() {
        assertThrows(InvalidBusinessDataException.class, () -> new Product("P1", "X", BigDecimal.ONE, -1));
    }

    @Test
    void zeroPriceRejected() {
        assertThrows(InvalidBusinessDataException.class, () -> new Product("P1", "X", BigDecimal.ZERO, 1));
    }

    @Test
    void withStockReturnsNewInstance() {
        Product p = new Product("P1", "X", BigDecimal.ONE, 5);
        Product q = p.withStock(2);
        assertEquals(5, p.stock());
        assertEquals(2, q.stock());
    }

    @Test
    void recordsHaveValueEquality() {
        assertEquals(new Product("P1", "X", BigDecimal.ONE, 5), new Product("P1", "X", BigDecimal.ONE, 5));
    }

    @Test
    void orderLinesAreDefensivelyCopied() {
        List<OrderLine> lines = new ArrayList<>(List.of(new OrderLine("P1", 1, BigDecimal.TEN)));
        Order o = new Order("O1", "Acme", lines);
        lines.add(new OrderLine("P2", 1, BigDecimal.TEN));
        assertEquals(1, o.lines().size());
        assertThrows(UnsupportedOperationException.class, () -> o.lines().clear());
    }

    @Test
    void emptyOrderRejected() {
        assertThrows(InvalidBusinessDataException.class, () -> new Order("O1", "Acme", List.of()));
    }
}
