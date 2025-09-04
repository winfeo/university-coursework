package com.example.university_coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.university_coursework.database.DatabasePatientsHelper;
import com.example.university_coursework.database.DoctorInfo;
import com.example.university_coursework.database.PatientInfo;
import com.example.university_coursework.database.StoreDatabases;
import com.example.university_coursework.fragments.HomeFragment;
import com.example.university_coursework.fragments.ProfileFragment;
import com.example.university_coursework.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
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
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                loadFragment(new HomeFragment());
                return true;
            } else if (id == R.id.navigation_search) {
                loadFragment(new SearchFragment());
                return true;
            } else if (id == R.id.navigation_profile) {
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
        Bitmap photoPath = getPatientPhoto(id);
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
                photoPath,
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

    private Bitmap getPatientPhoto(String id){
        String photoPath = "patients_photo/patient_" + id.substring(4) + ".jpg";
        try (InputStream inputStream = getAssets().open(photoPath)) {
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
        }
    }

    //Добавляем в список врача только его наблюдаемых пациентов
    private void fillDoctorsPatientsArrayList(){
        for (PatientInfo object : allPatients){
            //Log.d("CurrentComparison", object.getLeadingPhysician() + " - " + DoctorInfo.getId());
            if(object.getLeadingPhysician().equals(DoctorInfo.getObject().getId())){
                doctorsPatients.add(object);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userCursor != null && !userCursor.isClosed()) {
            userCursor.close();
        }
        if (dbPatients != null && dbPatients.isOpen()) {
            dbPatients.close();
        }
    }
}