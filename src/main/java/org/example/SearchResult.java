package org.example;

import java.io.Serializable;

/*
        this class is used to format the output of the text when
    searching for good items by name
        it bundles together all the details and returns it all, if good item
    was just returned, it wouldn't tell you any of the location information
    to do with the item
 */

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
