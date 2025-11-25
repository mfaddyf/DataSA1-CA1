package org.example;

import java.io.Serializable;

public class SearchResult implements Serializable {
    private String floorTitle;
    private String floorLevel;
    private String aisleName;
    private int shelfNumber;
    private GoodItem item;

    public SearchResult(String floorTitle, String floorLevel, String aisleName, int shelfNumber, GoodItem item) {
        this.floorTitle = floorTitle;
        this.floorLevel = floorLevel;
        this.aisleName = aisleName;
        this.shelfNumber = shelfNumber;
        this.item = item;
    }

    public String toString() {
        return "Floor: " + floorTitle + " (" + floorLevel + "), " +
                "Aisle: " + aisleName + ", Shelf: " + shelfNumber +
                " → " + item.getDescription() + " x" + item.getQuantity();
    }
}
