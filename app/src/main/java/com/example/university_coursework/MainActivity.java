package com.example.university_coursework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final static String CORRECT_LOGIN = "admin";
    private final static String CORRECT_PASSWORD = "admin";

    private EditText loginText;
    private EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        loginText = findViewById(R.id.enterLogin);
        passwordText = findViewById(R.id.enterPassword);
    }

    public void enterButton(View view){
        String inputLogin = loginText.getText().toString();
        String inputPassword = passwordText.getText().toString();

        //Проверяем, что не пустые
        if (inputLogin.isEmpty() || inputPassword.isEmpty()) {
            showError("Заполните все поля");
            return;
        }

        // Проверяем на коректный ввод (сравнение с данными из БД)
        if (inputLogin.equals(CORRECT_LOGIN) && inputPassword.equals(CORRECT_PASSWORD)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else {
            showError("Неверный логин или пароль");
        }

    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}