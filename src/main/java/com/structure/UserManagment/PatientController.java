package com.structure.UserManagment;

import com.structure.AppointmentScheduling.Appointment;
import com.structure.AppointmentScheduling.AppointmentManager;
import com.structure.ChatVedioConsultation.ChatClient;
import com.structure.ChatVedioConsultation.ChatServer;
import com.structure.DataAccessObject.DAOAdministrator;
import com.structure.DoctorPatientInteraction.Feedback;
import com.structure.DoctorPatientInteraction.MedicalHistory;
import com.structure.DoctorPatientInteraction.Prescription;
import com.structure.EmergencyAlertSystem.EmergencyAlert;
import com.structure.EmergencyAlertSystem.PanicButton;
import com.structure.Exception.ForeignKeyViolationException;
import com.structure.Exception.NoAppointmentException;
import com.structure.Exception.UserNotFoundException;
import com.structure.HealthDataHandling.VitalSign;
import com.structure.HealthDataHandling.VitalsDatabase;
import com.structure.Model.Doctor;
import com.structure.Model.Patient;
import com.structure.NotificationReminder.ReminderService;
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
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientController  implements Initializable {

    private static int appointmentIdNumber =0 ;
    private Patient patient;

    public PatientController() {
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }


    public void panicButton(ActionEvent e){
        PanicButton.panicButton(patient);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Panic Button Pressed");
        alert.setContentText("CALLING THE EMERGENCY CONTACTS.");
        alert.showAndWait();
    }

    @FXML private BorderPane chatBox;

    @FXML private WebView vedioCall;
    private WebEngine engine;
    @Override public void initialize(URL url, ResourceBundle arg1) {

        engine = vedioCall.getEngine();
        loadPage();
        doctorId.setCellValueFactory(new PropertyValueFactory<>("id"));
        doctorName.setCellValueFactory(new PropertyValueFactory<>("name"));
        specialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));



    }
    public void loadPage() {
        engine.load("https://us05web.zoom.us/myhome");
    }


    //functionalities of adding an appointment
    @FXML private TextField doctorIdLabel;
    //    @FXML private TextField appointmentIdLabel;
    @FXML private TextField timeField;
    @FXML private DatePicker dateField;
    @FXML private StackPane addAppointmentStackPane;
    @FXML public void bookAppointmentPane(ActionEvent e) throws SQLException {
        clearPage(e);
        addAppointmentStackPane.setVisible(true);
    }
    @FXML public void bookAppointmentButton() throws SQLException, ForeignKeyViolationException {

        // Check if the time field is empty
        String timeInput = timeField.getText().trim();
        if (timeInput.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please enter a valid time.");
            alert.showAndWait();
            return;
        }

        LocalTime time;
        try {
            // Try parsing the time only if it's not empty
            time = LocalTime.parse(timeInput);
        } catch (DateTimeParseException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Time Format");
            alert.setContentText("Please enter a valid time in the format HH:mm.");
            alert.showAndWait();
            return;  // Stop further execution if the format is invalid
        }

        LocalDate date = dateField.getValue();
        if (date == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please select a date.");
            alert.showAndWait();
            return;  // Stop further execution if date is empty
        }
        if (date.isBefore(LocalDate.now())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Date");
            alert.setHeaderText("Past Date Selected");
            alert.setContentText("Please select a date that is today or in the future.");
            alert.showAndWait();
            return;
        }
        if (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Time");
            alert.setHeaderText("Past Time Selected");
            alert.setContentText("Please select a time that is later than the current time.");
            alert.showAndWait();
            return;
        }

        String doctorId = doctorIdLabel.getText().trim();
        if (doctorId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please enter a doctor ID.");
            alert.showAndWait();
            return;  // Stop further execution if doctorId is empty
        }
        int appointmentId = appointmentIdNumber;


        // Clear the page and show the appointment pane


        // Create and add the appointment
        Appointment appointment = new Appointment(patient.getId(), doctorId, date, time,appointmentId);
        AppointmentManager.addAppointment(appointment);
        ReminderService.sendAppointmentReminders(patient.getEmail() ,"Appoinment","Appointment succesfully send" +
                "The details of the appointment are:\n" +
                appointment.detail());
        ReminderService.sendAppointmentReminders( DAOAdministrator.getEmail(doctorId),"Appointment","A new appointmnet has been scheduled\n"+appointment.detail());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Appointment Booked");
        alert.setContentText("Appointment booked successfully.");
        alert.showAndWait();

    }


    @FXML private TableView<Appointment> viewAppointmentTable;
    @FXML private TableColumn<Appointment, String> doctorIdColumn;
    @FXML private TableColumn<Appointment, String> dateColumn;
    @FXML private TableColumn<Appointment, String> timeColumn;
    @FXML private TableColumn<Appointment, String> statusColumn;
    @FXML private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML public void viewAppointment(ActionEvent e) throws SQLException {

        clearPage(e);
        viewAppointmentTable.setVisible(true);

        List<Appointment> appointments = AppointmentManager.viewAppointments(patient.getId());
        System.out.println("in controller the size of appointments is " + appointments.size());

        ObservableList<Appointment> doctorObservableList = FXCollections.observableArrayList(AppointmentManager.viewAppointments(patient.getId()));
        viewAppointmentTable.setItems(doctorObservableList);
        doctorIdColumn.setCellValueFactory(new PropertyValueFactory<>("doctorId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));

    }

    @FXML private TableView<Doctor> allDoctors;
    @FXML private TableColumn<Doctor, String> specialization;
    @FXML private TableColumn<Doctor, String> doctorId;
    @FXML private TableColumn<Doctor, String> doctorName;
    @FXML public void viewDoctors(ActionEvent e) {
        clearPage(e);
        patientDetails.setVisible(false);
        allDoctors.setVisible(true);

        List<Doctor> doctors = DAOAdministrator.viewAllDoctors();
        ObservableList<Doctor> doctorObservableList = FXCollections.observableArrayList(doctors);
        allDoctors.setItems(doctorObservableList);
    }


    //patient uploading vital signs code (method)


    @FXML
    public void vitalUploadFile(ActionEvent e) {
        patientDetails.setVisible(false);
        clearPage(e);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        System.out.println("reading csv");
        File selectedFile = fileChooser.showOpenDialog(vedioCall.getScene().getWindow());
        readCsvFile(selectedFile,patient);
        System.out.println("reading csv done");
        if (selectedFile != null) {
            if (patient != null) { // <-- Make sure this is your current selected patient
                readCsvFile(selectedFile, patient);
            } else {
                showAlert(Alert.AlertType.WARNING, "No Patient Selected", "Please select a patient before uploading vitals.");
            }
        }
    }
    private void readCsvFile(File file, Patient patient) {

        setPatient(patient);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // skip header
                    continue;
                }

                String[] values = line.split(",");
                if (values.length != 5) {
                    showAlert(Alert.AlertType.ERROR, "CSV Error", "Invalid row format: " + line);
                    continue;
                }

                try {

                    VitalSign vital = new VitalSign();
                    vital.setHeartRate(Integer.parseInt(values[0]));
                    vital.setTemp(Double.parseDouble(values[1]));
                    vital.setSystolicPressure(Integer.parseInt(values[2]));
                    vital.setDiastolicPressure(Integer.parseInt(values[3]));
                    vital.setOxygenLevel(Integer.parseInt(values[4]));
                    vital.setPatientId(patient.getId());


                    VitalsDatabase.saveVitalSign(vital);
                    //if there is something abnormal then the alert is thrown
                    //within it if the patient has no appointment booked then the whole doctors gets the email
                    if(EmergencyAlert.alert(patient,vital)){
                        System.out.println("in if block patient controller");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Emergency Alert");
                        alert.setHeaderText("Emergency Alert");
                        alert.setContentText("Emergency Alert send to doctor and emergency contact.");
                        alert.showAndWait();
                        return;
                    }



                }catch (NoAppointmentException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Patient Not Found");
                    alert.setContentText("The patient has no appointments" +
                            " Sending an emergency alert to all the doctors and emergency contact.");
                    alert.showAndWait();
                    return;
                }

                catch (IllegalArgumentException ex) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Row skipped: " + ex.getMessage());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "File Error", "Could not read the file.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML private Label oxygenLevel;
    @FXML private Label heartRate;
    @FXML private Label temp;
    @FXML private Label systolicPressure;
    @FXML private Label diastolicPressure;
    @FXML private StackPane vitalPane;
    @FXML public void viewVital(ActionEvent e) {

        clearPage(e);
        patientDetails.setVisible(false);
        vitalPane.setVisible(true);

        if (patient == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No patient is set. Cannot retrieve vitals.");
            return;
        }

        VitalSign vitalSign;

        vitalSign = VitalsDatabase.getVitals(patient);
        if (vitalSign == null) {
            showAlert(Alert.AlertType.INFORMATION, "No Data", "No vital signs found for the patient.");
            return; // Exit if no vital signs are found
        }
        oxygenLevel.setText(Integer.toString(vitalSign.getOxygenLevel()));
        heartRate.setText(Integer.toString(vitalSign.getHeartRate()));
        temp.setText(Double.toString(vitalSign.getTemp()));
        systolicPressure.setText(Integer.toString(vitalSign.getSystolicPressure()));
        diastolicPressure.setText(Integer.toString(vitalSign.getDiastolicPressure()));


    }


    @FXML public void vedioCallButton(ActionEvent e) {

        clearPage(e);
        patientDetails.setVisible(false);
        vedioCall.setVisible(true);

        loadPage();
    }


    @FXML private SplitPane prescriptionPane;
    @FXML private ListView<Prescription> doctorIdListView;
    @FXML private TextArea prescriptionTextArea;
    @FXML public void viewPrescription(ActionEvent e) throws SQLException {

        clearPage(e);
        prescriptionPane.setVisible(true);
        patientDetails.setVisible(false);
        List<Prescription> prescriptions;
        prescriptions = MedicalHistory.viewPrescriptions(patient);
        ObservableList<Prescription> prescriptionObservableList = FXCollections.observableArrayList(prescriptions);
        doctorIdListView.setItems(prescriptionObservableList);
        doctorIdListView.cellFactoryProperty();
        doctorIdListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Prescription prescription, boolean empty) {
                super.updateItem(prescription, empty);
                if (empty || prescription == null) {
                    setText(null);
                } else {
                    setText("Doctor: " + prescription.getDoctorId() );
                }
            }
        });

        doctorIdListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                prescriptionTextArea.setText(newVal.getPrescriptionText());
            }
        });

    }

    @FXML private SplitPane  feedbackPane ;
    @FXML private ListView<String> DoctorListView ;
    @FXML private TextArea feedbackTextArea ;
    @FXML public void viewFeedback(ActionEvent e) throws SQLException {

        clearPage(e);
        patientDetails.setVisible(false);
        feedbackPane.setVisible(true);
        feedbackTextArea.clear();

        List<String> doctorIds = MedicalHistory.getDistinctDoctorIds(patient);
        ObservableList<String> doctorObservableList = FXCollections.observableArrayList(doctorIds);
        DoctorListView.setItems(doctorObservableList);

        DoctorListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(String doctorId, boolean empty) {
                super.updateItem(doctorId, empty);
                setText(empty || doctorId == null ? null : "Doctor: " + doctorId);
            }
        });

        DoctorListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selectedDoctorId) -> {
            if (selectedDoctorId != null) {
                try {
                    List<Feedback> feedbacks = MedicalHistory.getFeedbacksByDoctor(patient, selectedDoctorId);
                    StringBuilder builder = new StringBuilder();
                    for (Feedback f : feedbacks) {
                        builder.append("Dated: ").append(f.getDateIssued())
                                .append("\nFeedback: ").append(f.getFeedback())
                                .append("\n\n");
                    }
                    feedbackTextArea.setText(builder.toString());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


    @FXML private ListView<Doctor> senderListView;
    @FXML private TextArea messageTextArea;
    @FXML private TextField messageTextField;
    @FXML public void chatBoxButton(ActionEvent e) {
        clearPage(e);
        chatBox.setVisible(true);
        patientDetails.setVisible(false);

        // Load all doctors
        List<Doctor> doctors = DAOAdministrator.viewAllDoctors();
        ObservableList<Doctor> doctorObservableList = FXCollections.observableArrayList(doctors);
        senderListView.setItems(doctorObservableList);

        // Customize ListView cell to show doctor name
        senderListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Doctor doctor, boolean empty) {
                super.updateItem(doctor, empty);
                setText(empty || doctor == null ? null : "Doctor: " + doctor.getName());
            }
        });

        // On selecting a doctor, load chat history
        senderListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selectedDoctor) -> {
            if (selectedDoctor != null) {
                messageTextArea.clear();
                messageTextField.clear();

                try {
                    // Get full chat history between patient and selected doctor
                    List<ChatClient> chatHistory = ChatServer.getMessages(patient.getId(), selectedDoctor.getId());

                    // Format and display messages
                    StringBuilder history = new StringBuilder();
                    for (ChatClient msg : chatHistory) {
                        String sender = msg.getSender().equals(patient.getId()) ? "You ->" : "Dr. ->" + selectedDoctor.getName();
                        history.append(sender).append(msg.getMessage()).append("\n");

                    }

                    messageTextArea.setText(history.toString());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert("Failed to load messages from the server.");
                }
            }
        });

        // Send message on Enter key
        messageTextField.setOnAction(event -> {
            sendMessage(null); // trigger the same logic as the Send button
        });
    }

    @FXML
    public void sendMessage(ActionEvent e) {
        String message = messageTextField.getText().trim();
        Doctor selectedDoctor = senderListView.getSelectionModel().getSelectedItem();

        if (selectedDoctor == null) {
            showAlert("Please select a doctor.");
            return;
        }

        if (message.isEmpty()) {
            showAlert("Message cannot be empty.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        ChatClient chatClient = new ChatClient(patient.getId(), selectedDoctor.getId(), message, now);
        ChatServer.addMessage(chatClient);

        // Append message to the text area
//        String formattedMessage = "You [" + now + "]: " + message + "\n";
        String formattedMessage = "You -> " + message + "\n";
        messageTextArea.appendText(formattedMessage);
        messageTextField.clear();
    }
    private void showAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void clearPage(ActionEvent e) {


        allDoctors.setVisible(false);
        viewAppointmentTable.setVisible(false);
        prescriptionPane.setVisible(false);
        chatBox.setVisible(false);
        addAppointmentStackPane.setVisible(false);
        vedioCall.setVisible(false);
        feedbackPane.setVisible(false);
        vitalPane.setVisible(false);
        patientDetails.setVisible(false);
    }
    @FXML  public void clearPage1(ActionEvent e) {


        allDoctors.setVisible(false);
        viewAppointmentTable.setVisible(false);
        prescriptionPane.setVisible(false);
        chatBox.setVisible(false);
        addAppointmentStackPane.setVisible(false);
        vedioCall.setVisible(false);
        feedbackPane.setVisible(false);
        vitalPane.setVisible(false);
        patientDetails.setVisible(true);
    }

    private Scene scene;
    private Stage stage;
    private Parent root;

    public void logout(ActionEvent e) throws IOException {
        clearPage(e);
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


    @FXML private  StackPane patientDetails;
    @FXML TextField address;
    @FXML TextField userId;
    @FXML TextField userName;
    @FXML TextField userEmail;

    @FXML public void patientDetailsButton(){
        address.setText(patient.getAddress());
        userId.setText(String.valueOf(patient.getId()));
        userName.setText(String.valueOf(patient.getName()));
        userEmail.setText(String.valueOf(patient.getEmail()));
        patientDetails.setVisible(true);

    }
}