package com.structure.UserManagment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;




public class AdministratorGUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/com/structure/project/Administrator.fxml"));
        Scene scene= new Scene(fxmlLoader);
        scene.getStylesheets().add(getClass().getResource("/com/structure/project/Doctor.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}