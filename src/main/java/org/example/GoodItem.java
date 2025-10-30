package org.example;

public class GoodItem {

    private String description;
    private String weight;
    private double unitPrice;
    private int quantity;
    private String temperature;
    private String photoUrl;

    public GoodItem(String description, String weight, double unitPrice, int quantity, String temperature,  String photoUrl) {
        this.description = description;
        this.weight = weight;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.temperature = temperature;
        this.photoUrl = photoUrl;
    }

    /// TO DO
    /// ADD GETTERS , SETTERS , ETC

    //   --
    //   GETTERS
    //   --
    public String getDescription() {
        return description;
    }

    public String getWeight() {
        return weight;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    //   --
    //   SETTERS
    //   --

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    //   --
    //   METHODS
    //   --
    public double getTotalValue() {
        return unitPrice * quantity;
    }

    public boolean matches(GoodItem other) {
        return this.description.equalsIgnoreCase(other.description) && this.weight.equalsIgnoreCase(other.weight);
    }

    public String getSummary() {
        return description + " (" + weight + ") x" + quantity + " @ €" + unitPrice;
    }

}
