package com.example.melophile;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Button logout;
    @FXML
    private Label email;
    @FXML
    private Label username;
    @FXML
    private MediaPlayer player;
    @FXML
    private Button playButton;
    @FXML
    private Button browseFileButton;
    @FXML
    private VBox folderBox;
    @FXML
    public ListView<File> songList;
    @FXML
    private TextField pathField;

    private Stage stage;
    private Scene scene;
    private Parent root;
    //Extra
    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber;



    //Starting for browsing
    public void setUserInformation(String username ,String email){
        this.username.setText( username);
        this.email.setText("Welcome "+username+" !");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "hello-view.fxml", "Login", null, null);
            }
        });
        songList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("musicplayer.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 707 ,431);
                //    DBUtils.addSongs(mouseEvent,songList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
        });
//        //extra
        //

    }
    public void ChooseFile(ActionEvent event) {
        try {

            String name=pathField.getText().toString();
            songs = new ArrayList<File>();

            directory = new File(name);

            files = directory.listFiles();

            if (files != null) {

                for (File file : files) {

                    songs.add(file);
                }
                songList.getItems().addAll(songs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToScene1(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("logged-in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500,300);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene2(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("musicplayer.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 707, 431);
        scene.setFill(Color.DEEPSKYBLUE);
        //stage.setResizable(false);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}

