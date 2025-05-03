package com.example.university_coursework.database;

// Класс для хранения информации по доктору (который зашёл в приложение)
public class DoctorInfo {
    private static String id;
    private static String name;
    private static String surname;
    private static String fathersName;
    private static String email;
    //private static DoctorInfo doctorInfoObject;

    public DoctorInfo(String id, String name, String surname, String fathersName, String email){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.fathersName = fathersName;
        this.email = email;
    }

    /*
    public static void saveObject(DoctorInfo doctorInfoObject){
        doctorInfoObject = doctorInfoObject;
    }
    public static DoctorInfo getObject(){
        return doctorInfoObject;
    }

     */

    public static String getId() { return id; }
    public static String getName() { return name; }
    public static String getSurname() { return surname; }
    public static String getFathersName() { return fathersName; }
    public static String getEmail() { return email; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setFathersName(String fathersName) { this.fathersName = fathersName; }
    public void setEmail(String email) { this.email = email; }
}
