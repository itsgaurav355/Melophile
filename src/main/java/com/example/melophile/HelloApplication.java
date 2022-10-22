package com.example.melophile;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 707, 431);
        stage.setTitle("Melophile Login");
       stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent arg0) {
                arg0.consume();
                logout(stage);
            }
        });
    }
    public void logout(Stage stage) {


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're About to logout !");
        alert.setContentText("Do you want to exit ?");

        if (alert.showAndWait().get() == ButtonType.OK) {

            System.out.println("You Successfully logged out!");
            stage.close();
        }
    }
    public static void main(String[] args) {
        launch();
    }
}