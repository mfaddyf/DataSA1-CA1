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

    public String getAisleName() {
        return aisleName;
    }

    public String getAisleDimension() {
        return aisleDimension;
    }

    public String getAisleTemperature() {
        return aisleTemperature;
    }

    public MLinkedList<Shelf> getShelves() {
        return shelves;
    }

    //   --
    //   SETTERS
    //   --

    public void setAisleName(String aisleName) {
        this.aisleName = aisleName;
    }

    public void setAisleDimension(String aisleDimension) {
        this.aisleDimension = aisleDimension;
    }

    public void setAisleTemperature(String aisleTemperature) {
        this.aisleTemperature = aisleTemperature;
    }

    public void setShelves(MLinkedList<Shelf> shelves) {
        this.shelves = shelves;
    }

    public void addShelf(Shelf shelf) {
        shelves.addElement(shelf);
    }

    public Shelf findShelfByNumber(int number) {
        for (int i = 0; i < shelves.size(); i++) {
            Shelf shelf = shelves.get(i);
            if (shelf.getShelfNumber() == number) {
                return shelf;
            }
        }
        return null;
    }

    public double getTotalValue() {
        double total = 0.0;
        for (int i = 0; i < shelves.size(); i++) {
            total += shelves.get(i).getTotalValue();
        }
        return total;
    }

}
