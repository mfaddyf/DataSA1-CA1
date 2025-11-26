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

    /**
     * smart add :
     * 1. try to add with an existing good item by matching description and weight
     *  - updates quantity, price, photo
     * 2. if no matches are found, place in the first aisle with a matching temperature
     *  - aisle has shelves, use the first
     *  - no shelves on aisle, make a shelf
     * @param newItem the new GoodItem to add or merge
     */
    public void smartAdd(GoodItem newItem) {
        // step 1 : search for an existing matching item
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
                        // matching description and weight
                        if (existing.getDescription().equalsIgnoreCase(newItem.getDescription()) &&
                                existing.getWeight().equalsIgnoreCase(newItem.getWeight())) {
                            // merge the quantities, update price, update photo
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

        // step 2 : no matches found so it will be placed in an aisle with matching temperature
        faNode = floorAreas.getHead();
        while (faNode != null) {
            FloorArea fa = faNode.data;
            Node<Aisle> aisleNode = fa.getAisles().getHead();
            while (aisleNode != null) {
                Aisle aisle = aisleNode.data;
                if (aisle.getAisleTemperature().equalsIgnoreCase(newItem.getTemperature())) {
                    // use the first shelf if it's available, if not, create a new one
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

    // clears all floors and reset the supermarket
    public void reset() {
        floorAreas.clear();
    }

    /**
     * saves the supermarket object to a file using serialization
     * @param filename the name of the file to save to
     */    public void saveToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads a supermarket object from a file
     * @param filename the name of the file to load from
     * @return the deserialised object or a new empty supermarket if loading fails
     */
    public static Supermarket loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (Supermarket) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Supermarket();
        }
    }

    /**
     * generate a full breakdown of all the stock in the supermarket
     * organised by floor area, aisle, shelf, item, all including totals and extra information
     * @return a built-up string containing the information about the supermarket
     */
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

            // count aisles in the floor area
            int aisleCount = 0;
            Node<Aisle> aisleNode = fa.getAisles().getHead();
            while (aisleNode != null) {
                aisleCount++;
                aisleNode = aisleNode.next;
            }

            // builds the string for floor area, \n skips to the next line for easier reading
            output = output + "Floor Area '" + fa.getFloorTitle() + "': "
                    + aisleCount + " Aisles, Total Value €"
                    + String.format("%.2f", floorTotal) + "\n";

            // loops through aisles
            aisleNode = fa.getAisles().getHead();
            while (aisleNode != null) {
                Aisle aisle = aisleNode.data;
                double aisleTotal = aisle.getTotalValue();

                // count shelves in the aisle
                int shelfCount = 0;
                Node<Shelf> shelfNode = aisle.getShelves().getHead();
                while (shelfNode != null) {
                    shelfCount++;
                    shelfNode = shelfNode.next;
                }

                // builds the string for the aisle
                output = output + "   Aisle '" + aisle.getAisleName() + "': "
                        + shelfCount + " Shelves, Total Value €"
                        + String.format("%.2f", aisleTotal) + "\n";

                // loops through shelves
                shelfNode = aisle.getShelves().getHead();
                while (shelfNode != null) {
                    Shelf shelf = shelfNode.data;
                    double shelfTotal = shelf.getTotalValue();

                    // count items in the shelf
                    int itemCount = 0;
                    Node<GoodItem> itemNode = shelf.getGoodItems().getHead();
                    while (itemNode != null) {
                        itemCount++;
                        itemNode = itemNode.next;
                    }

                    // builds the string for the aisle
                    output = output + "     Shelf " + shelf.getShelfNumber() + ": "
                            + itemCount + " items, Total Value €"
                            + String.format("%.2f", shelfTotal) + "\n";

                    // loops through the items
                    itemNode = shelf.getGoodItems().getHead();
                    while (itemNode != null) {
                        GoodItem item = itemNode.data;
                        double itemTotal = item.getTotalValue();

                        // builds the string for the items
                        output = output + "       " + item.getDescription()
                                + " (" + item.getWeight() + "G): "
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

        // final supermarket total displayed beneath everything
        output = output + "\nSupermarket: " + floorCount
                + " Floor Areas, Total Value €"
                + String.format("%.2f", supermarketTotal) + "\n";

        return output;
    }


    /**
     * searches for items using just their name, it is case-insensitive
     * @param name name of the item to search for
     * @return items with matching names are returned with their location and details
     */
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
                        if (item.getDescription().toLowerCase().contains(name.toLowerCase())) {
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
