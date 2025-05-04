package com.example.university_coursework.database;

//Класс для объекта мини-карточки пациента
public class PatientInfo {
    private int photoResource;
    private String id;

    private String name;
    private String surname;
    private String fathersName;

    private int age;
    private String gender;
    private String birthDate;

    private String diagnosis;
    private String policyNumber;
    private String leadingPhysician;
    private String snils;
    private String registrationDate;
    private String prescribedMedications;
    private String medicalHistory;


    public PatientInfo(int photoResource, String id, String name, String surname, String fathersName,
                       int age, String gender, String birthDate, String diagnosis, String policyNumber,
                       String leadingPhysician, String snils, String registrationDate,
                       String prescribedMedications, String medicalHistory){
        this.photoResource = photoResource;
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.fathersName = fathersName;
        this.age = age;
        this.gender = gender;
        this.birthDate = birthDate;
        this.diagnosis = diagnosis;
        this.policyNumber = policyNumber;
        this.leadingPhysician = leadingPhysician;
        this.snils = snils;
        this.registrationDate = registrationDate;
        this.prescribedMedications = prescribedMedications;
        this.medicalHistory = medicalHistory;

    }

    public int getPhotoResource() { return photoResource; }
    public String getId() {
        return id;
    }
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
    public String getBirthDate(){ return birthDate; }
    public String getDiagnosis() {
        return diagnosis;
    }
    public String getPolicyNumber() { return policyNumber; }
    public String getLeadingPhysician() { return leadingPhysician; }
    public String getSnils() { return snils; }
    public String getRegistrationDate() { return registrationDate; }
    public String getPrescribedMedications() { return prescribedMedications; }
    public String getMedicalHistory() { return medicalHistory; }


    public void setPhotoResource(int photoResource) { this.photoResource = photoResource; }
    public void setId(String id) {
        this.id = id;
    }
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
    public void setBirthDate(String birthDate){ this.birthDate = birthDate; }
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }
    public void setLeadingPhysician(String leadingPhysician) { this.leadingPhysician = leadingPhysician; }
    public void setSnils(String snils) { this.snils = snils; }
    public void setRegistrationDate(String registrationDate) {this.registrationDate = registrationDate; }
    public void setPrescribedMedications(String prescribedMedications) {this.prescribedMedications = prescribedMedications; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

}
