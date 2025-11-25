package org.example;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SupermarketShop extends Application {

        @Override
        public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(SupermarketShop.class.getResource("supermarket_shop.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 400);
            stage.setTitle("Supermarket Management System!");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }
}

// TO DO
// floors to the gui map..... omg how did i forget.....
// search for good items
// remove good items
// reset facility

// REFERENCES FOR WEBSITES I LOOKED AT TO SOLVE PROBLEMS
//  https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/parseInt