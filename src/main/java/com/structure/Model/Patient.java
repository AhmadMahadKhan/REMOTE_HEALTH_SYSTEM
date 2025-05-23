package com.structure.Model;

public class Patient extends User{


    private String address;
    private String emergencyEmail;

    public Patient(String name , String id , String email , String address,String emergencyEmail){
        //initializing parent class attributes
        super(name, id , email,address,emergencyEmail);
        this.address = address ;
        this.emergencyEmail = emergencyEmail;

    }
    public String getAddress() {
        return address;
    }
    public String getEmergencyEmail() {
        return emergencyEmail;
    }
    @Override
    public String detail() {

        return super.detail();
    }
}
