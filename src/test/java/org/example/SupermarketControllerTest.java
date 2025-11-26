package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SupermarketControllerTest {

    private Supermarket supermarket;

    @BeforeEach
    void setUp() {
        supermarket = new Supermarket();
    }

    @Test
    public void testSmartAddMergesExistingItem() {
        Supermarket market = new Supermarket();
        GoodItem original = new GoodItem("Tea", "500g", 3.99, 10, "Unrefrigerated", "tea.jpg");

        Shelf shelf = new Shelf(1);
        shelf.addGoodItem(original);

        Aisle aisle = new Aisle("A1", 2, 3, 100, 50, "Unrefrigerated");
        aisle.addShelf(shelf);

        FloorArea floor = new FloorArea("Main", "Ground");
        floor.addAisle(aisle);

        market.addFloorArea(floor);

        GoodItem duplicate = new GoodItem("Tea", "500g", 4.29, 5, "Unrefrigerated", "newtea.jpg");
        market.smartAdd(duplicate);

        GoodItem result = shelf.getGoodItems().getHead().data;
        assertEquals(15, result.getQuantity());
        assertEquals(4.29, result.getUnitPrice());
        assertEquals("newtea.jpg", result.getPhotoUrl());
    }

    @Test
    public void testSmartAddPlacesInMatchingTemperatureAisle() {
        Supermarket market = new Supermarket();

        Aisle aisle = new Aisle("FrozenAisle", 2, 3, 100, 50, "Frozen");
        FloorArea floor = new FloorArea("ColdStorage", "Basement");
        floor.addAisle(aisle);
        market.addFloorArea(floor);

        GoodItem fish = new GoodItem("Fish Fingers", "500g", 4.99, 10, "Frozen", "fish.jpg");
        market.smartAdd(fish);

        GoodItem result = aisle.getShelves().getHead().data.getGoodItems().getHead().data;
        assertEquals("Fish Fingers", result.getDescription());
    }

    @Test
    public void testSmartAddCreatesShelfIfNoneExist() {
        Supermarket market = new Supermarket();

        Aisle aisle = new Aisle("FrozenAisle", 2, 3, 100, 50, "Frozen");
        FloorArea floor = new FloorArea("ColdStorage", "Basement");
        floor.addAisle(aisle);
        market.addFloorArea(floor);

        assertNull(aisle.getShelves().getHead()); // no shelves yet

        GoodItem iceCream = new GoodItem("Ice Cream", "1L", 5.99, 3, "Frozen", "ice.jpg");
        market.smartAdd(iceCream);

        assertNotNull(aisle.getShelves().getHead());
        GoodItem result = aisle.getShelves().getHead().data.getGoodItems().getHead().data;
        assertEquals("Ice Cream", result.getDescription());
    }

    @Test
    public void testSmartAddFailsIfNoMatchingTemperature() {
        Supermarket market = new Supermarket();

        FloorArea floor = new FloorArea("Dry", "Ground");
        floor.addAisle(new Aisle("DryAisle", 2, 3, 100, 50, "Unrefrigerated"));
        market.addFloorArea(floor);

        GoodItem yogurt = new GoodItem("Yogurt", "200g", 1.99, 5, "Refrigerated", "yogurt.jpg");
        market.smartAdd(yogurt);

        // traverse all floors/aisles/shelves/items to check if yogurt was placed
        boolean found = false;
        Node<FloorArea> faNode = market.getFloorAreas().getHead();
        while (faNode != null) {
            FloorArea fa = faNode.data;
            Node<Aisle> aisleNode = fa.getAisles().getHead();
            while (aisleNode != null) {
                Aisle a = aisleNode.data;
                Node<Shelf> shelfNode = a.getShelves().getHead();
                while (shelfNode != null) {
                    Shelf s = shelfNode.data;
                    Node<GoodItem> itemNode = s.getGoodItems().getHead();
                    while (itemNode != null) {
                        if (itemNode.data.getDescription().equals("Yogurt")) {
                            found = true;
                        }
                        itemNode = itemNode.next;
                    }
                    shelfNode = shelfNode.next;
                }
                aisleNode = aisleNode.next;
            }
            faNode = faNode.next;
        }
        assertFalse(found);
    }

    @Test
    public void testSaveAndLoadSupermarket() {
        Supermarket original = new Supermarket();

        FloorArea floor = new FloorArea("Main Floor", "Ground");
        Aisle aisle = new Aisle("Frozen Aisle", 2, 3, 100, 50, "Frozen");
        Shelf shelf = new Shelf(1);
        GoodItem item = new GoodItem("Fish Fingers", "500g", 4.99, 10, "Frozen", "fish.jpg");

        shelf.addGoodItem(item);
        aisle.addShelf(shelf);
        floor.addAisle(aisle);
        original.addFloorArea(floor);

        String filename = "test_supermarket.dat";
        original.saveToFile(filename);

        Supermarket loaded = Supermarket.loadFromFile(filename);

        FloorArea loadedFloor = loaded.getFloorAreas().getHead().data;
        Aisle loadedAisle = loadedFloor.getAisles().getHead().data;
        Shelf loadedShelf = loadedAisle.getShelves().getHead().data;
        GoodItem loadedItem = loadedShelf.getGoodItems().getHead().data;

        assertEquals("Fish Fingers", loadedItem.getDescription());
        assertEquals("500g", loadedItem.getWeight());
        assertEquals(4.99, loadedItem.getUnitPrice());
        assertEquals(10, loadedItem.getQuantity());
        assertEquals("Frozen", loadedItem.getTemperature());
    }

    @Test
    void testAddFloorArea() {
        FloorArea fa = new FloorArea("Ground Floor", "Level 0");
        supermarket.addFloorArea(fa);

        assertNotNull(supermarket.getFloorAreas().getHead());
        assertEquals("Ground Floor", supermarket.getFloorAreas().getHead().data.getFloorTitle());
    }

    @Test
    void testAddAisleToFloorArea() {
        FloorArea fa = new FloorArea("Ground Floor", "Level 0");
        supermarket.addFloorArea(fa);

        Aisle aisle = new Aisle("Fruit", 10, 20, 50, 100, "Unrefrigerated");
        fa.addAisle(aisle);

        assertEquals("Fruit", fa.getAisles().getHead().data.getAisleName());
    }

    @Test
    void testAddShelfToAisle() {
        Aisle aisle = new Aisle("Fruit", 10, 20, 50, 100, "Unrefrigerated");
        aisle.addShelf(new Shelf(1));

        assertEquals(1, aisle.getShelves().getHead().data.getShelfNumber());
    }

    @Test
    void testAddGoodItemToShelf() {
        Shelf shelf = new Shelf(1);
        GoodItem item = new GoodItem("Apple", "200g", 0.50, 10, "Unrefrigerated", "apple.jpg");
        shelf.addGoodItem(item);

        assertEquals("Apple", shelf.getGoodItems().getHead().data.getDescription());
        assertEquals(10, shelf.getGoodItems().getHead().data.getQuantity());
    }

    @Test
    void testRemoveGoodItemQuantity() {
        Shelf shelf = new Shelf(1);
        GoodItem item = new GoodItem("Apple", "200g", 0.50, 10, "Unrefrigerated", "apple.jpg");
        shelf.addGoodItem(item);

        shelf.removeGoodItem("Apple", 5);

        assertEquals(5, shelf.getGoodItems().getHead().data.getQuantity());
    }

    @Test
    void testRemoveGoodItemCompletely() {
        Shelf shelf = new Shelf(1);
        GoodItem item = new GoodItem("Apple", "200g", 0.50, 5, "Unrefrigerated", "apple.jpg");
        shelf.addGoodItem(item);

        shelf.removeGoodItem("Apple", 5);

        assertNull(shelf.getGoodItems().getHead()); // list should be empty
    }
}
