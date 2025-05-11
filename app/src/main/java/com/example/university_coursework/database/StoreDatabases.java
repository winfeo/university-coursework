package com.example.university_coursework.database;

import java.util.ArrayList;

//Класс для сохранения списков с объектами пациентов
//Нужен для возможности обратиться к спискам из любой части программы
public class StoreDatabases {
    private static ArrayList<PatientInfo> allPatients;
    private static ArrayList<PatientInfo> doctorsPatients;
    private static ArrayList<DoctorInfo> allDoctors;

    public static void setAllPatients(ArrayList<PatientInfo> currentAllPatients) {
        allPatients = currentAllPatients;
    }
    public static void setDoctorsPatients(ArrayList<PatientInfo> currentDoctorsPatients) {
        doctorsPatients = currentDoctorsPatients;
    }
    public static void setAllDoctors(ArrayList<DoctorInfo> currentAllDoctors) {
        allDoctors = currentAllDoctors;
    }

    public static ArrayList<PatientInfo> getAllPatients() {
        return allPatients;
    }

    public static ArrayList<PatientInfo> getDoctorsPatients() {
        return doctorsPatients;
    }

    public static ArrayList<DoctorInfo> getAllDoctors() {
        return allDoctors;
    }

    public static void clearLocaleDatabases(){
        allPatients = null;
        doctorsPatients = null;
        allDoctors = null;
    }
}
