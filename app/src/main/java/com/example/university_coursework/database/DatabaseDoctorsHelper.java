package com.example.university_coursework.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseDoctorsHelper extends SQLiteOpenHelper {
    private static String DATABASE_PATH; //полный путь к БД
    private static String DATABASE_NAME = "doctors.db"; //название БД
    public static final String TABLE = "doctors"; //Название таблицы в БД

    //Названия столюцов
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_SURNAME = "surname";
    static final String COLUMN_LOGIN = "login";
    static final String COLUMN_PASSWORD = "password";
    static final String COLUMN_EMAIL = "email";

    private Context context;

    public DatabaseDoctorsHelper(Context context){
        super(context, DATABASE_NAME,null,1);
        this.context = context;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    public void createDatabase(){
        File file = new File(DATABASE_PATH);
        if(!file.exists()){
            //получаем локальную бд как поток
            try(InputStream myInput = context.getAssets().open(DATABASE_NAME);
                // Открываем пустую бд
                OutputStream myOutput = new FileOutputStream(DATABASE_PATH)) {

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                Log.d("DatabaseDoctorHelper", "Копирование БД успешно");
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabaseDoctorHelper", ex.getMessage());
            }
        }

        Log.d("DatabaseDoctorHelper", "Путь = " + DATABASE_PATH);
    }

    public SQLiteDatabase open() {
        return SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
