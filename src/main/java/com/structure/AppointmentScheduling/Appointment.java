package com.structure.AppointmentScheduling;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    private LocalDate date ;
    private LocalTime time ;
    private final String patientId;
    private final String doctorId ;
    private String status ;
    private int appointmentId ;

    //constructor
    public Appointment(String patientId , String doctorId , LocalDate date ,LocalTime time,int appointmentId,String status ){
        //by default status is pending ==> false
        this.status = status ;
        this.patientId = patientId ;
        this.doctorId = doctorId ;
        this.time = time;
        this.date = date;
        this.appointmentId = appointmentId ;

    } public Appointment(String patientId , String doctorId , LocalDate date ,LocalTime time,int appointmentId){
        //by default status is pending ==> false
        this.status = "pending" ;
        this.patientId = patientId ;
        this.doctorId = doctorId ;
        this.time = time;
        this.date = date;
        this.appointmentId = appointmentId ;

    }
    public int getAppointmentId(){
        return appointmentId;
    }
    public void setAppointmentId(int appointmentId){
        this.appointmentId = appointmentId ;
    }
    public  String getPatientId(){
        return patientId;
    }
    public String getDoctorId(){
        return doctorId;
    }
   public void setDate(LocalDate date){
        this.date = date ;
   }
   public void setTime(LocalTime time){
       this.time = time ;
   }
   public LocalDate getDate(){
       return date;
   }
   public LocalTime getTime(){
        return time;
   }
    public void setStatus(String status){
        this.status =  status ;
    }
    public String getStatus(){
        return  status;
    }


    //detail of appointment
    public String detail(){
        return "\n=== Appointment ===" +
                "\nPatient ID: " + patientId +
                "\nDoctor ID: " + doctorId +
                "\nDate: " + date +
                "\nTime: " + time +
                "\nStatus: " + getStatus();
    }

}
