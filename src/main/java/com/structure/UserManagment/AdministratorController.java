package com.structure.UserManagment;

import com.structure.DataAccessObject.DAOAdministrator;
import com.structure.Exception.DuplicateUserException;
import com.structure.Exception.UserNotFoundException;
import com.structure.Model.Administrator;
import com.structure.Model.Doctor;
import com.structure.Model.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class AdministratorController  {

    public AdministratorController() {

    }
    public AdministratorController(Administrator administrator) {
        this.administrator = administrator;
    }
    private Scene scene;
    private Stage stage;
    private Parent root;
    private Administrator administrator;
    public void setAdmin(Administrator administrator) {


        this.administrator  = administrator ;


    }

    @FXML public void initialize() {
        //remove window pane menu items


        removePatientMenuItem.setOnAction(e -> {
            selectedUserType = "patient";
            clearScreen(e);
            removeUserMenu.setText("Remove Patient");
            removePane.setVisible(true);
        });

        removeDoctorMenuItem.setOnAction(e -> {
            selectedUserType = "doctor";
            clearScreen(e);
            removeUserMenu.setText("Remove Doctor");
            removePane.setVisible(true);
        });

        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        patientAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        patientEmergencyEmailColumn.setCellValueFactory(new PropertyValueFactory<>("emergencyEmail"));

        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        doctorEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        doctorSpecializationColumn.setCellValueFactory(new PropertyValueFactory<>("specialization"));

    }







    private static Alert getAlert(DuplicateUserException dupEx) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Duplicate Entry");

        if ("ID".equals(dupEx.getField())) {
            alert.setHeaderText("Duplicate ID");
            alert.setContentText("A patient with this ID already exists.\n" + dupEx.getMessage());
        } else if ("Email".equals(dupEx.getField())) {
            alert.setHeaderText("Duplicate Email");
            alert.setContentText("This email is already in use.\n" + dupEx.getMessage());
        } else {
            alert.setHeaderText("Duplicate Data");
            alert.setContentText(dupEx.getMessage());
        }
        return alert;
    }



    // Add a patient to the database
    @FXML private StackPane addPatientPane;
    @FXML private TextField patientName;
    @FXML private TextField patientId;
    @FXML private TextField patientEmail;
    @FXML private TextField patientAddress;
    @FXML private TextField patientEmergencyEmail;
    @FXML public void addPatient(ActionEvent e) {
        clearScreen(e);
        adminDetails.setVisible(false);
        addPatientPane.setVisible(true);

    }
    public void addPatientButton(ActionEvent e){
        Patient patient;
        String name = patientName.getText();
        String id = patientId.getText();
        String email = patientEmail.getText();
        String address = patientAddress.getText();
        String emergencyEmail = patientEmergencyEmail.getText();
        //checking if the user has not left any textfield empty
        //then creating an object of patient and passing to the DAOADMINISTRATOR.ADDUSER where it will be added to the database
        if(!(name.isEmpty() || id.isEmpty() || email.isEmpty() || address.isEmpty() || emergencyEmail.isEmpty())){
            patient = new Patient(name , id , email , address , emergencyEmail);
            try{
                DAOAdministrator.addUser(patient);
                //an alert screen appears so that the patient has been added sucesfully
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Patient added successfully.");
                alert.showAndWait();
            }
            catch (DuplicateUserException duplicateUserException) {
                Alert alert = getAlert(duplicateUserException);

                alert.showAndWait();
            }
            catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Unexpected Error");
                alert.setContentText("Please check your input and try again.");
                alert.showAndWait();
            }

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please fill all the fields");
        }

    }



    @FXML private StackPane addDoctorPane;
    @FXML private TextField doctorName;
    @FXML private TextField doctorId;
    @FXML private TextField doctorEmail;
    @FXML private TextField doctorSpecialization;
    @FXML public void addDoctor(ActionEvent e) {

        clearScreen(e);

        adminDetails.setVisible(false);
        addDoctorPane.setVisible(true);

    }
    @FXML public void addDoctorButton(){


        String name = doctorName.getText();
        String id = doctorId.getText();
        String email = doctorEmail.getText();
        String address = doctorSpecialization.getText();
        Doctor doctor = new Doctor(name , id , email , address);

        //checking if the user has not left any textfield empty
        //then creating an object of patient and passing to the DAOADMINISTRATOR.ADDUSER where it will be added to the database
        if(!(name.isEmpty() || id.isEmpty() || email.isEmpty() || address.isEmpty())){

            try{
                DAOAdministrator.addUser(doctor);
                //an alert screen appears so that the doctor has been added sucesfully
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Doctor added successfully.");
                alert.showAndWait();
            }
            catch (DuplicateUserException dupEx) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate Entry");

                if ("ID".equals(dupEx.getField())) {
                    alert.setHeaderText("Duplicate ID");
                    alert.setContentText("A patient with this ID already exists.\n" + dupEx.getMessage());
                } else if ("Email".equals(dupEx.getField())) {
                    alert.setHeaderText("Duplicate Email");
                    alert.setContentText("This email is already in use.\n" + dupEx.getMessage());
                } else {
                    alert.setHeaderText("Duplicate Data");
                    alert.setContentText(dupEx.getMessage());
                }

                alert.showAndWait();
            }

            catch (Exception ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Unexpected Error");
                alert.setContentText("Please check your input and try again.");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please fill all the fields");
        }


    }



    @FXML private MenuButton removeUserMenu;
    @FXML private MenuItem removePatientMenuItem;
    @FXML private MenuItem removeDoctorMenuItem;
    @FXML private TextField removeUserId;
    @FXML private TextField removeUserName;
    @FXML private TextField removeUserEmail;
    private String selectedUserType = ""; // Class-level field

    //remove doctor and patient
    //the menu item decides who gets removed
    @FXML private StackPane removePane;
    @FXML public void removeUser(ActionEvent e) {

        clearScreen(e);

        removePane.setVisible(true);

        String id = removeUserId.getText();
        String name = removeUserName.getText();
        String email = removeUserEmail.getText();
        adminDetails.setVisible(false);

        //a confirmaion alert to show that the user has been deleted succesfully
        try{
            DAOAdministrator.deleteUser(id,selectedUserType,email ,name);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User Deleted");
            alert.setHeaderText(null);
            alert.setContentText("User with ID: " + id + " has been deleted successfully.");
            alert.showAndWait();
        }
        //an alert to show that the specific user is not found
        catch (UserNotFoundException userNotFoundException) {
            Alert notFoundAlert = new Alert(Alert.AlertType.WARNING);
            notFoundAlert.setTitle("User Not Found");
            notFoundAlert.setHeaderText("No Matching User");
            notFoundAlert.setContentText(userNotFoundException.getMessage());
            notFoundAlert.showAndWait();
        }
        catch (SQLException exception) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Deletion Error");
            errorAlert.setHeaderText("Failed to Delete User");
            errorAlert.setContentText("An error occurred while trying to delete the user.");
            errorAlert.showAndWait();
            throw new RuntimeException(exception);
        }
    }


    // View a specific patient
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, String> patientNameColumn;
    @FXML private TableColumn<Patient, String> patientIdColumn;
    @FXML private TableColumn<Patient, String> patientEmailColumn;
    @FXML private TableColumn<Patient, String> patientAddressColumn;
    @FXML private TableColumn<Patient, String> patientEmergencyEmailColumn;
    @FXML public void viewPatient(ActionEvent e) {
        // Make the patient table visible
        clearScreen(e);
        adminDetails.setVisible(false);
        patientTable.setVisible(true);
        //Fectching data from the data base
        List<?> patientList = DAOAdministrator.viewAllPatients();
        List<Patient> patients =(List<Patient>) patientList ;
        ObservableList<Patient> observableList = FXCollections.observableArrayList(patients);
        patientTable.setItems(observableList);

        //viewing all the patients
    }



    //to view a specific doctor
    @FXML private TableView<Doctor> doctorTable ;
    @FXML private TableColumn<Doctor, String> doctorNameColumn;
    @FXML private TableColumn<Doctor, String> doctorIdColumn;
    @FXML private TableColumn<Doctor, String> doctorEmailColumn;
    @FXML private TableColumn<Doctor, String> doctorSpecializationColumn;
    @FXML public void viewDoctor(ActionEvent e) {
        clearScreen(e);
        adminDetails.setVisible(false);
        doctorTable.setVisible(true);
        // patientTable.setManaged(false);
        List<?> doctorList = new ArrayList<>();
        doctorList = DAOAdministrator.viewAllDoctors();
        List<Doctor> doctors =(List<Doctor>) doctorList ;
        ObservableList<Doctor> doctorObservableList = FXCollections.observableArrayList(doctors);
        doctorTable.setItems(doctorObservableList);

    }

    //go to the sign in page after confirming to logout
    @FXML public void logout(ActionEvent e) throws IOException {

        clearScreen(e);

        // Confirmation Dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Do you want to logout?");
        alert.setContentText("Press OK to logout or Cancel to stay.");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {
            // If user clicks OK, navigate to Sign In
            root = FXMLLoader.load(getClass().getResource("/com/structure/project/SignIn.fxml"));
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            // User clicked Cancel
            System.out.println("Logout canceled by the user.");
        }
    }

    //cleans all the screen
    public void clearScreen(ActionEvent e){
        patientTable.setVisible(false);
        doctorTable.setVisible(false);
        addPatientPane.setVisible(false);
        addDoctorPane.setVisible(false);
        removePane.setVisible(false);
        adminDetails.setVisible(false);
    }
    public void clearScreen1(ActionEvent e){
        patientTable.setVisible(false);
        doctorTable.setVisible(false);
        addPatientPane.setVisible(false);
        addDoctorPane.setVisible(false);
        removePane.setVisible(false);
        adminDetails.setVisible(true);
    }
    @FXML private StackPane adminDetails;
    @FXML TextField designation;
    @FXML TextField userId;
    @FXML TextField userName;
    @FXML TextField userEmail;

    @FXML public void adminDetailsButton(String admin){
        designation.setText(String.valueOf(admin));
        userId.setText(String.valueOf(administrator.getId()));
        userName.setText(String.valueOf(administrator.getName()));
        userEmail.setText(String.valueOf(administrator.getEmail()));
        adminDetails.setVisible(true);

    }

}
