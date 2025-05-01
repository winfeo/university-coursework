package com.example.university_coursework;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ArrayList<PatientMiniCard> patients = new ArrayList<PatientMiniCard>();  //список карточек пациентов

    public HomeFragment() { // Отображение фрагмента
        super(R.layout.fragment_home);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRecyclerViewData(); //Метод для заполения списка пациентов
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }


    @Override
    public void onResume() {
        super.onResume();

        RecyclerView recyclerView = getView().findViewById(R.id.patient_recyclerView_list);
        PatientMiniCardAdapter adapter = new PatientMiniCardAdapter(getContext(), patients);
        recyclerView.setAdapter(adapter);

        TextView patientsNumber = getView().findViewById(R.id.patientNumber);
        patientsNumber.setText(Integer.toString(patients.size()));

        ImageButton button = getView().findViewById(R.id.notificationButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NotificationList.class);
                startActivity(intent);
            }
        });
    }

    //Здесь будет запрос к базе данных, чтобы вытащить данные пациентов
    //Возможно нужно будет передавать имя или айди врача, чтобы отфильтровать
    //в таблице нужные пациентов
    private void setRecyclerViewData(){
        patients.add(new PatientMiniCard(R.drawable.ic_launcher_foreground, "Пётр",
                "Иванов", "Пёторович", 52, "мужской",
                "Расстройство личности", "1234 1234 1234 1234", "12A-234"));

        patients.add(new PatientMiniCard(R.drawable.ic_launcher_foreground, "Илья",
                "Иванов", "Пёторович", 52, "мужской",
                "Расстройство личности", "1234 1234 1234 1234", "12A-234"));

        patients.add(new PatientMiniCard(R.drawable.ic_launcher_foreground, "Никита",
                "Иванов", "Пёторович", 52, "мужской",
                "Расстройство личности", "1234 1234 1234 1234", "12A-234"));

        patients.add(new PatientMiniCard(R.drawable.ic_launcher_foreground, "Андрей",
                "Иванов", "Пёторович", 52, "мужской",
                "Расстройство личности", "1234 1234 1234 1234", "12A-234"));

        patients.add(new PatientMiniCard(R.drawable.ic_launcher_foreground, "Александр",
                "Иванов", "Пёторович", 52, "мужской",
                "Расстройство личности", "1234 1234 1234 1234", "12A-234"));
    }
}