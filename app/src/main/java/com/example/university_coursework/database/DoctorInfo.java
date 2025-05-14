package com.example.university_coursework.database;

import android.graphics.Bitmap;

// Класс для хранения информации по доктору (который зашёл в приложение)
public class DoctorInfo {
    private Bitmap doctorPhoto;
    private String id;
    private String name;
    private String surname;
    private String fathersName;
    private String email;
    private static DoctorInfo doctorInfoObject;

    public DoctorInfo(Bitmap doctorPhoto, String id, String name, String surname, String fathersName, String email){
        this.doctorPhoto = doctorPhoto;
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.fathersName = fathersName;
        this.email = email;
    }

    public static void saveObject(DoctorInfo currentDoctorInfoObject){
        doctorInfoObject = currentDoctorInfoObject;
    }
    public static DoctorInfo getObject(){
        return doctorInfoObject;
    }


    public Bitmap getDoctorPhoto() { return this.doctorPhoto; }
    public String getId() { return this.id; }
    public String getName() { return this.name; }
    public String getSurname() { return this.surname; }
    public String getFathersName() { return this.fathersName; }
    public String getEmail() { return this.email; }

    public void setDoctorPhoto(Bitmap doctorPhoto) { this.doctorPhoto = doctorPhoto; }
    public void setId(String currentId) { this.id = currentId; }
    public void setName(String currentName) { this.name = currentName; }
    public void setSurname(String currentSurname) { this.surname = currentSurname; }
    public void setFathersName(String currentFathersName) { this.fathersName = currentFathersName; }
    public void setEmail(String currentEmail) { this.email = currentEmail; }
}
