package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SupermarketShop extends Application {

        @Override
        public void start(Stage stage) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(SupermarketShop.class.getResource("supermarket_shop.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
            stage.setTitle("Supermarket Management System!");
            stage.setScene(scene);
            stage.show();
        }
}



// REFERENCES FOR WEBSITES I USED TO RESEARCH SOME THINGS
//  https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Spinner.html
//  https://openjfx.io/javadoc/25/javafx.fxml/javafx/fxml/doc-files/introduction_to_fxml.html