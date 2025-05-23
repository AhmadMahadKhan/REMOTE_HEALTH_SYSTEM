package com.structure.Logins;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SignIn extends Application {

   @Override
    public void start(Stage stage) throws IOException {
       Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/com/structure/project/SignIn.fxml"));
       Scene scene= new Scene(fxmlLoader);
       scene.getStylesheets().add(getClass().getResource("/com/structure/project/SignIn.css").toExternalForm());
       stage.setScene(scene);
       stage.show();

   }
    public static void main(String[] args) {
        launch();
    }
}