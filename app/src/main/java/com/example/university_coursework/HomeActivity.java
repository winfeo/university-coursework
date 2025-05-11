package com.example.university_coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.university_coursework.database.DatabasePatientsHelper;
import com.example.university_coursework.database.DoctorInfo;
import com.example.university_coursework.database.PatientInfo;
import com.example.university_coursework.database.StoreDatabases;
import com.example.university_coursework.fragments.HomeFragment;
import com.example.university_coursework.fragments.ProfileFragment;
import com.example.university_coursework.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import com.example.university_coursework.fragments.*;


public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;//нижнее меню навигации
    public ArrayList<PatientInfo> allPatients = new ArrayList<PatientInfo>();                 //список всех пациентов
    public ArrayList<PatientInfo> doctorsPatients = new ArrayList<PatientInfo>();  //список пациентов конкретного доктора

    public DatabasePatientsHelper dbPatientsHelper;
    public SQLiteDatabase dbPatients;
    public Cursor userCursor; //курсор для получения запросов БД пациентов


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        //создаём (загружаем) базу данных пациентов
        dbPatientsHelper = new DatabasePatientsHelper(getApplicationContext());
        dbPatientsHelper.createDatabase();

        //Не заполнять заново каждый раз
        if(StoreDatabases.getAllPatients() == null){
            fillAllPatientsArrayList();
            StoreDatabases.setAllPatients(allPatients);
        }
        if(StoreDatabases.getDoctorsPatients() == null){
            fillDoctorsPatientsArrayList();
            StoreDatabases.setDoctorsPatients(doctorsPatients);
        }


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        loadFragment(new HomeFragment()); // Загружаем сразу домашний экран

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(new HomeFragment());
                    return true;
                case R.id.navigation_search:
                    loadFragment(new SearchFragment());
                    return true;
                case R.id.navigation_profile:
                    loadFragment(new ProfileFragment());
                    return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


    //Получаем данные пациентов из БД и заполняем ArrayList всех пациентов
    private void fillAllPatientsArrayList(){
        //Получаем все записи из БД
        dbPatients = dbPatientsHelper.open();
        userCursor = dbPatients.rawQuery("select * from " + dbPatientsHelper.TITLE, null);
        //Log.d("DatabasePatientsHelper", Integer.toString(userCursor.getCount()));

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
            //Log.d("CurrentComparison", object.getLeadingPhysician() + " - " + DoctorInfo.getId());
            if(object.getLeadingPhysician().equals(DoctorInfo.getObject().getId())){
                doctorsPatients.add(object);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userCursor.close();
        dbPatients.close();
    }
}