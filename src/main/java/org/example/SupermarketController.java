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

    // -------- floor area tab
    @FXML private TextField floorTitleField;
    @FXML private TextField floorLevelField;
    @FXML private ListView<String> floorAreaList;
    @FXML private ComboBox<String> floorAreaSelector;

    // -------- aisle tab
    @FXML private TextField aisleNameField;
    @FXML private Spinner<Integer> aisleLengthSpinner;
    @FXML private Spinner<Integer> aisleWidthSpinner;
    @FXML private ComboBox<String> aisleTempCombo;
    @FXML private ListView<String> aisleList;
    @FXML private ComboBox<String> aisleSelector;

    // -------- shelf tab
    @FXML private Spinner<Integer> shelfNumberSpinner;
    @FXML private ListView<String> shelfList;
    @FXML private ComboBox<ShelfReference> shelfSelector;

    // -------- goods tab
    @FXML private TextField goodDescField;
    @FXML private TextField goodSizeField;
    @FXML private Spinner<Double> goodPriceSpinner;
    @FXML private Spinner<Integer> goodQtySpinner;
    @FXML private TextField goodPhotoField;
    @FXML private ComboBox<String> goodTempCombo;
    @FXML private ListView<GoodItem> goodItemList;

    // -------- stock overview
    @FXML private TextArea stockOverviewArea;

    // -------- searching for good items
    @FXML private TextField searchField;
    @FXML private TextArea searchResultsArea;

    // -------- deleting function
    @FXML private Spinner<Integer> deleteQtySpinner;

    // -------- initialize
    @FXML
    public void initialize() {
        // create temperature dropdowns
        aisleTempCombo.setItems(FXCollections.observableArrayList("Unrefrigerated", "Refrigerated", "Frozen"));
        goodTempCombo.setItems(FXCollections.observableArrayList("Unrefrigerated", "Refrigerated", "Frozen"));

        // configure spinners with ranges and defaults
        aisleLengthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 10));
        aisleWidthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 5));
        shelfNumberSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));
        goodPriceSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.01, 1000.0, 1.0, 0.5));
        goodQtySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 1));
        deleteQtySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500, 1));

        // allow typing into spinners
        aisleLengthSpinner.setEditable(true);
        aisleWidthSpinner.setEditable(true);
        shelfNumberSpinner.setEditable(true);
        goodPriceSpinner.setEditable(true);
        goodQtySpinner.setEditable(true);
        deleteQtySpinner.setEditable(true);

    }


    // -------- floor area handlers

    /**
     * handles creating a new floor area in the supermarket
     * read title and level text fields, creates floor area
     * updates the model and refreshes the gui
     */
    @FXML
    public void handleAddFloorArea() {
        String title = floorTitleField.getText();
        String level = floorLevelField.getText();
        if (!title.isEmpty() && !level.isEmpty()) {
            // update model only
            supermarket.addFloorArea(new FloorArea(title, level));

            // refresh GUI from MLinkedList
            refreshAllViews();

            floorTitleField.clear();
            floorLevelField.clear();
        }
    }


    // -------- aisle handlers
    @FXML
    public void handleAddAisle() {
        String selectedFloor = floorAreaSelector.getValue();
        String name = aisleNameField.getText();
        String temp = aisleTempCombo.getValue();

        if (selectedFloor != null && name != null && !name.isEmpty() && temp != null) {
            FloorArea fa = findFloorAreaByTitle(selectedFloor);
            if (fa != null) {
                int heightVal = aisleLengthSpinner.getValue();
                int widthVal  = aisleWidthSpinner.getValue();

                int index = countAisles(fa); // use MLinkedList traversal to count
                int xVal = 50 + (index * 50);
                int yVal = 100;

                fa.addAisle(new Aisle(name, widthVal, heightVal, xVal, yVal, temp));

                // Refresh GUI from MLinkedList
                refreshAllViews();

                aisleNameField.clear();
                aisleTempCombo.getSelectionModel().clearSelection();
            }
        }
    }


    // -------- shelf handlers
    @FXML
    public void handleAddShelf() {
        String aisleName = aisleSelector.getValue();
        if (aisleName != null) {
            Aisle aisle = findAisleByName(aisleName);
            if (aisle != null) {
                int shelfNum = shelfNumberSpinner.getValue();
                aisle.addShelf(new Shelf(shelfNum));

                refreshAllViews();
                shelfNumberSpinner.getValueFactory().setValue(1);
            }
        }
    }

    // -------- item handlers
    @FXML
    public void handleAddGoodItem() {
        ShelfReference ref = shelfSelector.getValue();
        String desc = goodDescField.getText();
        String size = goodSizeField.getText();
        String photo = goodPhotoField.getText();
        String temp = goodTempCombo.getValue();

        if (ref != null && desc != null && size != null && temp != null) {
            Shelf shelf = findShelfByReference(ref.getAisleName(), ref.getShelfNumber());
            if (shelf != null) {
                double price = goodPriceSpinner.getValue();
                int qty = goodQtySpinner.getValue();
                shelf.addGoodItem(new GoodItem(desc, size, price, qty, temp, photo));

                refreshAllViews();

                goodDescField.clear();
                goodSizeField.clear();
                goodPhotoField.clear();
                goodTempCombo.getSelectionModel().clearSelection();
                goodPriceSpinner.getValueFactory().setValue(1.0);
                goodQtySpinner.getValueFactory().setValue(1);
            }
        }
    }

    @FXML
    public void handleSmartAddGoodItem() {
        String desc = goodDescField.getText();
        String size = goodSizeField.getText();
        String photo = goodPhotoField.getText();
        String temp = goodTempCombo.getValue();

        if (desc != null && size != null && temp != null) {
            double price = goodPriceSpinner.getValue();
            int qty = goodQtySpinner.getValue();
            supermarket.smartAdd(new GoodItem(desc, size, price, qty, temp, photo));

            refreshAllViews();

            goodDescField.clear();
            goodSizeField.clear();
            goodPhotoField.clear();
            goodTempCombo.getSelectionModel().clearSelection();
            goodPriceSpinner.getValueFactory().setValue(1.0);
            goodQtySpinner.getValueFactory().setValue(1);
        }
    }

    // -------- stock overview
    @FXML
    public void handleViewAllStock() {
        String report = supermarket.viewAllStockBreakdown();
        stockOverviewArea.setText(report);
    }

    private FloorArea findFloorAreaByTitle(String title) {
        Node<FloorArea> n = supermarket.getFloorAreas().getHead();
        while (n != null) {
            FloorArea fa = n.data;
            if (fa.getFloorTitle().equalsIgnoreCase(title)) return fa;
            n = n.next;
        }
        return null;
    }

    private Aisle findAisleByName(String name) {
        Node<FloorArea> fn = supermarket.getFloorAreas().getHead();
        while (fn != null) {
            Node<Aisle> an = fn.data.getAisles().getHead();
            while (an != null) {
                Aisle aisle = an.data;
                if (aisle.getAisleName().equalsIgnoreCase(name)) return aisle;
                an = an.next;
            }
            fn = fn.next;
        }
        return null;
    }

    private Shelf findShelfByReference(String aisleName, int shelfNum) {
        Node<FloorArea> fn = supermarket.getFloorAreas().getHead();
        while (fn != null) {
            Node<Aisle> an = fn.data.getAisles().getHead();
            while (an != null) {
                Aisle aisle = an.data;
                if (aisle.getAisleName().equalsIgnoreCase(aisleName)) {
                    Node<Shelf> sn = aisle.getShelves().getHead();
                    while (sn != null) {
                        Shelf shelf = sn.data;
                        if (shelf.getShelfNumber() == shelfNum) return shelf;
                        sn = sn.next;
                    }
                }
                an = an.next;
            }
            fn = fn.next;
        }
        return null;
    }


    private int countAisles(FloorArea fa) {
        int c = 0;
        Node<Aisle> n = fa.getAisles().getHead();
        while (n != null) { c++; n = n.next; }
        return c;
    }

    // -------- file operations
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

    // -------- gui refresh
    public void refreshAllViews() {
        floorAreaList.getItems().clear();
        floorAreaSelector.getItems().clear();
        aisleList.getItems().clear();
        aisleSelector.getItems().clear();
        shelfList.getItems().clear();
        shelfSelector.getItems().clear();
        goodItemList.getItems().clear();

        Node<FloorArea> faNode = supermarket.getFloorAreas().getHead();
        while (faNode != null) {
            FloorArea fa = faNode.data;
            floorAreaList.getItems().add(fa.getFloorTitle() + " (" + fa.getFloorLevel() + ")");
            floorAreaSelector.getItems().add(fa.getFloorTitle());

            Node<Aisle> aisleNode = fa.getAisles().getHead();
            while (aisleNode != null) {
                Aisle aisle = aisleNode.data;
                aisleList.getItems().add(aisle.getAisleName() + " [" + aisle.getAisleTemperature() + "]");
                aisleSelector.getItems().add(aisle.getAisleName());

                Node<Shelf> shelfNode = aisle.getShelves().getHead();
                while (shelfNode != null) {
                    Shelf shelf = shelfNode.data;
                    ShelfReference ref = new ShelfReference(aisle.getAisleName(), shelf.getShelfNumber());
                    shelfList.getItems().add(ref.toString());
                    shelfSelector.getItems().add(ref);

                    Node<GoodItem> itemNode = shelf.getGoodItems().getHead();
                    while (itemNode != null) {
                        goodItemList.getItems().add(itemNode.data);
                        itemNode = itemNode.next;
                    }
                    shelfNode = shelfNode.next;
                }
                aisleNode = aisleNode.next;
            }
            faNode = faNode.next;
        }

        stockOverviewArea.setText(supermarket.viewAllStockBreakdown());
    }


    // -------- map visualisation
    @FXML
    public void handleShowMap() {
        Stage mapStage = new Stage();
        mapStage.setTitle("Supermarket Map");

        Pane mapPane = new Pane();
        mapPane.setPrefSize(800, 600);

        int floorIndex = 0;
        Node<FloorArea> faNode = supermarket.getFloorAreas().getHead();
        while (faNode != null) {
            FloorArea fa = faNode.data;

            // add floor heading
            Label floorLabel = new Label(fa.getFloorTitle() + " (" + fa.getFloorLevel() + ")");
            floorLabel.setLayoutX(20);
            floorLabel.setLayoutY(40 + (floorIndex * 200));
            mapPane.getChildren().add(floorLabel);

            int floorOffsetY = floorIndex * 200;

            Node<Aisle> aisleNode = fa.getAisles().getHead();
            while (aisleNode != null) {
                Aisle aisle = aisleNode.data;

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

                aisleNode = aisleNode.next;
            }

            faNode = faNode.next;
            floorIndex++;
        }

        Scene scene = new Scene(mapPane);
        mapStage.setScene(scene);
        mapStage.show();
    }


    // -------- shelf and item pop up windows
    private void showShelves(Aisle aisle) {
        Stage shelfStage = new Stage();
        javafx.scene.layout.VBox box = new javafx.scene.layout.VBox(8);
        box.setPadding(new javafx.geometry.Insets(10));

        Node<Shelf> shelfNode = aisle.getShelves().getHead();
        while (shelfNode != null) {
            Shelf shelf = shelfNode.data;
            javafx.scene.control.Button b = new javafx.scene.control.Button("Shelf " + shelf.getShelfNumber());
            b.setOnAction(e -> showItems(shelf));
            box.getChildren().add(b);
            shelfNode = shelfNode.next;
        }

        shelfStage.setTitle("Shelves in " + aisle.getAisleName());
        shelfStage.setScene(new Scene(box, 320, 240));
        shelfStage.show();
    }


    private void showItems(Shelf shelf) {
        javafx.scene.control.ListView<String> list = new javafx.scene.control.ListView<>();

        Node<GoodItem> itemNode = shelf.getGoodItems().getHead();
        while (itemNode != null) {
            GoodItem g = itemNode.data;
            list.getItems().add(g.getDescription() + " x" + g.getQuantity());
            itemNode = itemNode.next;
        }

        Stage s = new Stage();
        s.setTitle("Items in shelf " + shelf.getShelfNumber());
        s.setScene(new Scene(list, 320, 240));
        s.show();
    }


    // -------- searching items
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

    // -------- deleting items
    @FXML
    public void handleDeleteSelectedGoodItem() {
        ShelfReference ref = shelfSelector.getValue();
        GoodItem selectedItem = goodItemList.getSelectionModel().getSelectedItem();
        Integer qty = deleteQtySpinner.getValue();

        if (ref != null && selectedItem != null && qty != null && qty > 0) {
            Shelf shelf = findShelfByReference(ref.getAisleName(), ref.getShelfNumber());
            if (shelf != null) {
                shelf.removeGoodItem(selectedItem.getDescription(), qty); // operates on MLinkedList
                refreshAllViews();
                deleteQtySpinner.getValueFactory().setValue(1);
            }
        }
    }

    @FXML
    public void handleDeleteAllGoodItems() {
        ShelfReference ref = shelfSelector.getValue();
        if (ref != null) {
            Shelf shelf = findShelfByReference(ref.getAisleName(), ref.getShelfNumber());
            if (shelf != null) {
                shelf.getGoodItems().clear(); // your MLinkedList.clear()
                refreshAllViews();
            }
        }
    }
}
