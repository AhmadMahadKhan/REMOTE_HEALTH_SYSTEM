package com.structure.Model;

public class Administrator extends User {

    public Administrator(String name, String id, String email) {
        //initializing parent class attributes
        super(name, id, email);
    }

    public static void main(String[] args) {

        Administrator admin = new Administrator("Mahad", "129", "<EMAIL>");
        System.out.println(admin);


    }
}


