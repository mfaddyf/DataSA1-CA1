package org.example;

public class Aisle {

    private String aisleName;
    private String aisleDimension;
    private String aisleTemperature;
    private MLinkedList<Shelf> shelves;

    public Aisle(String aisleName, String aisleDimension, String aisleTemperature) {
        this.aisleName = aisleName;
        this.aisleDimension = aisleDimension;
        this.aisleTemperature = aisleTemperature;
        this.shelves = new MLinkedList<>();
    }

    /// TO DO
    /// ADD GETTERS , SETTERS , ETC

    //   --
    //   GETTERS
    //   --

    //   --
    //   SETTERS
    //   --
}
