CREATE DATABASE hospitals IF NOT EXIST;
USE hospital;

CREATE TABLE chats (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       sender VARCHAR(50) NOT NULL,
                       receiver VARCHAR(50) NOT NULL,
                       timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       message TEXT NOT NULL
);
CREATE TABLE admin (
                       id VARCHAR(20) NOT NULL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100)
);
CREATE TABLE appointments (
                              id INT NOT NULL PRIMARY KEY,
                              appointment_date DATE NOT NULL,
                              appointment_time TIME NOT NULL,
                              patient_id VARCHAR(20) NOT NULL,
                              doctor_id VARCHAR(20) NOT NULL,
                              status VARCHAR(20),
                              INDEX (patient_id),
                              INDEX (doctor_id)
);
CREATE TABLE doctors (
                         name VARCHAR(30),
                         id VARCHAR(7) NOT NULL PRIMARY KEY,
                         email VARCHAR(40) UNIQUE,
                         password VARCHAR(8),
                         specialization VARCHAR(40)
);
CREATE TABLE feedback (
                          id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          doctorId VARCHAR(50) NOT NULL,
                          patientId VARCHAR(50) NOT NULL,
                          feedback TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          INDEX (doctorId),
                          INDEX (patientId)
);
CREATE TABLE patients (
                          Name VARCHAR(30),
                          id VARCHAR(7) NOT NULL PRIMARY KEY,
                          address VARCHAR(60),
                          email VARCHAR(40) UNIQUE,
                          password VARCHAR(8),
                          emergency_email VARCHAR(40)
);
CREATE TABLE prescription (
                              id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                              patientId VARCHAR(20) NOT NULL,
                              doctorId VARCHAR(20) NOT NULL,
                              medicine VARCHAR(20) NOT NULL,
                              dosage VARCHAR(20) NOT NULL,
                              schedule VARCHAR(20) NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              INDEX (patientId),
                              INDEX (doctorId)
);
CREATE TABLE vitalsigns (
                            id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            patient_id VARCHAR(7) NOT NULL,
                            oxygen_level INT NOT NULL,
                            heart_rate INT NOT NULL,
                            temperature DOUBLE NOT NULL,
                            systolic INT NOT NULL,
                            diastolic INT NOT NULL,
                            recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            INDEX (patient_id)
);
