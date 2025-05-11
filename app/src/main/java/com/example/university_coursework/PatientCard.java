package com.example.university_coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.university_coursework.database.*;

import java.util.ArrayList;

public class PatientCard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_page);

        //Получаем объект пациента (нажатая карточка)
        PatientInfo patient = (PatientInfo) getIntent().getSerializableExtra("patient_object");

        Toolbar toolbar = findViewById(R.id.patientToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getText(R.string.patient_toolbarTitle));
        }

        if(patient.getLeadingPhysician().equals(DoctorInfo.getObject().getId())){
            Button editButton = findViewById(R.id.patient_editButton);
            editButton.setClickable(true);
            editButton.setVisibility(Button.VISIBLE);

        }

        //Устанавливаем текст элементам карточки
        fillData(patient);

    }

    // Нажатие на стрелку "назад"
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void fillData(PatientInfo patient){
        TextView patient_diagnosis = findViewById(R.id.patient_diagnosis);
        patient_diagnosis.setText(patient.getDiagnosis());



        String leadingPhysicianId = patient.getLeadingPhysician();
        DoctorInfo doctorObject = null;
        ArrayList<DoctorInfo> allDoctors = StoreDatabases.getAllDoctors();
        for(DoctorInfo doctor: allDoctors){
            if(doctor.getId().equals(leadingPhysicianId)){
                doctorObject = doctor;
            }
        }
        TextView patient_leadingPhysician = findViewById(R.id.patient_leadingPhysician);
        patient_leadingPhysician.setText(doctorObject.getSurname() + " " +
                doctorObject.getName().charAt(0) + ". " + doctorObject.getFathersName().charAt(0) + ".");

        TextView patient_Id = findViewById(R.id.patient_Id);
        patient_Id.setText(patient.getId());

        TextView patient_FIO = findViewById(R.id.patient_FIO);
        patient_FIO.setText(getString(R.string.patient_FIO)
                + " " + patient.getSurname() + " " + patient.getName() + " " + patient.getFathersName());

        TextView patient_gender = findViewById(R.id.patient_gender);
        patient_gender.setText(getString(R.string.gender) + " " + patient.getGender());

        TextView patient_age = findViewById(R.id.patient_age);
        patient_age.setText(getString(R.string.age) + " " + patient.getAge());

        TextView patient_birthDate = findViewById(R.id.patient_birthDate);
        patient_birthDate.setText(getString(R.string.patient_birthDate) + " " + patient.getBirthDate());

        TextView patient_snils = findViewById(R.id.patient_snils);
        patient_snils.setText(getString(R.string.patient_snils) + " "+ patient.getSnils());

        TextView patient_policyNumber = findViewById(R.id.patient_policyNumber);
        patient_policyNumber.setText(getString(R.string.oms) + " " + patient.getPolicyNumber());

        TextView patient_registrationDate = findViewById(R.id.patient_registrationDate);
        patient_registrationDate.setText(getString(R.string.patient_registrationDate) + " " + patient.getRegistrationDate());

        TextView patient_prescribedMedications = findViewById(R.id.patient_prescribedMedications);
        patient_prescribedMedications.setText(patient.getPrescribedMedications());

        TextView patient_medicalHistory = findViewById(R.id.patient_medicalHistory);
        patient_medicalHistory.setText(patient.getMedicalHistory());

    }
}