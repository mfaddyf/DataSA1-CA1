package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class SupermarketController {

    private Supermarket supermarket = new Supermarket();

    // floor area tab
    @FXML private TextField floorTitleField;
    @FXML private TextField floorLevelField;
    @FXML private ListView<String> floorAreaList;
    @FXML private ComboBox<String> floorAreaSelector;

    // aisle tab
    @FXML private TextField aisleNameField;
    @FXML private TextField aisleLengthField;
    @FXML private TextField aisleWidthField;
    @FXML private ComboBox<String> aisleTempCombo;
    @FXML private ListView<String> aisleList;
    @FXML private ComboBox<String> aisleSelector;

    // shelf tab
    @FXML private TextField shelfNumberField;
    @FXML private ListView<String> shelfList;
    @FXML private ComboBox<ShelfReference> shelfSelector;

    // goods tab
    @FXML private TextField goodDescField;
    @FXML private TextField goodSizeField;
    @FXML private TextField goodPriceField;
    @FXML private TextField goodQtyField;
    @FXML private TextField goodPhotoField;
    @FXML private ComboBox<String> goodTempCombo;
    @FXML private ListView<GoodItem> goodItemList;

    @FXML private TextArea stockOverviewArea;

    @FXML private TextField searchField;
    @FXML private TextArea searchResultsArea;

    @FXML private TextField deleteQtyField;

    // called automatically after FXML is loaded
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
        String lengthText = aisleLengthField.getText();
        String widthText = aisleWidthField.getText();
        String temp = aisleTempCombo.getValue();

        if (selectedFloor != null && name != null && !name.isEmpty()
                && temp != null && !lengthText.isEmpty() && !widthText.isEmpty()) {

            if (lengthText.matches("\\d+") && widthText.matches("\\d+")) {
                int heightVal = Integer.parseInt(lengthText); // aisle "length" → height
                int widthVal = Integer.parseInt(widthText);

                FloorArea fa = findFloorAreaByTitle(selectedFloor);
                if (fa != null) {
                    // stagger aisles horizontally so they don’t overlap
                    int index = fa.getAisles().size();
                    int xVal = 50 + (index * 50);
                    int yVal = 100;

                    Aisle aisle = new Aisle(name, widthVal, heightVal, xVal, yVal, temp);
                    fa.addAisle(aisle);

                    aisleList.getItems().add(name + " [" + temp + "]");
                    aisleSelector.getItems().add(name);

                    aisleNameField.clear();
                    aisleLengthField.clear();
                    aisleWidthField.clear();
                    aisleTempCombo.getSelectionModel().clearSelection();
                }
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
                goodItemList.getItems().add(item);
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

    @FXML
    public void handleResetSupermarket() {
        supermarket.reset();
        refreshAllViews();
    }

    public void refreshAllViews() {
        // clear all GUI lists and selectors
        floorAreaList.getItems().clear();
        floorAreaSelector.getItems().clear();
        aisleList.getItems().clear();
        aisleSelector.getItems().clear();
        shelfList.getItems().clear();
        shelfSelector.getItems().clear();
        goodItemList.getItems().clear();

        // rebuild from supermarket data
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
                        goodItemList.getItems().add(item);
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

    @FXML
    public void handleShowMap() {
        Stage mapStage = new Stage();
        mapStage.setTitle("Supermarket Map");

        Pane mapPane = new Pane();
        mapPane.setPrefSize(800, 600);

        for (int i = 0; i < supermarket.getFloorAreas().size(); i++) {
            FloorArea fa = supermarket.getFloorAreas().get(i);

            // add floor heading
            Label floorLabel = new Label(fa.getFloorTitle() + " (" + fa.getFloorLevel() + ")");
            floorLabel.setLayoutX(20);
            floorLabel.setLayoutY(40 + (i * 200));
            mapPane.getChildren().add(floorLabel);

            int floorOffsetY = i * 200;

            for (int j = 0; j < fa.getAisles().size(); j++) {
                Aisle aisle = fa.getAisles().get(j);

                System.out.println("Drawing aisle: " + aisle.getAisleName() +
                        " at (" + aisle.getX() + ", " + (aisle.getY() + floorOffsetY) + ") size: " +
                        aisle.getWidth() + "x" + aisle.getLength());

                Rectangle aisleRect = new Rectangle(
                        aisle.getX(), aisle.getY() + floorOffsetY,
                        aisle.getWidth(), aisle.getLength()
                );
                aisleRect.setFill(Color.LIGHTBLUE);
                aisleRect.setStroke(Color.BLACK);

                Label aisleLabel = new Label(aisle.getAisleName());
                aisleLabel.setLayoutX(aisle.getX());
                aisleLabel.setLayoutY(aisle.getY() + floorOffsetY - 20);

                aisleRect.setOnMouseClicked(e -> showShelves(aisle));

                mapPane.getChildren().addAll(aisleRect, aisleLabel);
            }
        }

        Scene scene = new Scene(mapPane);
        mapStage.setScene(scene);
        mapStage.show();
    }

    private void showShelves(Aisle aisle) {
        Stage shelfStage = new Stage();
        javafx.scene.layout.VBox box = new javafx.scene.layout.VBox(8);
        box.setPadding(new javafx.geometry.Insets(10));

        for (int i = 0; i < aisle.getShelves().size(); i++) {
            Shelf shelf = aisle.getShelves().get(i);
            javafx.scene.control.Button b = new javafx.scene.control.Button("Shelf " + shelf.getShelfNumber());
            b.setOnAction(e -> showItems(shelf));
            box.getChildren().add(b);
        }

        shelfStage.setTitle("Shelves in " + aisle.getAisleName());
        shelfStage.setScene(new Scene(box, 320, 240));
        shelfStage.show();
    }

    private void showItems(Shelf shelf) {
        javafx.scene.control.ListView<String> list = new javafx.scene.control.ListView<>();
        for (int i = 0; i < shelf.getGoodItems().size(); i++) {
            GoodItem g = shelf.getGoodItems().get(i);
            list.getItems().add(g.getDescription() + " x" + g.getQuantity());
        }
        Stage s = new Stage();
        s.setTitle("Items in shelf " + shelf.getShelfNumber());
        s.setScene(new Scene(list, 320, 240));
        s.show();
    }

    @FXML
    public void handleSearchGoodItem() {
        String name = searchField.getText();
        if (name != null && !name.isEmpty()) {
            MLinkedList<SearchResult> results = supermarket.searchGoodItemByName(name);
            StringBuilder sb = new StringBuilder();
            Node<SearchResult> current = results.getHead();
            while (current != null) {
                sb.append(current.data.toString()).append("\n");
                current = current.next;
            }
            searchResultsArea.setText(sb.toString());
        }
    }

    @FXML
    public void handleDeleteSelectedGoodItem() {
        ShelfReference ref = shelfSelector.getValue();
        GoodItem selectedItem = goodItemList.getSelectionModel().getSelectedItem();
        String qtyText = deleteQtyField.getText();

        if (ref != null && selectedItem != null && qtyText != null && !qtyText.isEmpty()) {
            try {
                int qty = Integer.parseInt(qtyText);
                Shelf shelf = findShelfByReference(ref.getAisleName(), ref.getShelfNumber());
                if (shelf != null) {
                    shelf.removeGoodItem(selectedItem.getDescription(), qty);
                    refreshAllViews();
                    deleteQtyField.clear();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity entered.");
            }
        }
    }


    @FXML
    public void handleDeleteAllGoodItems() {
        ShelfReference ref = shelfSelector.getValue();
        if (ref != null) {
            Shelf shelf = findShelfByReference(ref.getAisleName(), ref.getShelfNumber());
            if (shelf != null) {
                shelf.getGoodItems().clear(); // clear linked list
                refreshAllViews();
            }
        }
    }

}
