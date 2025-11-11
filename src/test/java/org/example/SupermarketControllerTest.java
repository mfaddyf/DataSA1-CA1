package org.example;

// mainly just tests for smart add + saving/loading files

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SupermarketControllerTest {

    @Test
    public void testSmartAddMergesExistingItem() {
        Supermarket market = new Supermarket();
        GoodItem original = new GoodItem("Tea", "500g", 3.99, 10, "Unrefrigerated", "tea.jpg");

        Shelf shelf = new Shelf(1);
        shelf.addGoodItem(original);

        Aisle aisle = new Aisle("A1", 2, 3,100,50, "Unrefrigerated");
        aisle.addShelf(shelf);

        FloorArea floor = new FloorArea("Main", "Ground");
        floor.addAisle(aisle);

        market.addFloorArea(floor);

        GoodItem duplicate = new GoodItem("Tea", "500g", 4.29, 5, "Unrefrigerated", "newtea.jpg");
        market.smartAdd(duplicate);

        GoodItem result = shelf.getGoodItems().get(0);
        assertEquals(15, result.getQuantity());
        assertEquals(4.29, result.getUnitPrice());
        assertEquals("newtea.jpg", result.getPhotoUrl());
    }

    @Test
    public void testSmartAddPlacesInMatchingTemperatureAisle() {
        Supermarket market = new Supermarket();

        Aisle aisle = new Aisle("FrozenAisle", 2, 3, 100,50,"Frozen");
        FloorArea floor = new FloorArea("ColdStorage", "Basement");
        floor.addAisle(aisle);
        market.addFloorArea(floor);

        GoodItem fish = new GoodItem("Fish Fingers", "500g", 4.99, 10, "Frozen", "fish.jpg");
        market.smartAdd(fish);

        Shelf shelf = aisle.getShelves().get(0);
        GoodItem result = shelf.getGoodItems().get(0);
        assertEquals("Fish Fingers", result.getDescription());
    }

    @Test
    public void testSmartAddCreatesShelfIfNoneExist() {
        Supermarket market = new Supermarket();

        Aisle aisle = new Aisle("FrozenAisle", 2, 3, 100,50,"Frozen");
        FloorArea floor = new FloorArea("ColdStorage", "Basement");
        floor.addAisle(aisle);
        market.addFloorArea(floor);

        assertEquals(0, aisle.getShelves().size());

        GoodItem iceCream = new GoodItem("Ice Cream", "1L", 5.99, 3, "Frozen", "ice.jpg");
        market.smartAdd(iceCream);

        assertEquals(1, aisle.getShelves().size());
        assertEquals("Ice Cream", aisle.getShelves().get(0).getGoodItems().get(0).getDescription());
    }

    @Test
    public void testSmartAddFailsIfNoMatchingTemperature() {
        Supermarket market = new Supermarket();

        FloorArea floor = new FloorArea("Dry", "Ground");
        floor.addAisle(new Aisle("DryAisle",2, 3, 100,50,"Unrefrigerated"));
        market.addFloorArea(floor);

        GoodItem yogurt = new GoodItem("Yogurt", "200g", 1.99, 5, "Refrigerated", "yogurt.jpg");
        market.smartAdd(yogurt);

        // should not be placed
        boolean found = false;

        MLinkedList<FloorArea> floors = market.getFloorAreas();
        for (int i = 0; i < floors.size(); i++) {
            FloorArea fa = floors.get(i);
            MLinkedList<Aisle> aisles = fa.getAisles();
            for (int j = 0; j < aisles.size(); j++) {
                Aisle a = aisles.get(j);
                MLinkedList<Shelf> shelves = a.getShelves();
                for (int k = 0; k < shelves.size(); k++) {
                    Shelf s = shelves.get(k);
                    MLinkedList<GoodItem> items = s.getGoodItems();
                    for (int l = 0; l < items.size(); l++) {
                        GoodItem g = items.get(l);
                        if (g.getDescription().equals("Yogurt")) {
                            found = true;
                        }
                    }
                }
            }
        }
        assertFalse(found);
    }

    @Test
    public void testSaveAndLoadSupermarket() {
        // Create a supermarket and add a good item
        Supermarket original = new Supermarket();

        FloorArea floor = new FloorArea("Main Floor", "Ground");
        Aisle aisle = new Aisle("Frozen Aisle", 2, 3, 100,50,"Frozen");
        Shelf shelf = new Shelf(1);
        GoodItem item = new GoodItem("Fish Fingers", "500g", 4.99, 10, "Frozen", "fish.jpg");

        shelf.addGoodItem(item);
        aisle.addShelf(shelf);
        floor.addAisle(aisle);
        original.addFloorArea(floor);

        // Save to file
        String filename = "test_supermarket.dat";
        original.saveToFile(filename);

        // Load from file
        Supermarket loaded = Supermarket.loadFromFile(filename);

        // Verify structure and data
        FloorArea loadedFloor = loaded.getFloorAreas().get(0);
        Aisle loadedAisle = loadedFloor.getAisles().get(0);
        Shelf loadedShelf = loadedAisle.getShelves().get(0);
        GoodItem loadedItem = loadedShelf.getGoodItems().get(0);

        assertEquals("Fish Fingers", loadedItem.getDescription());
        assertEquals("500g", loadedItem.getWeight());
        assertEquals(4.99, loadedItem.getUnitPrice());
        assertEquals(10, loadedItem.getQuantity());
        assertEquals("Frozen", loadedItem.getTemperature());
    }
}