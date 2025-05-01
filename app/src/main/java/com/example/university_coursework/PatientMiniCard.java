package com.example.university_coursework;

//Класс для объекта мини-карточки пациента
public class PatientMiniCard {
    private int photoResource;

    private String name;
    private String surname;
    private String fathersName;

    private int age;
    private String gender;
    private String diagnosis;

    private String policyNumber;
    private String id;


    public PatientMiniCard(int photoResource, String name, String surname, String fathersName,
                           int age, String gender, String diagnosis, String policyNumber, String id){
        this.photoResource = photoResource;
        this.name = name;
        this.surname = surname;
        this.fathersName = fathersName;
        this.age = age;
        this.gender = gender;
        this.diagnosis = diagnosis;
        this.policyNumber = policyNumber;
        this.id = id;
    }

    public int getPhotoResource() { return photoResource; }
    public String getName() { return name;}
    public String getSurname() {
        return surname;
    }
    public String getFathersName() {
        return fathersName;
    }
    public int getAge(){ return age; }
    public String getGender() {
        return gender;
    }
    public String getDiagnosis() {
        return diagnosis;
    }
    public String getPolicyNumber() {
        return policyNumber;
    }
    public String getId() {
        return id;
    }

    public void setPhotoResource(int photoResource) { this.photoResource = photoResource; }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }
    public void setAge(int age){
        this.age = age;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void getDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public void getPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
    public void getId(String id) {
        this.id = id;
    }
}
