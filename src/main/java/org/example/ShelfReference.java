package org.example;

import java.io.Serializable;

/*
        ShelfReference is used to identify which shelf belongs to which aisle so it makes it
    easier to identify it within the GUI for users,
        when the user picks a shelf, it directly retrieves the name and the number, rather than having
    to parse a string into aisle name and shelf number, getters are called
 */

public class ShelfReference implements Serializable {
    private String aisleName;
    private int shelfNumber;

    public ShelfReference(String aisleName, int shelfNumber) {
        this.aisleName = aisleName;
        this.shelfNumber = shelfNumber;
    }

    public String getAisleName() {
        return aisleName;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    @Override
    public String toString() {
        return aisleName + " - Shelf " + shelfNumber;
    }
}

