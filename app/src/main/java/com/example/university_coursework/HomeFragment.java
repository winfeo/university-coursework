package com.example.university_coursework;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.university_coursework.database.*;

public class HomeFragment extends Fragment {
    ArrayList<PatientInfo> allPatients = new ArrayList<PatientInfo>();  //список всех пациентов
    ArrayList<PatientInfo> doctorsPatients = new ArrayList<PatientInfo>();  //список пациентов конкретного доктора

    public DatabasePatientsHelper dbPatientsHelper;
    public SQLiteDatabase dbPatients;
    public Cursor userCursor; //курсор для получения БД пациентов

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //создаём (загружаем) базу данных пациентов
        dbPatientsHelper = new DatabasePatientsHelper(getContext().getApplicationContext());
        dbPatientsHelper.createDatabase();

        fillAllPatientsArrayList();
        fillDoctorsPatientsArrayList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();

        RecyclerView recyclerView = getView().findViewById(R.id.patient_recyclerView_list);
        PatientMiniCardAdapter adapter = new PatientMiniCardAdapter(getContext(), doctorsPatients);
        recyclerView.setAdapter(adapter);

        TextView patientsNumber = getView().findViewById(R.id.patientNumber);
        patientsNumber.setText(Integer.toString(doctorsPatients.size()));

        TextView greetingPhrase = getView().findViewById(R.id.greetingPhrase);
        greetingPhrase.setText(getString(R.string.greetings) + " " + DoctorInfo.getName() + "!");

        ImageButton button = getView().findViewById(R.id.notificationButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NotificationList.class);
                startActivity(intent);
            }
        });
    }


    //Получаем данные пациентов из БД и заполняем ArrayList всех пациентов
    private void fillAllPatientsArrayList(){
        //Получаем все записи из БД
        dbPatients = dbPatientsHelper.open();
        userCursor = dbPatients.rawQuery("select * from " + dbPatientsHelper.TITLE, null);
        Log.d("DatabasePatientsHelper", Integer.toString(userCursor.getCount()));


        userCursor.moveToFirst();
        while(!userCursor.isAfterLast()){
            allPatients.add(createPatientObject(userCursor));
            userCursor.moveToNext();
        }
    }

    //Вспомогательный метод для заполения полей объекта пациента
    private PatientInfo createPatientObject(Cursor userCursor){
        String id = userCursor.getString(userCursor.getColumnIndexOrThrow("_id"));
        String name = userCursor.getString(userCursor.getColumnIndexOrThrow("name"));
        String surname = userCursor.getString(userCursor.getColumnIndexOrThrow("surname"));
        String fathersName = userCursor.getString(userCursor.getColumnIndexOrThrow("fathers_name"));
        int age = userCursor.getInt(userCursor.getColumnIndexOrThrow("age"));
        String gender = userCursor.getString(userCursor.getColumnIndexOrThrow("gender"));
        String birthDate = userCursor.getString(userCursor.getColumnIndexOrThrow("birth_date"));
        String diagnosis = userCursor.getString(userCursor.getColumnIndexOrThrow("diagnosis"));
        String policyNumber = userCursor.getString(userCursor.getColumnIndexOrThrow("policyNumber"));
        String leadingPhysician = userCursor.getString(userCursor.getColumnIndexOrThrow("leading_physician"));
        String snils = userCursor.getString(userCursor.getColumnIndexOrThrow("snils"));
        String registrationDate = userCursor.getString(userCursor.getColumnIndexOrThrow("registration_date"));
        String prescribedMedications = userCursor.getString(userCursor.getColumnIndexOrThrow("prescribed_medications"));
        String medicalHistory = userCursor.getString(userCursor.getColumnIndexOrThrow("medical_history"));


        PatientInfo newObject = new PatientInfo(
                R.drawable.ic_launcher_foreground,
                id,
                name,
                surname,
                fathersName,
                age,
                gender,
                birthDate,
                diagnosis,
                policyNumber,
                leadingPhysician,
                snils,
                registrationDate,
                prescribedMedications,
                medicalHistory
        );

        return newObject;
    }

    //Добавляем в список врача только его наблюдаемых пациентов
    public void fillDoctorsPatientsArrayList(){
        for (PatientInfo object : allPatients){
            //Log.d("CurrentСomparison", object.getLeadingPhysician() + " - " + DoctorInfo.getId());
            if(object.getLeadingPhysician().equals(DoctorInfo.getId())){
                doctorsPatients.add(object);
            }
        }
    }
}