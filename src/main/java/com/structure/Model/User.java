package com.structure.Model;


public class User {

    //attributes of the user class
    private String name;
    private String id;
    private String email;
    private String password;
    private String address;
    private String emergencyEmail;

    //constructor

    //user created by the admin

    public User(String name, String id, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = "";
    }
    public User(String name, String id, String email,String address , String emergencyEmail) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address ;
        this.emergencyEmail = emergencyEmail;
        this.password = "";
    }
    //getter methods
    public String getName() {
        return name; }
    public String getId() {
        return id ;
    }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    //setter methods
    public void setPassword(String password){ this.password= password;}

    //display user data in the console

    public String detail() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", email='" + email + '\'' +

                '}';
    }

    public static void main(String[] args) {
        User user = new User("Mahad", "123456789", "<EMAIL>");
        //System.out.println(user);
        System.out.println(user.getId());

    }


}