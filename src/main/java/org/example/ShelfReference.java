package org.example;

import java.io.Serializable;

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

