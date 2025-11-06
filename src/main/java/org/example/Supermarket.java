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

    public void setFloorAreas(MLinkedList<FloorArea> floorAreas) {
        this.floorAreas = floorAreas;
    }

    // check if an aisle name is unique across all floor areas
    public boolean isAisleNameUnique(String aisleName) {
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea fa = floorAreas.get(i);
            if (fa.aisleNameExists(aisleName)) {
                return false;
            }
        }
        return true;
    }

    // search for a GoodItem by description across all shelves
    public GoodItem searchGoodItem(String description) {
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea fa = floorAreas.get(i);
            for (int j = 0; j < fa.getAisles().size(); j++) {
                Aisle aisle = fa.getAisles().get(j);
                for (int k = 0; k < aisle.getShelves().size(); k++) {
                    Shelf shelf = aisle.getShelves().get(k);
                    for (int l = 0; l < shelf.getGoodItems().size(); l++) {
                        GoodItem item = shelf.getGoodItems().get(l);
                        if (item.getDescription().equalsIgnoreCase(description)) {
                            return item;
                        }
                    }
                }
            }
        }
        return null;
    }

    // smart add: merge if match, else place in best aisle by temperature
    public void smartAdd(GoodItem newItem) {
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea fa = floorAreas.get(i);
            for (int j = 0; j < fa.getAisles().size(); j++) {
                Aisle aisle = fa.getAisles().get(j);
                for (int k = 0; k < aisle.getShelves().size(); k++) {
                    Shelf shelf = aisle.getShelves().get(k);
                    for (int l = 0; l < shelf.getGoodItems().size(); l++) {
                        GoodItem existing = shelf.getGoodItems().get(l);
                        if (existing.getDescription().equalsIgnoreCase(newItem.getDescription()) &&
                                existing.getWeight().equalsIgnoreCase(newItem.getWeight())) {
                            existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
                            existing.setUnitPrice(newItem.getUnitPrice());
                            existing.setPhotoUrl(newItem.getPhotoUrl());
                            return;
                        }
                    }
                }
            }
        }

        // no match found — place in first aisle with matching temperature
        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea fa = floorAreas.get(i);
            for (int j = 0; j < fa.getAisles().size(); j++) {
                Aisle aisle = fa.getAisles().get(j);
                if (aisle.getAisleTemperature().equalsIgnoreCase(newItem.getTemperature())) {
                    Shelf targetShelf;
                    if (aisle.getShelves().size() > 0) {
                        targetShelf = aisle.getShelves().get(0);
                    } else {
                        targetShelf = new Shelf(1);
                        aisle.addShelf(targetShelf);
                    }
                    targetShelf.addGoodItem(newItem);
                    return;
                }
            }
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

        for (int i = 0; i < floorAreas.size(); i++) {
            FloorArea fa = floorAreas.get(i);
            double floorTotal = fa.getTotalValue();
            supermarketTotal += floorTotal;

            output = output + "Floor Area '" + fa.getFloorTitle() + "': " +
                    fa.getAisles().size() + " Aisles, Total Value €" +
                    String.format("%.2f", floorTotal) + "\n";
            // "%.2f" formats the floor total to 2 decimal places.

            for (int j = 0; j < fa.getAisles().size(); j++) {
                Aisle aisle = fa.getAisles().get(j);
                double aisleTotal = aisle.getTotalValue();

                output = output + "  Aisle '" + aisle.getAisleName() + "': " +
                        aisle.getShelves().size() + " Shelves, Total Value €" +
                        String.format("%.2f", aisleTotal) + "\n";

                for (int k = 0; k < aisle.getShelves().size(); k++) {
                    Shelf shelf = aisle.getShelves().get(k);
                    double shelfTotal = shelf.getTotalValue();

                    output = output + "    Shelf " + shelf.getShelfNumber() + ": " +
                            shelf.getGoodItems().size() + " items, Total Value €" +
                            String.format("%.2f", shelfTotal) + "\n";

                    for (int l = 0; l < shelf.getGoodItems().size(); l++) {
                        GoodItem item = shelf.getGoodItems().get(l);
                        double itemTotal = item.getTotalValue();

                        output = output + "      " + item.getDescription() + " (" +
                                item.getWeight() + "): " + item.getQuantity() +
                                " @ €" + String.format("%.2f", item.getUnitPrice()) +
                                " = €" + String.format("%.2f", itemTotal) + "\n";
                    }
                }
            }
        }

        output = output + "\nSupermarket: " + floorAreas.size() +
                " Floor Areas, Total Value €" +
                String.format("%.2f", supermarketTotal) + "\n";

        return output;
    }

}
