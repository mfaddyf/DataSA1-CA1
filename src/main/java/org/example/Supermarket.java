package org.example;

import java.io.*;

public class Supermarket implements Serializable {
    private MLinkedList<FloorArea> floorAreas;

    public Supermarket() {
        this.floorAreas = new MLinkedList<>();
    }

    // Add a new floor area
    public void addFloorArea(FloorArea floorArea) {
        floorAreas.addElement(floorArea);
    }

    public MLinkedList<FloorArea> getFloorAreas() {
        return floorAreas;
    }

    // smart add: merge if match, else place in best aisle by temperature
    public void smartAdd(GoodItem newItem) {
        Node<FloorArea> faNode = floorAreas.getHead();
        while (faNode != null) {
            FloorArea fa = faNode.data;
            Node<Aisle> aisleNode = fa.getAisles().getHead();
            while (aisleNode != null) {
                Aisle aisle = aisleNode.data;
                Node<Shelf> shelfNode = aisle.getShelves().getHead();
                while (shelfNode != null) {
                    Shelf shelf = shelfNode.data;
                    Node<GoodItem> itemNode = shelf.getGoodItems().getHead();
                    while (itemNode != null) {
                        GoodItem existing = itemNode.data;
                        if (existing.getDescription().equalsIgnoreCase(newItem.getDescription()) &&
                                existing.getWeight().equalsIgnoreCase(newItem.getWeight())) {
                            existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
                            existing.setUnitPrice(newItem.getUnitPrice());
                            existing.setPhotoUrl(newItem.getPhotoUrl());
                            return;
                        }
                        itemNode = itemNode.next;
                    }
                    shelfNode = shelfNode.next;
                }
                aisleNode = aisleNode.next;
            }
            faNode = faNode.next;
        }

        // no match found — place in first aisle with matching temperature
        faNode = floorAreas.getHead();
        while (faNode != null) {
            FloorArea fa = faNode.data;
            Node<Aisle> aisleNode = fa.getAisles().getHead();
            while (aisleNode != null) {
                Aisle aisle = aisleNode.data;
                if (aisle.getAisleTemperature().equalsIgnoreCase(newItem.getTemperature())) {
                    Shelf targetShelf;
                    if (aisle.getShelves().getHead() != null) {
                        targetShelf = aisle.getShelves().getHead().data;
                    } else {
                        targetShelf = new Shelf(1);
                        aisle.addShelf(targetShelf);
                    }
                    targetShelf.addGoodItem(newItem);
                    return;
                }
                aisleNode = aisleNode.next;
            }
            faNode = faNode.next;
        }
    }


    // Reset all data
    public void reset() {
        floorAreas.clear();
    }

    // Save supermarket to file
    public void saveToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load supermarket from file
    public static Supermarket loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (Supermarket) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Supermarket(); // fallback
        }
    }

    public String viewAllStockBreakdown() {
        String output = "";
        double supermarketTotal = 0.0;

        Node<FloorArea> faNode = floorAreas.getHead();
        int floorCount = 0;
        while (faNode != null) {
            FloorArea fa = faNode.data;
            double floorTotal = fa.getTotalValue();
            supermarketTotal += floorTotal;
            floorCount++;

            // count aisles
            int aisleCount = 0;
            Node<Aisle> aisleNode = fa.getAisles().getHead();
            while (aisleNode != null) {
                aisleCount++;
                aisleNode = aisleNode.next;
            }

            output = output + "Floor Area '" + fa.getFloorTitle() + "': "
                    + aisleCount + " Aisles, Total Value €"
                    + String.format("%.2f", floorTotal) + "\n";

            aisleNode = fa.getAisles().getHead();
            while (aisleNode != null) {
                Aisle aisle = aisleNode.data;
                double aisleTotal = aisle.getTotalValue();

                // count shelves
                int shelfCount = 0;
                Node<Shelf> shelfNode = aisle.getShelves().getHead();
                while (shelfNode != null) {
                    shelfCount++;
                    shelfNode = shelfNode.next;
                }

                output = output + "  Aisle '" + aisle.getAisleName() + "': "
                        + shelfCount + " Shelves, Total Value €"
                        + String.format("%.2f", aisleTotal) + "\n";

                shelfNode = aisle.getShelves().getHead();
                while (shelfNode != null) {
                    Shelf shelf = shelfNode.data;
                    double shelfTotal = shelf.getTotalValue();

                    // count items
                    int itemCount = 0;
                    Node<GoodItem> itemNode = shelf.getGoodItems().getHead();
                    while (itemNode != null) {
                        itemCount++;
                        itemNode = itemNode.next;
                    }

                    output = output + "    Shelf " + shelf.getShelfNumber() + ": "
                            + itemCount + " items, Total Value €"
                            + String.format("%.2f", shelfTotal) + "\n";

                    itemNode = shelf.getGoodItems().getHead();
                    while (itemNode != null) {
                        GoodItem item = itemNode.data;
                        double itemTotal = item.getTotalValue();

                        output = output + "      " + item.getDescription()
                                + " (" + item.getWeight() + "): "
                                + item.getQuantity() + " @ €"
                                + String.format("%.2f", item.getUnitPrice())
                                + " = €" + String.format("%.2f", itemTotal)
                                + "\n";

                        itemNode = itemNode.next;
                    }
                    shelfNode = shelfNode.next;
                }
                aisleNode = aisleNode.next;
            }
            faNode = faNode.next;
        }

        output = output + "\nSupermarket: " + floorCount
                + " Floor Areas, Total Value €"
                + String.format("%.2f", supermarketTotal) + "\n";

        return output;
    }



    public MLinkedList<SearchResult> searchGoodItemByName(String name) {
        MLinkedList<SearchResult> results = new MLinkedList<>();

        Node<FloorArea> faNode = floorAreas.getHead();
        while (faNode != null) {
            FloorArea fa = faNode.data;
            Node<Aisle> aisleNode = fa.getAisles().getHead();
            while (aisleNode != null) {
                Aisle aisle = aisleNode.data;
                Node<Shelf> shelfNode = aisle.getShelves().getHead();
                while (shelfNode != null) {
                    Shelf shelf = shelfNode.data;
                    Node<GoodItem> itemNode = shelf.getGoodItems().getHead();
                    while (itemNode != null) {
                        GoodItem item = itemNode.data;
                        if (item.getDescription().equalsIgnoreCase(name)) {
                            results.addElement(new SearchResult(
                                    fa.getFloorTitle(), fa.getFloorLevel(),
                                    aisle.getAisleName(), shelf.getShelfNumber(),
                                    item
                            ));
                        }
                        itemNode = itemNode.next;
                    }
                    shelfNode = shelfNode.next;
                }
                aisleNode = aisleNode.next;
            }
            faNode = faNode.next;
        }
        return results;
    }

}
