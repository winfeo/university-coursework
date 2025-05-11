package com.example.university_coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.university_coursework.database.*;

import java.util.ArrayList;

public class PatientCardEdit extends AppCompatActivity {
    EditText prescribedMedicationsText;
    EditText medicalHistoryText;
    String prescribedMedications;
    String medicalHistory;

    ArrayList<PatientInfo> allPatients = StoreDatabases.getAllPatients();
    private String PATIENT_ID;
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

        prescribedMedications = getIntent().getStringExtra("patient_prescribedMedications");
        medicalHistory = getIntent().getStringExtra("patient_medicalHistory");
        PATIENT_ID = getIntent().getStringExtra("patient_id");

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
                prescribedMedications = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                TEXT_CHANGED = true;
                turnOnButton();
            }
        });

        medicalHistoryText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                medicalHistory = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                TEXT_CHANGED = true;
                turnOnButton();
            }
        });


        Button saveButton = findViewById(R.id.patient_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChangedInDatabase();
                turnOffButton();
                TEXT_CHANGED = false;
                Toast.makeText(PatientCardEdit.this,"Данные сохранены", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(TEXT_CHANGED){
                saveChangesDialog();
            }
            else{
                saveChangedForCallback();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (TEXT_CHANGED) {
            saveChangesDialog();
        } else {
            saveChangedForCallback();
        }
    }

    private void saveChangesDialog(){
        //Диалоговое окно с информацией о приложении
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_save_changes_confirmation, null);
        AlertDialog dialogInfo = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle(getText(R.string.patient_saveTextConfirmationTitle))
                .setCancelable(false)
                .create();

        //для отображения без зданего фона
        if (dialogInfo.getWindow() != null) {
            dialogInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }




        dialogView.findViewById(R.id.buttonSAVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {






                Toast toast = Toast.makeText(PatientCardEdit.this,"Данные сохранены", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        dialogView.findViewById(R.id.buttonBACK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        dialogInfo.show();
    }

    private void saveChangedInDatabase(){

        //Обновляем данные в локальном списке
        for(PatientInfo patient : allPatients){
            if(patient.getId().equals(PATIENT_ID)){
                patient.setPrescribedMedications(prescribedMedications);
                patient.setMedicalHistory(medicalHistory);
            }
        }

        //Записываем обновления в БД
        SQLiteDatabase dbPatients = DatabasePatientsHelper.open();

        ContentValues values = new ContentValues();
        values.put("medical_history", medicalHistory);
        values.put("prescribed_medications", prescribedMedications);

        String whereClause = "_id = ?";
        String[] whereArgs = { PATIENT_ID };
        dbPatients.update(DatabasePatientsHelper.TITLE, values, whereClause, whereArgs);

        dbPatients.close();
    }

    private void saveChangedForCallback(){
        Intent intent = new Intent();
        intent.putExtra("changed_medications", prescribedMedications);
        intent.putExtra("changed_history", medicalHistory);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void turnOnButton(){
        Button buttonSAVE = findViewById(R.id.patient_saveButton);
        buttonSAVE.setClickable(true);
        buttonSAVE.setEnabled(true);
    }

    private void turnOffButton(){
        Button buttonSAVE = findViewById(R.id.patient_saveButton);
        buttonSAVE.setClickable(false);
        buttonSAVE.setEnabled(false);
    }

}