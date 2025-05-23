package com.structure.Logins;

import com.structure.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController {
    @FXML
    private TextField signupemail;
    @FXML
    private TextField signupid;
    @FXML
    private PasswordField signuppassword;
    private Scene scene;
    private Stage stage;
    private Parent root;

    //goto the sign in page
    @FXML
    public void returnToSignIn(ActionEvent actionEvent) throws IOException {
         root = FXMLLoader.load(getClass().getResource("/com/structure/project/SignIn.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    //goto sign in page after submitting the form
    public void returnToSignInAfterSubmit(ActionEvent actionEvent) throws IOException, SQLException {

        //creating an instance of the user class to store the data entered by the user
        User user ;

        //casting the fxml injected data to respective types
        String email = signupemail.getText().trim();
        String id = signupid.getText().trim();
        String password = signuppassword.getText().trim();

        //to display an alert if the user misses any detail in the sign in page
        if(email.isEmpty() || id.isEmpty() || password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
            return;
        }


        System.out.println("pass set");
        //display an alert
        //password validation checks that the password is already set  or not
        // passwordValidation return "false" if the password is set for the user
        if(!LoginManager.passwordValidation(email , id)){
            System.out.println("i am working not password validation");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("User already exists or not allowed");
            alert.setContentText("You are either already registered or not authorized. Please contact support.");
            alert.showAndWait();
            return;
        }

        //setting the password after checking if the password is set or not
        //
        LoginManager.setPassword(email , id , password);

        root = FXMLLoader.load(getClass().getResource("/com/structure/project/SignIn.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
