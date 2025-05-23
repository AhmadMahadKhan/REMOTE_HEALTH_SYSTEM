package com.structure.DoctorPatientInteraction;

import java.time.LocalDate;

public class Feedback {

    //attributes
    private String doctorId;
    private String patientId;
    private String feedback;
    private LocalDate dateIssued;

    public Feedback(String doctorId  , String patientId , String feedback){
        this.doctorId = doctorId ;
        this.patientId = patientId ;
        this.feedback = feedback ;
    }
    public Feedback(String doctorId  , String patientId , String feedback,LocalDate dateIssued){
        this.doctorId = doctorId ;
        this.patientId = patientId ;
        this.feedback = feedback ;
        this.dateIssued= dateIssued;
    }


    //    getter methods
    public LocalDate getDateIssued(){
        return dateIssued;
    }
    public String getPatientId() {
        return patientId;
    }
    public String getDoctorId(){
        return  doctorId ;
    }
    public String getFeedback() {
        return feedback;
    }

    public String detail(){
        return "\n=== Feedback ===" +
                "\nDoctor ID: " + doctorId +
                "\nPatient ID: " + patientId +
                "\nFeedback: " + feedback +
                "\nDated: "+dateIssued;
    }
}
