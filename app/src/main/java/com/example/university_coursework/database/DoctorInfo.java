package com.example.university_coursework.database;

// Класс для хранения информации по доктору (который зашёл в приложение)
public class DoctorInfo {
    private String id;
    private String name;
    private String surname;
    private String fathersName;
    private String email;

    public DoctorInfo(String id, String name, String surname, String fathersName, String email){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.fathersName = fathersName;
        this.email = email;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public String getFathersName() { return fathersName; }
    public String getEmail() { return email; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setFathersName(String fathersName) { this.fathersName = fathersName; }
    public void setEmail(String email) { this.email = email; }
}
