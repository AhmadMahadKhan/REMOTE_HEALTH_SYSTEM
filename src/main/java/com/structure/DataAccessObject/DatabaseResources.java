package com.structure.DataAccessObject;

public class DatabaseResources {



    public static void loadDriver() {
      try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver loading failed.");
            e.printStackTrace();
        }
    }
}
