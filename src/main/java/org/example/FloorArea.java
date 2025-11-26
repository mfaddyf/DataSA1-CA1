package org.example;

import java.io.Serializable;

public class FloorArea implements Serializable {

    //---
    //METHODS
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

    public void addAisle(Aisle aisle) {
        aisles.addElement(aisle);
    }

    public Aisle findAisleByName(String name) {
        for (int i = 0; i < aisles.size(); i++) {
            Aisle aisle = aisles.get(i);
            if (aisle.getAisleName().equalsIgnoreCase(name)) {
                return aisle;
            }
        }
        return null;
    }

    public double getTotalValue() {
        double total = 0.0;
        for (int i = 0; i < aisles.size(); i++) {
            total += aisles.get(i).getTotalValue();
        }
        return total;
    }


}
