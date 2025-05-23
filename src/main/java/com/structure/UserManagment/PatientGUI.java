//package com.structure.UserManagment;
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//
//public class PatientGUI extends Application {
//
//    public PatientGUI() {
//    }
//
//    @Override
//    public void start(Stage stage) throws IOException {
//        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/com/structure/project/Patient.fxml"));
//
//        Scene scene = new Scene(fxmlLoader);
//
//
// scene.getStylesheets().add(getClass().getResource("/com/structure/project/patientView.css").toExternalForm());
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
package com.structure.UserManagment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PatientGUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load FXML
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/com/structure/project/Patient.fxml"));

        // Create scene
        Scene scene = new Scene(fxmlLoader);

        // Apply CSS stylesheet
        scene.getStylesheets().add(getClass().getResource("/com/structure/project/patientView.css").toExternalForm());

        // Set the scene to the stage
        stage.setScene(scene);


        // Show the stage (window)
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
