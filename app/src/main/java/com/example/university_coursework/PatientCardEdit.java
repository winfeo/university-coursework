package com.example.university_coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.university_coursework.database.*;

import java.util.ArrayList;

public class PatientCardEdit extends AppCompatActivity {
    EditText prescribedMedicationsText;
    EditText medicalHistoryText;
    ArrayList<PatientInfo> allPatients = StoreDatabases.getAllPatients();
    private boolean TEXT_CHANGED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_edithistory);

        Toolbar toolbar = findViewById(R.id.patient_ToolbarEditHistory);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getText(R.string.patient_editPatientHistoryTitle));
        }

        String prescribedMedications = getIntent().getStringExtra("patient_prescribedMedications");
        String medicalHistory = getIntent().getStringExtra("patient_medicalHistory");

        prescribedMedicationsText = findViewById(R.id.patient_prescribedMedicationsEdit);
        prescribedMedicationsText.setText(prescribedMedications);

        medicalHistoryText = findViewById(R.id.patient_medicalHistoryEdit);
        medicalHistoryText.setText(medicalHistory);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //слушатели изменения текста
        prescribedMedicationsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        medicalHistoryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Button saveButton = findViewById(R.id.patient_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                TEXT_CHANGED = false;
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(!TEXT_CHANGED){
                finish();
                return true;
            }
            else{

            }
        }
        return super.onOptionsItemSelected(item);
    }
}