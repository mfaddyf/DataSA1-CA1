package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

public class SupermarketController {

    private Supermarket supermarket = new Supermarket();

    // Floor Area tab
    @FXML private TextField floorTitleField;
    @FXML private TextField floorLevelField;
    @FXML private ListView<String> floorAreaList;
    @FXML private ComboBox<String> floorAreaSelector;

    // Aisle tab
    @FXML private TextField aisleNameField;
    @FXML private TextField aisleLengthField;
    @FXML private TextField aisleWidthField;
    @FXML private ComboBox<String> aisleTempCombo;
    @FXML private ListView<String> aisleList;
    @FXML private ComboBox<String> aisleSelector;

    // Shelf tab
    @FXML private TextField shelfNumberField;
    @FXML private ListView<String> shelfList;
    @FXML private ComboBox<ShelfReference> shelfSelector;

    // Goods tab
    @FXML private TextField goodDescField;
    @FXML private TextField goodSizeField;
    @FXML private TextField goodPriceField;
    @FXML private TextField goodQtyField;
    @FXML private TextField goodPhotoField;
    @FXML private ComboBox<String> goodTempCombo;
    @FXML private ListView<String> goodItemList;

    @FXML private TextArea stockOverviewArea;

    // Called automatically after FXML is loaded
    @FXML
    public void initialize() {
        aisleTempCombo.setItems(FXCollections.observableArrayList("Unrefrigerated", "Refrigerated", "Frozen"));
        goodTempCombo.setItems(FXCollections.observableArrayList("Unrefrigerated", "Refrigerated", "Frozen"));
    }

    @FXML
    public void handleAddFloorArea() {
        String title = floorTitleField.getText();
        String level = floorLevelField.getText();
        if (!title.isEmpty() && !level.isEmpty()) {
            FloorArea fa = new FloorArea(title, level);
            supermarket.addFloorArea(fa);
            floorAreaList.getItems().add(title + " (" + level + ")");
            floorAreaSelector.getItems().add(title);
            floorTitleField.clear();
            floorLevelField.clear();
        }
    }

    @FXML
    public void handleAddAisle() {
        String selectedFloor = floorAreaSelector.getValue();
        String name = aisleNameField.getText();
        String length = aisleLengthField.getText();
        String width = aisleWidthField.getText();
        String temp = aisleTempCombo.getValue();

        if (selectedFloor != null && name != null && temp != null) {
            FloorArea fa = findFloorAreaByTitle(selectedFloor);
            if (fa != null) {
                Aisle aisle = new Aisle(name, length + "x" + width, temp);
                fa.addAisle(aisle);
                aisleList.getItems().add(name + " [" + temp + "]");
                aisleSelector.getItems().add(name);
                aisleNameField.clear();
                aisleLengthField.clear();
                aisleWidthField.clear();
            }
        }
    }

    @FXML
    public void handleAddShelf() {
        String aisleName = aisleSelector.getValue();
        String shelfNumText = shelfNumberField.getText();
        if (aisleName != null && !shelfNumText.isEmpty()) {
            Aisle aisle = findAisleByName(aisleName);
            if (aisle != null) {
                int shelfNum = Integer.parseInt(shelfNumText);
                Shelf shelf = new Shelf(shelfNum);
                aisle.addShelf(shelf);

                ShelfReference ref = new ShelfReference(aisle.getAisleName(), shelfNum);
                shelfList.getItems().add(ref.toString());
                shelfSelector.getItems().add(ref); // Store object, not string

                shelfNumberField.clear();
            }
        }
    }


    @FXML
    public void handleAddGoodItem() {
        ShelfReference ref = shelfSelector.getValue();
        String desc = goodDescField.getText();
        String size = goodSizeField.getText();
        String priceText = goodPriceField.getText();
        String qtyText = goodQtyField.getText();
        String photo = goodPhotoField.getText();
        String temp = goodTempCombo.getValue();

            Shelf shelf = findShelfByReference(ref.getAisleName(), ref.getShelfNumber());
            if (shelf != null) {
                double price = Double.parseDouble(priceText);
                int qty = Integer.parseInt(qtyText);
                GoodItem item = new GoodItem(desc, size, price, qty, temp, photo);
                shelf.addGoodItem(item);
                goodItemList.getItems().add(desc + " x" + qty + " @ €" + price);
                goodDescField.clear();
                goodSizeField.clear();
                goodPriceField.clear();
                goodQtyField.clear();
                goodPhotoField.clear();
                goodTempCombo.getSelectionModel().clearSelection();
            }
    }


    @FXML
    public void handleViewAllStock() {
        String report = supermarket.viewAllStockBreakdown();
        stockOverviewArea.setText(report);
    }

    // Helper methods
    private FloorArea findFloorAreaByTitle(String title) {
        for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
            FloorArea fa = supermarket.getFloorAreas().get(i);
            if (fa.getFloorTitle().equalsIgnoreCase(title)) return fa;
        }
        return null;
    }

    private Aisle findAisleByName(String name) {
        for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
            FloorArea fa = supermarket.getFloorAreas().get(i);
            Aisle aisle = fa.findAisleByName(name);
            if (aisle != null) return aisle;
        }
        return null;
    }

    private Shelf findShelfByReference(String aisleName, int shelfNum) {
        for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
            FloorArea fa = supermarket.getFloorAreas().get(i);
            for (int j = 0; j < fa.getAisles().size(); j++) {
                Aisle aisle = fa.getAisles().get(j);
                if (aisle.getAisleName().equalsIgnoreCase(aisleName)) {
                    Shelf shelf = aisle.findShelfByNumber(shelfNum);
                    if (shelf != null) return shelf;
                }
            }
        }
        return null;
    }

    @FXML
    public void handleSaveSupermarket() {
        supermarket.saveToFile("supermarket.dat");
    }

    @FXML
    public void handleLoadSupermarket() {
        supermarket = Supermarket.loadFromFile("supermarket.dat");
        refreshAllViews();
    }

    public void refreshAllViews() {
        // Clear all GUI lists and selectors
        floorAreaList.getItems().clear();
        floorAreaSelector.getItems().clear();
        aisleList.getItems().clear();
        aisleSelector.getItems().clear();
        shelfList.getItems().clear();
        shelfSelector.getItems().clear();
        goodItemList.getItems().clear();

        // Rebuild from supermarket data
        for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
            FloorArea fa = supermarket.getFloorAreas().get(i);
            floorAreaList.getItems().add(fa.getFloorTitle() + " (" + fa.getFloorLevel() + ")");
            floorAreaSelector.getItems().add(fa.getFloorTitle());

            for (int j = 0; j < fa.getAisles().size(); j++) {
                Aisle aisle = fa.getAisles().get(j);
                aisleList.getItems().add(aisle.getAisleName() + " [" + aisle.getAisleTemperature() + "]");
                aisleSelector.getItems().add(aisle.getAisleName());

                for (int k = 0; k < aisle.getShelves().size(); k++) {
                    Shelf shelf = aisle.getShelves().get(k);
                    ShelfReference ref = new ShelfReference(aisle.getAisleName(), shelf.getShelfNumber());
                    shelfList.getItems().add(ref.toString());
                    shelfSelector.getItems().add(ref);

                    for (int l = 0; l < shelf.getGoodItems().size(); l++) {
                        GoodItem item = shelf.getGoodItems().get(l);
                        goodItemList.getItems().add(item.getDescription() + " x" + item.getQuantity() + " @ €" + item.getUnitPrice());
                    }
                }
            }
        }

        // refresh stock overview
        stockOverviewArea.setText(supermarket.viewAllStockBreakdown());
    }

    @FXML
    public void handleSmartAddGoodItem() {
        String desc = goodDescField.getText();
        String size = goodSizeField.getText();
        String priceText = goodPriceField.getText();
        String qtyText = goodQtyField.getText();
        String photo = goodPhotoField.getText();
        String temp = goodTempCombo.getValue();

        if (desc != null && size != null && priceText != null && qtyText != null && temp != null) {
            try {
                double price = Double.parseDouble(priceText);
                int qty = Integer.parseInt(qtyText);
                GoodItem item = new GoodItem(desc, size, price, qty, temp, photo);

                supermarket.smartAdd(item);

                refreshAllViews();
                goodDescField.clear();
                goodSizeField.clear();
                goodPriceField.clear();
                goodQtyField.clear();
                goodPhotoField.clear();
                goodTempCombo.getSelectionModel().clearSelection();
            } catch (NumberFormatException e) {
            }
        }
    }

}
