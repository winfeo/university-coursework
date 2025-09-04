package com.example.university_coursework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.university_coursework.database.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText loginText;
    private EditText passwordText;

    public DatabaseDoctorsHelper dbDoctorsHelper;
    public SQLiteDatabase dbDoctors;
    public Cursor userCursor; //курсор для получения БД докторов

    ArrayList<DoctorInfo> allDoctors = new ArrayList<>();


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

        PreferenceManager.setDefaultValues(this, "settings", Context.MODE_PRIVATE, R.xml.settings, true);
        setContentView(R.layout.login_page);

        loginText = findViewById(R.id.enterLogin);
        passwordText = findViewById(R.id.enterPassword);

        //создаём (загружаем) базу данных докторов
        dbDoctorsHelper = new DatabaseDoctorsHelper(getApplicationContext());
        dbDoctorsHelper.createDatabase();

        //Получаем все записи из БД
        dbDoctors = dbDoctorsHelper.open();
        userCursor = dbDoctors.rawQuery("select * from " + dbDoctorsHelper.TABLE,null);
        Log.d("DatabaseDoctorHelper", Integer.toString(userCursor.getCount()));
        fillDoctorsArray();

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        if (!prefs.contains("notification_ringtone")) {
            prefs.edit().putString("notification_ringtone", getString(R.string.settings_ringtoneArray2)).apply();
        }
        if (!prefs.contains("notification_volume")) {
            prefs.edit().putString("notification_volume", getString(R.string.settings_volumeArray2)).apply();
        }

        //Выполняем автоматический вход в аккаунт, если уже авторизован
        String doctorId = prefs.getString("doctor_id", null);
        if (doctorId != null) {
            for(DoctorInfo doctor: allDoctors){
                if(doctor.getId().equals(doctorId)){
                    DoctorInfo.saveObject(doctor);
                }
            }
            // Уже авторизован, переходим сразу на главный экран
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    public void enterButton(View view){
        String inputLogin = loginText.getText().toString();
        String inputPassword = passwordText.getText().toString();

        //Проверяем, что не пустые
        if (inputLogin.isEmpty() || inputPassword.isEmpty()) {
            showError(getText(R.string.fillTheGapsToast).toString());
            return;
        }

        // Проверяем на коректный ввод (сравнение с логином и паролём из БД)
        userCursor.moveToFirst();
        while(!userCursor.isAfterLast()){
            String userLogin = userCursor.getString(userCursor.getColumnIndexOrThrow("login"));
            if(userLogin.equals(inputLogin)){
                String userPassword = userCursor.getString(userCursor.getColumnIndexOrThrow("password"));
                if(userPassword.equals(inputPassword)){
                    //сохраняем объект доктора, который входит в систему
                    // для использования в других активностях, в static поле класса
                    for(DoctorInfo doctor: allDoctors){
                        if(doctor.getId().equals(userCursor.getString(userCursor.getColumnIndexOrThrow("_id")))){
                            DoctorInfo.saveObject(doctor);
                            //сохраняем id для того, чтобы снова не авторизовываться при входе
                            SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
                            prefs.edit()
                                    .putString("doctor_id", doctor.getId()) // здесь должен быть настоящий ID врача
                                    .apply();
                        }
                    }

                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                    return;
                }
            }
            userCursor.moveToNext();
        }
        showError(getText(R.string.wrongPasswordToast).toString());
    }

    private void showError(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);

        TextView text = layout.findViewById(R.id.toast_text);
        text.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


    //Добавляем всех докторов из системы и сохраняем в списке
    private void fillDoctorsArray(){
        while(userCursor.moveToNext()){
            String doctorId = userCursor.getString(userCursor.getColumnIndexOrThrow("_id"));
            Bitmap doctorPhoto = getPatientPhoto(doctorId);
            String doctorName = userCursor.getString(userCursor.getColumnIndexOrThrow("name"));
            String doctorSurname = userCursor.getString(userCursor.getColumnIndexOrThrow("surname"));
            String doctorFathersName = userCursor.getString(userCursor.getColumnIndexOrThrow("fathers_name"));
            String doctorEmail = userCursor.getString(userCursor.getColumnIndexOrThrow("email"));
            DoctorInfo doctorObject = new DoctorInfo(doctorPhoto, doctorId, doctorName, doctorSurname, doctorFathersName, doctorEmail);

            allDoctors.add(doctorObject);
        }
        StoreDatabases.setAllDoctors(allDoctors);
    }

    private Bitmap getPatientPhoto(String id){
        String photoPath = "doctors_photo/doctor_" + id + ".jpg";
        try (InputStream inputStream = getAssets().open(photoPath)) {
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userCursor != null && !userCursor.isClosed()) {
            userCursor.close();
        }
        if (dbDoctors != null && dbDoctors.isOpen()) {
            dbDoctors.close();
        }
    }
}