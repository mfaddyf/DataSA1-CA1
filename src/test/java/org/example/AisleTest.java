package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AisleTest {

    @Test
    public void testAddShelfAndTotalValue() {
        Aisle aisle = new Aisle("A1", 5, 10, 0, 0, "Cold");
        Shelf shelf1 = new Shelf(1);
        Shelf shelf2 = new Shelf(2);
        shelf1.addGoodItem(new GoodItem("Yogurt", "150g", 1.0, 5, "Cold", "yogurt.jpg"));
        shelf2.addGoodItem(new GoodItem("Milk", "1L", 2.0, 3, "Cold", "milk.jpg"));
        aisle.addShelf(shelf1);
        aisle.addShelf(shelf2);
        assertEquals(11.0, aisle.getTotalValue(), 0.01);
    }
}
