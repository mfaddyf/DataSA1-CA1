package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FloorAreaTest {

    @Test
    public void testAddAisleAndTotalValue() {
        FloorArea floor = new FloorArea("Ground", "G");
        Aisle aisle1 = new Aisle("A1", 5, 10, 0, 0, "Ambient");
        Aisle aisle2 = new Aisle("A2", 5, 10, 0, 0, "Ambient");

        Shelf shelf1 = new Shelf(1);
        shelf1.addGoodItem(new GoodItem("Bread", "500g", 1.5, 4, "Ambient", "bread.jpg"));
        aisle1.addShelf(shelf1);

        Shelf shelf2 = new Shelf(2);
        shelf2.addGoodItem(new GoodItem("Cereal", "750g", 3.0, 2, "Ambient", "cereal.jpg"));
        aisle2.addShelf(shelf2);

        floor.addAisle(aisle1);
        floor.addAisle(aisle2);

        assertEquals(12.0, floor.getTotalValue(), 0.01);
    }
}
