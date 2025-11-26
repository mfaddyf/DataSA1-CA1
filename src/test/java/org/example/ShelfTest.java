package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ShelfTest {

    @Test
    public void testAddGoodItem_NewItem() {
        Shelf shelf = new Shelf(1);
        GoodItem item = new GoodItem("Apple", "200g", 0.5, 10, "Ambient", "apple.jpg");
        shelf.addGoodItem(item);
        assertEquals(1, shelf.getGoodItems().size());
        assertEquals(5.0, shelf.getTotalValue(), 0.01);
    }

    @Test
    public void testAddGoodItem_MergeItem() {
        Shelf shelf = new Shelf(1);
        GoodItem item1 = new GoodItem("Apple", "200g", 0.5, 10, "Ambient", "apple.jpg");
        GoodItem item2 = new GoodItem("apple", "200G", 0.6, 5, "Ambient", "apple2.jpg");
        shelf.addGoodItem(item1);
        shelf.addGoodItem(item2);
        assertEquals(1, shelf.getGoodItems().size());
        GoodItem merged = shelf.getGoodItems().get(0);
        assertEquals(15, merged.getQuantity());
        assertEquals(0.6, merged.getUnitPrice(), 0.01);
        assertEquals("apple2.jpg", merged.getPhotoUrl());
    }

    @Test
    public void testRemoveGoodItem_Partial() {
        Shelf shelf = new Shelf(1);
        GoodItem item = new GoodItem("Banana", "1kg", 1.0, 10, "Ambient", "banana.jpg");
        shelf.addGoodItem(item);
        shelf.removeGoodItem("banana", 4);
        assertEquals(6, shelf.getGoodItems().get(0).getQuantity());
    }

    @Test
    public void testRemoveGoodItem_Full() {
        Shelf shelf = new Shelf(1);
        GoodItem item = new GoodItem("Banana", "1kg", 1.0, 10, "Ambient", "banana.jpg");
        shelf.addGoodItem(item);
        shelf.removeGoodItem("banana", 10);
        assertEquals(0, shelf.getGoodItems().size());
    }
}
