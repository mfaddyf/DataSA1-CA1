package org.example;

import java.io.Serializable;

public class FloorArea implements Serializable {

    //---
    //FIELDS
    //---

    private String floorTitle;
    private String floorLevel;
    private MLinkedList<Aisle> aisles;

    public FloorArea(String floorTitle, String floorLevel) {
        this.floorTitle = floorTitle;
        this.floorLevel = floorLevel;
        this.aisles = new MLinkedList<>();
    }

    //---
    //GETTERS
    //---

    public String getFloorTitle() {
        return floorTitle;
    }

    public String getFloorLevel() {
        return floorLevel;
    }

    public MLinkedList<Aisle> getAisles() {
        return aisles;
    }


    //---
    //SETTERS
    //---

    public void setFloorTitle(String floorTitle) {
        this.floorTitle = floorTitle;
    }

    public void setFloorLevel(String floorLevel) {
        this.floorLevel = floorLevel;
    }

    public void setAisles(MLinkedList<Aisle> aisles) {
        this.aisles = aisles;
    }

    //---
    //METHODS
    //---

    /**
     * adds a new aisle to the floor area
     * the aisle is added to the linked list of aisles beneath this floor area
     * @param aisle
     */
    public void addAisle(Aisle aisle) {
        aisles.addElement(aisle);
    }

    /**
     * calculates the total value of stock in this floor area
     * loops through each aisle and sums their total values
     * @return the combined value of the floor area
     */
    public double getTotalValue() {
        double total = 0.0;
        for (int i = 0; i < aisles.size(); i++) {
            total += aisles.get(i).getTotalValue();
        }
        return total;
    }
}
