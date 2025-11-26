package org.example;

import java.io.Serializable;

public class Aisle implements Serializable {

    //---
    //FIELDS
    //---

    private String aisleName;
    private int width;
    private int length;
    private int x;
    private int y;
    private String aisleTemperature;
    private MLinkedList<Shelf> shelves;

    public Aisle(String aisleName, int width, int length, int x, int y, String aisleTemperature) {
        this.aisleName = aisleName;
        this.width = width;
        this.length = length;
        this.x = x;
        this.y = y;
        this.aisleTemperature = aisleTemperature;
        this.shelves = new MLinkedList<>();
    }

    //---
    //GETTERS
    //---

    public String getAisleName() {
        return aisleName;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getAisleTemperature() {
        return aisleTemperature;
    }

    public MLinkedList<Shelf> getShelves() {
        return shelves;
    }

    //---
    //SETTERS
    //---

    public void setAisleName(String aisleName) {
        this.aisleName = aisleName;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setAisleTemperature(String aisleTemperature) {
        this.aisleTemperature = aisleTemperature;
    }

    public void setShelves(MLinkedList<Shelf> shelves) {
        this.shelves = shelves;
    }

    /**
     * adds a shelf to this aisle
     * @param shelf the Shelf object to add to the aisle
     */
    public void addShelf(Shelf shelf) {
        shelves.addElement(shelf);
    }

    //---
    //METHODS
    //---

    /**
     * finds a shelf in this aisle by its shelf number
     * @param number the shelf number to search for
     * @return shelf if found, null if nothing
     */
    public Shelf findShelfByNumber(int number) {
        for (int i = 0; i < shelves.size(); i++) {
            Shelf shelf = shelves.get(i);
            if (shelf.getShelfNumber() == number) {
                return shelf;
            }
        }
        return null;
    }

    /**
     * calculates the total of all items stored in the aisle
     * loops through the shelves and adds their totals together
     * @return total value of the aisle
     */
    public double getTotalValue() {
        double total = 0.0;
        for (int i = 0; i < shelves.size(); i++) {
            total += shelves.get(i).getTotalValue();
        }
        return total;
    }

}
