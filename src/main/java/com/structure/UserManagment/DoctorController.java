package com.structure.UserManagment;

import com.structure.AppointmentScheduling.Appointment;
import com.structure.AppointmentScheduling.AppointmentManager;
import com.structure.ChatVedioConsultation.ChatClient;
import com.structure.ChatVedioConsultation.ChatServer;
import com.structure.DataAccessObject.DAOAdministrator;
import com.structure.DoctorPatientInteraction.Feedback;
import com.structure.DoctorPatientInteraction.MedicalHistory;
import com.structure.DoctorPatientInteraction.Prescription;
import com.structure.EmergencyAlertSystem.NotificationService;
import com.structure.HealthDataHandling.VitalSign;
import com.structure.HealthDataHandling.VitalsDatabase;
import com.structure.Model.Doctor;
import com.structure.Model.Patient;
import com.structure.NotificationReminder.ReminderService;
import com.structure.PDF.VitalsPDFExporter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class DoctorController implements Initializable {

    private Doctor doctor ;
    public void setDoctor(Doctor doctor) {
        this.doctor  = doctor ;
    }
    public void initialize() {}
    @FXML public void initialize(URL url, ResourceBundle arg1) {

        engine = vedioCall.getEngine();
        loadPage();
    }
    @FXML private BorderPane chatBox;
    @FXML private Pane feedbackPane;


    @FXML StackPane doctorDetails;



    @FXML private SplitPane patientVitalsPane;
    @FXML private ListView<String> patientListView;
    @FXML private TextArea vitalsTextArea;
    @FXML public void viewPatientVitals(ActionEvent e) {
        clearScreen(e);
        doctorDetails.setVisible(false);
        patientVitalsPane.setVisible(true);
        vitalsTextArea.clear();
        patientListView.getItems().clear();
        List<Patient> patients ;

        try {
            patients = AppointmentManager.getPatientData(doctor);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("Database connection error.");
            alert.showAndWait();
            return;
        }

        // Convert patient data to strings for display
        ObservableList<String> patientDisplayList = FXCollections.observableArrayList();
        for (Patient patient : patients) {
            patientDisplayList.add(patient.getName() + " (" + patient.getId() + ")");
        }

        // Set the list to the ListView
        patientListView.setItems(patientDisplayList);

        // Add listener to display vitals when a patient is selected
        patientListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Extract patient ID from "Name (ID)"
                String patientId = newVal.substring(newVal.indexOf('(') + 1, newVal.indexOf(')'));
                System.out.println("calling vitals");
                String vitals = VitalsDatabase.getPatientsVital(patientId); // You must implement this
                vitalsTextArea.setText(vitals);
            }
        });
    }



    @FXML private TableView<Appointment> viewAppointmentTable;
    @FXML private TableColumn<Appointment ,String> doctorNameColumn ;
    @FXML private TableColumn<Appointment ,String> patientNameColumn ;
    @FXML private TableColumn<Appointment , LocalDate> dateColumn ;
    @FXML private TableColumn<Appointment ,Boolean> statusColumn ;
    @FXML private TableColumn<Appointment , LocalTime> timeColumn ;
    @FXML public void viewAllAppointments(ActionEvent e){
        clearScreen(e);
        doctorDetails.setVisible(false);
        viewAppointmentTable.setVisible(true);

        List<Appointment> appointments = AppointmentManager.viewAppointmentsDoctor(doctor);
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointments);
        viewAppointmentTable.setItems(observableList);

        //setting the columns of the table
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

    }




    private Appointment selectedAppointment;
    @FXML private StackPane checkAppointmentStackPane;
    @FXML private ListView<Appointment> appointmentValidationListView; //  ListView of Appointment
    @FXML private TextArea appointmentDetail;
    @FXML private Button confirmAppointmentButton;
    @FXML private Button rejectAppointmentButton;
    private String formatAppointmentDetails(Appointment appointment) {
        return "Patient ID: " + appointment.getPatientId() + "\n"
                +"Appointment Id: " + appointment.getAppointmentId() + "\n"
                + "Date: " + appointment.getDate() + "\n"
                + "Time: " + appointment.getTime() + "\n"
                + "Status: " + appointment.getStatus();
    }
    @FXML public void viewCheckAppointmentPane(ActionEvent e) {
        clearScreen(e);
        doctorDetails.setVisible(false);
        checkAppointmentStackPane.setVisible(true);

        List<Appointment> appointments = AppointmentManager.viewAppointmentsDoctor(doctor);
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointments);
        appointmentValidationListView.setItems(observableList);

        appointmentValidationListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Appointment appointment, boolean empty) {
                super.updateItem(appointment, empty);
                if (empty || appointment == null) {
                    setText(null);
                } else {
                    setText("Patient ID: " + appointment.getPatientId() + " | Appointment Id: " + appointment.getAppointmentId());
                }
            }
        });


        appointmentValidationListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedAppointment = newVal;
            if (newVal != null) {
                appointmentDetail.setText(formatAppointmentDetails(newVal));
                confirmAppointmentButton.setDisable(false);
                rejectAppointmentButton.setDisable(false);
            } else {
                appointmentDetail.clear();
                confirmAppointmentButton.setDisable(true);
                rejectAppointmentButton.setDisable(true);
            }
        });

        confirmAppointmentButton.setDisable(true);
        rejectAppointmentButton.setDisable(true);
    }

    private void refreshAppointmentList() {
        List<Appointment> appointments = AppointmentManager.viewAppointmentsDoctor(doctor);
        ObservableList<Appointment> observableList = FXCollections.observableArrayList(appointments);
        appointmentValidationListView.setItems(observableList);

        // Apply proper cell factory to show Patient ID and Status
        appointmentValidationListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Appointment appointment, boolean empty) {
                super.updateItem(appointment, empty);
                if (empty || appointment == null) {
                    setText(null);
                } else {
                    setText("Patient ID: " + appointment.getPatientId() + " | Appointment Id: " + appointment.getAppointmentId());
                }
            }
        });

        // Clear selection and disable buttons
        appointmentValidationListView.getSelectionModel().clearSelection();
        appointmentDetail.clear();
        selectedAppointment = null;
        confirmAppointmentButton.setDisable(true);
        rejectAppointmentButton.setDisable(true);
    }
    @FXML public void confirmAppointment(ActionEvent e) throws SQLException {
        if (selectedAppointment != null) {
            AppointmentManager.updateDecision(selectedAppointment, "Accepted");

            ReminderService.sendAppointmentReminders(DAOAdministrator.getEmail(selectedAppointment.getPatientId()),"Appointment Update",selectedAppointment.detail()+"\nAPPOINTMENT ACCEPTED");
            checkAppointmentStackPane.setVisible(false);
            refreshAppointmentList();

        }
    }
    @FXML public void rejectAppointment(ActionEvent e) throws SQLException {
        if (selectedAppointment != null) {
            AppointmentManager.updateDecision(selectedAppointment, "Rejected");
            ReminderService.sendAppointmentReminders(DAOAdministrator.getEmail(selectedAppointment.getPatientId()),"Appointment Update",selectedAppointment.detail()+"\nAPPOINTMENT REJECTED");
            checkAppointmentStackPane.setVisible(false);
            refreshAppointmentList();
        }
    }



    private WebEngine engine;
    @FXML private WebView vedioCall;
    public void loadPage(){
        engine.load("https://us05web.zoom.us/myhome");
//        engine.load("https://web.whatsapp.com/");
    }
    @FXML public void vedioCallButton(ActionEvent e){
        clearScreen(e);
        doctorDetails.setVisible(false);
        vedioCall.setVisible(true);
        loadPage();
    }


    @FXML private StackPane prescritionPane;
    @FXML private TextField precriptionPatientId;
    @FXML private TextField medicineTextField;
    @FXML private TextField dosageTextField;
    @FXML private TextField scheduleTextField;
    @FXML public void uploadPrescitpionButton(ActionEvent e) throws SQLException {

        clearScreen(e);
        doctorDetails.setVisible(false);
        prescritionPane.setVisible(true);
    }
    @FXML public void uploadPrescitpionConfirmButton(ActionEvent e) throws SQLException {

        String patientId = precriptionPatientId.getText();
        String medicine = medicineTextField.getText();
        String dosage = dosageTextField.getText();
        String schedule = scheduleTextField.getText();
        Prescription prescription = new Prescription(medicine,dosage,schedule,patientId,doctor.getId());
        MedicalHistory.addPrescription(prescription);
        System.out.println("Prescription added.");

    }


    @FXML private TextField feedbackPatientId;
    @FXML private TextArea patientFeedback;
    @FXML public void uploadFeedbackButton(ActionEvent e) throws SQLException {
        clearScreen(e);
        doctorDetails.setVisible(false);
        feedbackPane.setVisible(true);
    }
    @FXML public void uploadFeedbackConfirmButton(ActionEvent e) throws SQLException {

        String patientId = feedbackPatientId.getText();
        String feedbackTxt= patientFeedback.getText();

        Feedback feedbackObj = new Feedback(doctor.getId() ,patientId ,feedbackTxt);
        MedicalHistory.addFeedback(feedbackObj);
    }


    @FXML private ListView<Patient> receiverListView;
    @FXML private TextArea doctorMessageTextArea;
    @FXML private TextField doctorMessageTextField;
    @FXML public void chatBoxButtonDoctor(ActionEvent e) {
        clearScreen(e);
        doctorDetails.setVisible(false);
        chatBox.setVisible(true); // Show chat box area in Doctor UI

        // Load all patients
        List<Patient> patients = DAOAdministrator.viewAllPatients();
        ObservableList<Patient> patientObservableList = FXCollections.observableArrayList(patients);
        receiverListView.setItems(patientObservableList);

        // Customize ListView to show patient names
        receiverListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Patient patient, boolean empty) {
                super.updateItem(patient, empty);
                setText(empty || patient == null ? null : "Patient: " + patient.getName());
            }
        });

        // Load chat history when a patient is selected
        receiverListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, selectedPatient) -> {
                    if (selectedPatient != null) {
                        doctorMessageTextArea.clear();
                        doctorMessageTextField.clear();

                        try {
                            List<ChatClient> chatHistory = ChatServer.getMessages(doctor.getId(), selectedPatient.getId());

                            StringBuilder history = new StringBuilder();
                            for (ChatClient msg : chatHistory) {
                                String sender = msg.getSender().equals(doctor.getId()) ? "You ->" : "Patient -> " + selectedPatient.getName();
                                history.append(sender)
                                        .append(msg.getMessage()).append("\n");
                            }

                            doctorMessageTextArea.setText(history.toString());
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            showAlert("Failed to load messages.","");
                        }
                    }
                });

        // Send message on Enter key
        doctorMessageTextField.setOnAction(event -> sendMessageDoctor(null));
    }
    @FXML public void sendMessageDoctor(ActionEvent e) {
        String message = doctorMessageTextField.getText().trim();
        Patient selectedPatient = receiverListView.getSelectionModel().getSelectedItem();

        if (selectedPatient == null) {
            showAlert("Please select a patient.","");
            return;
        }

        if (message.isEmpty()) {
            showAlert("Message cannot be empty.","Message cannot be empty.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        ChatClient chatClient = new ChatClient(doctor.getId(), selectedPatient.getId(), message,now);
        ChatServer.addMessage(chatClient);


//        String formattedMessage = "You [" + now + "]: " + message + "\n";
        String formattedMessage = "You -> " + message + "\n";
        doctorMessageTextArea.appendText(formattedMessage);

        doctorMessageTextField.clear();
    }


    @FXML private TableView<Patient> patientTableView;
    @FXML private  TableColumn<Patient,String> patientIdColumn;
    @FXML private  TableColumn<Patient , String > ViewPatientNameColumn;
    @FXML private  TableColumn<Patient , String > patientEmailColumn;
    @FXML public void viewPatientsButton(ActionEvent e)  {
        clearScreen(e);
        doctorDetails.setVisible(false);
        patientTableView.setVisible(true);
        List<Patient> patients;
        try{
            patients = AppointmentManager.getPatientData(doctor);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("Database connection error.");
            alert.showAndWait();
            patients = new ArrayList<>();
            System.out.println("Database connection error.");
            ex.printStackTrace();
        }

        ObservableList<Patient> observableList = FXCollections.observableArrayList(patients);
        patientTableView.setItems(observableList);
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        ViewPatientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        patientEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML StackPane pdfStackPane;
    @FXML ListView<String> pdfListView;
    @FXML void distictPatients(ActionEvent e) {
        clearScreen(e);
        doctorDetails.setVisible(false);
        pdfStackPane.setVisible(true);
        try {
            List<String> patientIds = VitalsDatabase.getPatientsForPDF();
            pdfListView.getItems().setAll(patientIds); // populate ListView
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("Error", "Failed to load patient IDs.");
        }
    }
    @FXML void handleDownloadVitalsPDF(ActionEvent event) {
        try {
            String selectedId = pdfListView.getSelectionModel().getSelectedItem();
            if (selectedId == null) {
                showAlert("No Selection", "Please select a patient from the list.");
                return;
            }

            List<VitalSign> vitals = VitalsDatabase.getVitalsForPDF(selectedId);
            if (vitals.isEmpty()) {
                showAlert("No Data", "No vitals available for this patient.");
                return;
            }

            String path = System.getProperty("user.home") + "/Downloads/vitals_report_" + selectedId + ".pdf";
            VitalsPDFExporter.exportVitalsToPDF(selectedId, vitals, path);
            showAlert("Success", "PDF saved to Downloads!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to generate PDF.");
        }
    }

    public void clearScreen(ActionEvent e){
        feedbackPane.setVisible(false);
        patientTableView.setVisible(false);
        patientVitalsPane.setVisible(false);
        prescritionPane.setVisible(false);
        viewAppointmentTable.setVisible(false);
        checkAppointmentStackPane.setVisible(false);
        vedioCall.setVisible(false);
        chatBox.setVisible(false);
        pdfStackPane.setVisible(false);
        doctorDetails.setVisible(true);
    }

    private Scene scene;
    private Stage stage;
    private Parent root;
    public void logout(ActionEvent e) throws IOException {
        clearScreen(e);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("You will be redirected to the login page.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
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

    @FXML private TextField userName;
    @FXML private TextField userId;
    @FXML private TextField specialization;
    @FXML private TextField userEmail;
    @FXML public void setDoctorDetails(){

        userName.setText(doctor.getName());
        userId.setText(doctor.getId());
        specialization.setText(doctor.getSpecialization());
        userEmail.setText(doctor.getEmail());
    }

}