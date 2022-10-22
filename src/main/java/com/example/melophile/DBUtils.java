package com.example.melophile;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

//import javax.xml.transform.Result;
import java.io.File;
import java.sql.*;

import java.io.IOException;



public class DBUtils {

    public static void changeScene(ActionEvent event, String fxmlFile,String title ,String username,String email ){
        Parent root =null;

        if(username !=null && email != null){
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root=loader.load();
                LoggedInController loggedInController=loader.getController();
                loggedInController.setUserInformation(username,email);
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            try{
                root =FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        Stage stage =(Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root , 707,431));
        stage.show();
    }
    public static void signUpUser(ActionEvent event , String username ,String password ,String email){
        Connection connection=null;
        PreparedStatement psInsert =null;
        PreparedStatement psCheckUserExists =null;
        ResultSet resultSet =null;

        try{
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/login","root","1234");
            psCheckUserExists= connection.prepareStatement("SELECT * FROM users WHERE username = ? ");
            psCheckUserExists.setString(1,username);
           resultSet = psCheckUserExists.executeQuery();
           if(resultSet.isBeforeFirst() ){
                System.out.println("User Already Exists ! ");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username it is already taken");
                alert.show();
            }
            else{
                psInsert =connection.prepareStatement("INSERT INTO users (username ,password,email_id) VALUES (? , ? ,?)");
                psInsert.setString(1,username);
                psInsert.setString(2,password);
                psInsert.setString(3,email);
                psInsert.executeUpdate();

                changeScene(event ,"logged-in.fxml","Welcome",username,email);

            }
        }catch (SQLException e)
        {
        e.printStackTrace();
        }finally{
            if(resultSet != null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(psCheckUserExists != null){
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(psInsert != null){

                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
                try{
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
    /*
    public static ListView<File> addSongs(MouseEvent event , ListView<File> songs){
        Connection connection=null;
        PreparedStatement psInsert =null;
        PreparedStatement psCheckUserExists =null;
        ResultSet resultSet =null;
        ListView<String> songsList = null;
        try{
            connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/login","root","1234");
            psCheckUserExists= connection.prepareStatement("SELECT * FROM songs WHERE song = ? ");

            psCheckUserExists.setString(1,songs.getItems().toString());
            resultSet = psCheckUserExists.executeQuery();
            if(resultSet.isBeforeFirst() ){
                System.out.println("Song Already Exists ! ");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot add this song it is already exist");
                alert.show();
            }
            else{
                String song=songs.getItems().toString();
                psInsert =connection.prepareStatement("INSERT INTO songs (song) VALUES (?)");
                psInsert.setString(1,song);
                psInsert.executeUpdate();
                while(resultSet.next()){
                    String temp=songs.getItems().toString();
                //  songsList.setItems();
                }
                return songsList;

            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }finally{
            if(resultSet != null){
                try{
                    resultSet.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(psCheckUserExists != null){
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(psInsert != null){

                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null){
                try{
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
        }
        return songsList;
    }

     */
    public static void logInUser(ActionEvent event , String username ,String password){
    Connection connection=null;
    PreparedStatement preparedStatement=null;
    ResultSet resultSet=null;
    try {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/login", "root", "1234");//error
        preparedStatement=connection.prepareStatement("SELECT password,email_id FROM users WHERE username = ?");
        preparedStatement.setString(1,username);
       resultSet = preparedStatement.executeQuery();

        if(!resultSet.isBeforeFirst()){
            System.out.println("User Not found in the Database");
            Alert alert =new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Provided credentials are incorrect or empty please fill it Properly! ");
            alert.show();
        }else{
            while(resultSet.next()){
                String retrievedPassword =resultSet.getString("password");
                String retrieveEmail =resultSet.getString("email_id");
                if(retrievedPassword.equals(password)){
                    changeScene(event,"logged-in.fxml","Melophile",username,retrieveEmail);
                }else{
                    System.out.println("Incorrect Password");
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("The Provided credentials are incorrect");
                    alert.show();
                }
            }
        }

    }catch(SQLException e){
        e.printStackTrace();
    }catch(Exception e ){
      e.printStackTrace();
    }  finally{
        if(resultSet != null){

            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(preparedStatement!= null){

            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection != null){

            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    }
}
