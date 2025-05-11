package com.example.university_coursework.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.university_coursework.NotificationList;
import com.example.university_coursework.PatientCard;
import com.example.university_coursework.R;
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

        // определяем слушателя нажатия элемента в списке
        PatientMiniCardAdapter.OnStateClickListener stateClickListener = new PatientMiniCardAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(PatientInfo patient, int position) {

                Intent intent = new Intent(view.getContext(), PatientCard.class);
                intent.putExtra("patient_object", patient);
                startActivity(intent);
            }
        };

        RecyclerView recyclerView = view.findViewById(R.id.patient_recyclerView_list);
        PatientMiniCardAdapter adapter = new PatientMiniCardAdapter(getContext(), doctorsPatients, stateClickListener);
        recyclerView.setAdapter(adapter);

        TextView patientsNumber = view.findViewById(R.id.patientNumber);
        patientsNumber.setText(Integer.toString(doctorsPatients.size()));

        TextView greetingPhrase = view.findViewById(R.id.greetingPhrase);
        greetingPhrase.setText(getString(R.string.greetings) + " " + DoctorInfo.getObject().getName() + "!");
    }

    @Override
    public void onResume() {
        super.onResume();

        //Уведомления
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