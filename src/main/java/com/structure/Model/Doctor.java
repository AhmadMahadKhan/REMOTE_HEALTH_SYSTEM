package com.structure.Model;

public class Doctor extends User{

    private  String specialization;

    public Doctor(String name , String id , String email, String specialization  ){
        //initializing parent class attributes
        super(name, id , email);
        this.specialization = specialization ;
    }
    public String getSpecialization() {
        return specialization;
    }
    public String getId(){
        return  super.getId() ;
    }
}
