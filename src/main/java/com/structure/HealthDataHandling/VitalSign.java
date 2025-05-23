    package com.structure.HealthDataHandling;

    import java.time.LocalDateTime;

    public class VitalSign {

        //attributs
        private int oxygenLevel;
        private int heartRate;
        private int diastolicPressure;
        private int systolicPressure;
        private double temp;
        private String patientId;
        private LocalDateTime dateTime;


        //default constructor
        public VitalSign() {

        }
        //parametrized constructor
        public VitalSign(String patientId, int oxygenLevel, double temp, int heartRate, int diastolicPressure, int systolicPressure) {
            this.patientId = patientId;
            this.oxygenLevel = oxygenLevel;
            this.temp = temp;
            this.heartRate = heartRate;
            this.diastolicPressure = diastolicPressure;
            this.systolicPressure = systolicPressure;
        }
  public VitalSign(String patientId, int oxygenLevel, double temp, int heartRate, int diastolicPressure, int systolicPressure,LocalDateTime dateTime) {
            this.patientId = patientId;
            this.oxygenLevel = oxygenLevel;
            this.temp = temp;
            this.heartRate = heartRate;
            this.diastolicPressure = diastolicPressure;
            this.systolicPressure = systolicPressure;
            this.dateTime = dateTime;
        }

        //setter methods
        public LocalDateTime getDateTime(){
            return dateTime ;
        }
        public void setOxygenLevel(int oxygenLevel) {
            if (oxygenLevel >= 50 && oxygenLevel <= 100) {
                this.oxygenLevel = oxygenLevel;
            } else {
                throw new IllegalArgumentException("Oxygen level must be between 50 and 100.");
            }
        }
        public void setTemp(double temp) {
            if (temp >=  20.0 && temp <=46) {
                this.temp = temp;
            } else {
                throw new IllegalArgumentException("Temperature must be between 28°C and 46.0°C .");
            }
        }
        public void setHeartRate(int heartRate) {
            if (heartRate >= 40 && heartRate <= 200) {
                this.heartRate = heartRate;
            } else {
                throw new IllegalArgumentException("Heart rate must be between 40 and 200 bpm.");
            }
        }
        public void setDiastolicPressure(int diastolicPressure){
            if(diastolicPressure>=50 && diastolicPressure<=200){
                this.diastolicPressure = diastolicPressure;
            }
            else{
                throw new IllegalArgumentException("Diastolic pressure must be between 50 and 200 mmHg.");
            }
        }
        public void setSystolicPressure(int  systolicPressure){
            if(systolicPressure>=50 && systolicPressure<=230){
                this.systolicPressure = systolicPressure;
            }
            else{
                throw new IllegalArgumentException("Systolic pressure must be between 50 and 230 mmHg.");
            }
        }
        public void setPatientId(String patientId) {
            if (patientId != null && !patientId.isBlank()) {
                this.patientId = patientId;
            } else {
                throw new IllegalArgumentException("Patient ID cannot be null or blank.");
            }
        }

        //getter methods
        public int getOxygenLevel() {
            return oxygenLevel;
        }
        public double getTemp() {
            return temp;
        }
        public int getHeartRate() {
            return heartRate;
        }
        public int getSystolicPressure(){
            return systolicPressure;
        }
        public int getDiastolicPressure(){
            return diastolicPressure;
       }
        public String getPatientId() {
            return patientId;
        }
        //display detail method
        public String detail(){
            String data = "Patient ID: "+getPatientId()+
                    "\nHeart rate: "+getHeartRate()+
                    "\nTemprature: "+getTemp()+" C"+
                    "\nSystolic Blood pressure: "+getSystolicPressure()+" mmHg"+
                    "\nDiastolic Blood pressure: "+getDiastolicPressure()+" mmHg"+
                    "\nOxygen level: "+getOxygenLevel();
            return data ;
        }
    }
