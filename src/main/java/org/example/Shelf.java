package org.example;

import java.io.Serializable;

public class Shelf implements Serializable {

    //---
    //FIELDS
    //---

    private int shelfNumber;
    private MLinkedList<GoodItem> goodItems;

    public Shelf(int shelfNumber) {
        this.shelfNumber = shelfNumber;
        this.goodItems = new MLinkedList<>();
    }

    //---
    //GETTERS
    //---

    public int getShelfNumber() {
        return shelfNumber;
    }

    public MLinkedList<GoodItem> getGoodItems() {
        return goodItems;
    }

    //---
    //SETTERS
    //---

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public void setGoodItems(MLinkedList<GoodItem> goodItems) {
        this.goodItems = goodItems;
    }

    //---
    //METHODS
    //---

    /**
     * loops and calculates the total value of the shelf
     * @return total value of items on shelf
     */
    public double getTotalValue() {
        double total = 0.0;
        for (int i = 0; i < goodItems.size(); i++) {
            total += goodItems.get(i).getTotalValue();
        }
        return total;
    }

    /**
     * adds a new GoodItem to the shelf
     * if an item with the same description and weight exists it will update the original
     * @param newItem the item to add
     */
    public void addGoodItem(GoodItem newItem) {
        for (int i = 0; i < goodItems.size(); i++) {
            GoodItem existing = goodItems.get(i);
            if (existing.matches(newItem)) {
                existing.setQuantity(existing.getQuantity() + newItem.getQuantity());
                existing.setUnitPrice(newItem.getUnitPrice());
                existing.setPhotoUrl(newItem.getPhotoUrl());
                return;
            }
        }
        goodItems.addElement(newItem);
    }

    /**
     * removes a given quantity of an item from the shelf
     * if the quantity to remove is greater or equal to the existing amount it will remove it completely
     * if not, the quantity is reduced
     * @param description description/name of item to remove
     * @param quantity amount to remove of said item
     */
    public void removeGoodItem(String description, int quantity) {
        for (int i = 0; i < goodItems.size(); i++) {
            GoodItem item = goodItems.get(i);
            if (item.getDescription().equalsIgnoreCase(description)) {
                if (quantity >= item.getQuantity()) {
                    goodItems.remove(item);
                } else {
                    item.setQuantity(item.getQuantity() - quantity);
                }
                return;
            }
        }
    }

}
