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

public class DatabasePatientsHelper extends SQLiteOpenHelper {
    private static String DATABASE_PATH;  //полный путь к БД
    private static final String DATABASE_NAME = "patients.db";  //название БД
    public static final String TITLE = "patients";  //Название таблицы в БД

    //название столбцов
    /*
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_FATHERS_NAME = "fathers_name";

     */

    private Context context;

    public DatabasePatientsHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) { }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) { }

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
                Log.d("DatabasePatientsHelper", "Копирование БД успешно");
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabasePatientsHelper", ex.getMessage());
            }
        }

        Log.d("DatabasePatientsHelper", "Путь = " + DATABASE_PATH);
    }

    public SQLiteDatabase open() {
        return SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

}
