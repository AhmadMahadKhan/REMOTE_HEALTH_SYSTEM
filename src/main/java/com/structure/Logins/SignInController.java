    package com.structure.Logins;


    import com.structure.Model.Administrator;
    import com.structure.Model.Doctor;
    import com.structure.Model.Patient;
    import com.structure.UserManagment.AdministratorController;
    import com.structure.UserManagment.DoctorController;
    import com.structure.UserManagment.PatientController;
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

    public class SignInController {




        private Scene scene;
        private Stage stage;
        private Parent root;

        //goto the sign up page
        @FXML
        public void switchToSignUp(ActionEvent actionEvent) throws IOException {
             root = FXMLLoader.load(getClass().getResource("/com/structure/project/SignUp.fxml"));
            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

        @FXML
        private TextField username;
        @FXML
        private TextField id;
        @FXML
        private PasswordField password;
        //goto the dashboard of the respective user
        @FXML void signInButton(ActionEvent actionEvent) throws SQLException, IOException {

            String usernameText = username.getText().trim();
            String passwordText = password.getText().trim();
            String idText = id.getText().trim();

            if(usernameText.isEmpty() || passwordText.isEmpty() || idText.isEmpty()){
               Alert alert = new Alert(Alert.AlertType.ERROR);
               alert.setTitle("Error");
               alert.setHeaderText("Empty Fields");
               alert.setContentText("Please fill all the fields");
               alert.showAndWait();
               return;
            }
            //check if the password is set or not in the database
            if(LoginManager.passwordValidation(usernameText,idText)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Password Not Set");
                alert.setContentText("Your password is not set. Please set your password and try again.");
                alert.showAndWait();
                return;
            }

            //creating an instance of object "user" and then the output from loginManager class method check user is stored in
            //that instaance
            Object user = LoginManager.checkUser(usernameText, idText, passwordText);

            //checking which class instance is stored in the user object
            //then tranfering to the respective class dashboard
            if (user instanceof Patient) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/structure/project/Patient.fxml"));
                Parent root = loader.load();
                PatientController controller = loader.getController();
                controller.setPatient((Patient) user); // Inject user

                Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }
            else if (user instanceof Doctor) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/structure/project/Doctor.fxml"));
                Parent root = loader.load();
                DoctorController controller = loader.getController();
                controller.setDoctor((Doctor) user);

                Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }
            else if (user instanceof Administrator) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/structure/project/Administrator.fxml"));
                Parent root = loader.load();
                AdministratorController controller = loader.getController();
                controller.setAdmin((Administrator) user);

                Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("User not found");
                alert.setContentText("Please check your username and try again.");
                alert.showAndWait();
                return;
            }


        }


        private void switchScene(ActionEvent event, String fxmlPath) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }





    }
