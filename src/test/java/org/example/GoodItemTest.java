package org.example;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GoodItemTest {

    @Test
    public void testGetTotalValue() {
        GoodItem item = new GoodItem("Milk", "1L", 2.5, 4, "Cold", "milk.jpg");
        assertEquals(10.0, item.getTotalValue(), 0.01);
    }

    @Test
    public void testMatchesTrue() {
        GoodItem a = new GoodItem("Bread", "500g", 1.2, 2, "Ambient", "bread.jpg");
        GoodItem b = new GoodItem("bread", "500G", 1.5, 1, "Ambient", "bread2.jpg");
        assertTrue(a.matches(b));
    }

    @Test
    public void testMatchesFalse() {
        GoodItem a = new GoodItem("Bread", "500g", 1.2, 2, "Ambient", "bread.jpg");
        GoodItem b = new GoodItem("Bread", "1kg", 1.5, 1, "Ambient", "bread2.jpg");
        assertFalse(a.matches(b));
    }

    @Test
    public void testToStringFormat() {
        GoodItem item = new GoodItem("Juice", "1L", 3.0, 2, "Cold", "juice.jpg");
        assertEquals("Juice x2 @ €3.0", item.toString());
    }
}
