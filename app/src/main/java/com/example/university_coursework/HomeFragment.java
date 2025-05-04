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
    ArrayList<PatientInfo> doctorsPatients = StoreDatabases.getDoctorsPatients();//получаем список пациентов врача

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.patient_recyclerView_list);
        PatientMiniCardAdapter adapter = new PatientMiniCardAdapter(getContext(), doctorsPatients);
        recyclerView.setAdapter(adapter);

        TextView patientsNumber = view.findViewById(R.id.patientNumber);
        patientsNumber.setText(Integer.toString(doctorsPatients.size()));

        TextView greetingPhrase = view.findViewById(R.id.greetingPhrase);
        greetingPhrase.setText(getString(R.string.greetings) + " " + DoctorInfo.getName() + "!");
    }

    @Override
    public void onResume() {
        super.onResume();

        ImageButton button = getView().findViewById(R.id.notificationButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NotificationList.class);
                startActivity(intent);
            }
        });
    }


}