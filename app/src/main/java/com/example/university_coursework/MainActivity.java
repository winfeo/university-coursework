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

import com.example.university_coursework.database.DatabaseDoctorsHelper;

public class MainActivity extends AppCompatActivity {
    private EditText loginText;
    private EditText passwordText;

    public DatabaseDoctorsHelper dbDoctorsHelper;
    public SQLiteDatabase dbDoctors;
    public Cursor userCursor;

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
        while(userCursor.moveToNext()){
            String userLogin = userCursor.getString(userCursor.getColumnIndexOrThrow("login"));
            if(userLogin == inputLogin){
                String userPassword = userCursor.getString(userCursor.getColumnIndexOrThrow("password"));
                if(userPassword == inputPassword){
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                }
                else showError("Неверный логин или пароль");
            }

        }


        /*
        if (inputLogin.equals(CORRECT_LOGIN) && inputPassword.equals(CORRECT_PASSWORD)) {

        } else {
            showError("Неверный логин или пароль");
        }

         */

    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}