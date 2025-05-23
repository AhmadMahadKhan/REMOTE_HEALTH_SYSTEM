package com.structure.DoctorPatientInteraction;

import java.time.LocalDate;

public class Prescription {
    private String medicine ;
    private String dosage ;
    private String schedule ;
    private String patientId;
    private String doctorId;
    private LocalDate dateIssued;

    //constructor
    public Prescription(String medicine, String dosage, String schedule, String patientId, String doctorId){
        this.medicine = medicine;
        this.dosage = dosage;
        this.schedule = schedule;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }
    public Prescription(String medicine, String dosage, String schedule, String patientId, String doctorId,LocalDate dateIssued){
        this.medicine = medicine;
        this.dosage = dosage;
        this.schedule = schedule;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateIssued = dateIssued;
    }
    //getter methods
    public LocalDate getDateIssued(){
        return dateIssued;
    }
    public String getMedicine(){
        return medicine;
    }
    public String getDosage(){
        return dosage;
    }
    public String getSchedule(){
        return schedule;
    }
    public String getPatientId(){
        return patientId;
    }
    public String getDoctorId(){
        return doctorId;
    }
    public String getPrescriptionText() {
        return "Medicine: " + medicine + "\nDosage: " + dosage + "\nSchedule: " + schedule +"\nDated: "+dateIssued;
    }

}
