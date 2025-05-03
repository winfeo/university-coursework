package com.example.university_coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.university_coursework.database.*;

public class MainActivity extends AppCompatActivity {
    private EditText loginText;
    private EditText passwordText;

    public DatabaseDoctorsHelper dbDoctorsHelper;
    public SQLiteDatabase dbDoctors;
    public Cursor userCursor; //курсор для получения БД докторов


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        loginText = findViewById(R.id.enterLogin);
        passwordText = findViewById(R.id.enterPassword);

        //создаём (загружаем) базу данных докторов
        dbDoctorsHelper = new DatabaseDoctorsHelper(getApplicationContext());
        dbDoctorsHelper.createDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Получаем все записи из БД
        dbDoctors = dbDoctorsHelper.open();
        userCursor = dbDoctors.rawQuery("select * from " + dbDoctorsHelper.TABLE,null);
        Log.d("DatabaseDoctorHelper", Integer.toString(userCursor.getCount()));

    }

    public void enterButton(View view){
        String inputLogin = loginText.getText().toString();
        String inputPassword = passwordText.getText().toString();

        //Проверяем, что не пустые
        if (inputLogin.isEmpty() || inputPassword.isEmpty()) {
            showError("Заполните все поля");
            return;
        }

        // Проверяем на коректный ввод (сравнение с логином и паролём из БД)
        userCursor.moveToFirst();
        while(!userCursor.isAfterLast()){
            String userLogin = userCursor.getString(userCursor.getColumnIndexOrThrow("login"));
            if(userLogin.equals(inputLogin)){
                String userPassword = userCursor.getString(userCursor.getColumnIndexOrThrow("password"));
                if(userPassword.equals(inputPassword)){
                    //получаем данные о докторе и сохраняем их в объект
                    String doctorId = userCursor.getString(userCursor.getColumnIndexOrThrow("_id"));
                    String doctorName = userCursor.getString(userCursor.getColumnIndexOrThrow("name"));
                    String doctorSurname = userCursor.getString(userCursor.getColumnIndexOrThrow("surname"));
                    String doctorFathersName = userCursor.getString(userCursor.getColumnIndexOrThrow("fathers_name"));
                    String doctorEmail = userCursor.getString(userCursor.getColumnIndexOrThrow("email"));
                    DoctorInfo doctorObject = new DoctorInfo(doctorId, doctorName, doctorSurname, doctorFathersName, doctorEmail);
                    //сохраняем экземпляр класса для использования в других активностях
                    //doctorObject.saveObject(doctorObject);

                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                    return;
                }
            }
            userCursor.moveToNext();
        }
        showError("Неверный логин или пароль");
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userCursor.close();
        dbDoctors.close();
    }
}