package com.example.university_coursework;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.university_coursework.database.*;

import java.util.ArrayList;

public class PatientCard extends AppCompatActivity {
    ArrayList<PatientInfo> allPatients = StoreDatabases.getAllPatients();
    PatientInfo patient;
    TextView patient_prescribedMedications;
    TextView patient_medicalHistory;
    //private ActivityResultLauncher<Intent> activityResultLauncher; //колбек для отображение отредакт. инфы

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences prefs = newBase.getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("app_language", "ru"); // язык по умолчанию — русский
        Context context = LocaleHelper.setAppLocale(newBase, lang);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Принудительно устанавливаем светлую тему
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.patient_page);

        /*
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        patient_prescribedMedications.setText(data.getStringExtra("changed_medications"));
                        patient_medicalHistory.setText(data.getStringExtra("changed_history"));
                    }
                }
        );

         */

        //Получаем объект пациента (нажатая карточка) ???????
        //patient = (PatientInfo) getIntent().getSerializableExtra("patient_object");
        String current_patient_id = getIntent().getStringExtra("patient_id");
        for(PatientInfo current_patient : allPatients){
            if(current_patient.getId().equals(current_patient_id)){
                patient = current_patient;
            }
        }


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


    }

    @Override
    protected void onStart() {
        super.onStart();

        //Устанавливаем текст элементам карточки
        fillData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button editButton = findViewById(R.id.patient_editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientCard.this, PatientCardEdit.class);
                intent.putExtra("patient_prescribedMedications", patient.getPrescribedMedications());
                intent.putExtra("patient_medicalHistory", patient.getMedicalHistory());
                intent.putExtra("patient_id", patient.getId());
                //activityResultLauncher.launch(intent);
                startActivity(intent);
            }
        });
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


    public void fillData(){
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
        ImageView patientPhoto = findViewById(R.id.patientImage);
        patientPhoto.setImageBitmap(patient.getPhotoResource());

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

        patient_prescribedMedications = findViewById(R.id.patient_prescribedMedications);
        patient_prescribedMedications.setText(patient.getPrescribedMedications());

        patient_medicalHistory = findViewById(R.id.patient_medicalHistory);
        patient_medicalHistory.setText(patient.getMedicalHistory());

    }
}