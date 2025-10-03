package org.example;

public class FloorArea {

    private String floorTitle; // title the items hold like fruit and veg, dairy, etc
    private String floorLevel;
    private LinkedList<Aisle> aisles;

    public FloorArea(String floorTitle, String floorLevel) {
        this.floorTitle = floorTitle;
        this.floorLevel = floorLevel;
        this.aisles = new LinkedList<>();
    }

    /// TO DO
    /// ADD GETTERS , SETTERS , ADD AISLE , FIND AISLE , GET TOTAL VALUE , ETC
}
