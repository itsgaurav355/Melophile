package com.example.melophile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.events.MouseEvent;

public class PlayerController implements Initializable{

    @FXML
    private Pane pane;
    @FXML
    private Label songLabel;
    @FXML
    private Button playButton, pauseButton, resetButton, previousButton, nextButton;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private ProgressBar songProgressBar;
//    @FXML
//    private Label songLabel1;
    @FXML
    ImageView backgroundImage;


    private Media media;
    private MediaPlayer mediaPlayer;
    private String name;
//    public PlayerController(String name) {
//        this.name=name;
//    }

    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber;
    private int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200};

    private Timer timer;
    private TimerTask task;

    private boolean running;
    RotateTransition rotate=new RotateTransition();


    public void switchToScene1(ActionEvent event) throws IOException {
    mediaPlayer.stop();
    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("logged-in.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 707, 431);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        LoggedInController l=new LoggedInController();
//        l.display(name);
    stage.setScene(scene);
    stage.show();
}
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        try {

            String path="/C://Users//Gaurav//Downloads//Songs/";
            songs = new ArrayList<File>();

            directory = new File(path);

            files = directory.listFiles();

            if (files != null) {

                for (File file : files) {
                    songs.add(file);
                }
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());


            for (int i = 0; i < speeds.length; i++) {

                speedBox.getItems().add(Integer.toString(speeds[i]) + "%");
            }

            speedBox.setOnAction(this::changeSpeed);

            volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

                @Override
                public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

                    mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
                }
            });

            songProgressBar.setStyle("-fx-accent: #00FF00;");

            rotate.setNode(backgroundImage);

            rotate.setDuration(Duration.millis(10000));
            rotate.setCycleCount(TranslateTransition.INDEFINITE);
            rotate.setInterpolator(Interpolator.LINEAR);
            rotate.setByAngle(360);

        } catch (Exception e) {
            System.out.println(e);
        }

}

    public void playMedia() {

        beginTimer();
        rotate.playFromStart();
        changeSpeed(null);
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();
    }

    public void pauseMedia() {

        cancelTimer();
        rotate.stop();
        mediaPlayer.pause();
    }

    public void resetMedia() {

        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    public void previousMedia() {

        if(songNumber > 0) {

            songNumber--;
            rotate.playFromStart();
            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            playMedia();
        }
        else {

            songNumber = songs.size() - 1;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            playMedia();
        }
    }

    public void nextMedia() {

        if(songNumber < songs.size() - 1) {

            songNumber++;

            mediaPlayer.stop();

            if(running) {

                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());
          rotate.playFromStart();

            playMedia();
        }
        else {

            songNumber = 0;

            mediaPlayer.stop();

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            songLabel.setText(songs.get(songNumber).getName());

            playMedia();
        }
    }

    public void changeSpeed(ActionEvent event) {

        if(speedBox.getValue() == null) {

            mediaPlayer.setRate(1);
        }
        else {

            //mediaPlayer.setRate(Integer.parseInt(speedBox.getValue()) * 0.01);
            mediaPlayer.setRate(Integer.parseInt(speedBox.getValue().substring(0, speedBox.getValue().length() - 1)) * 0.01);
        }
    }

    public void beginTimer() {

        timer = new Timer();

        task = new TimerTask() {

            public void run() {

                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgressBar.setProgress(current/end);

                if(current/end == 1) {
                    rotate.stop();
                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void cancelTimer() {

        running = false;
        timer.cancel();
    }
}