package com.example.university_coursework.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.university_coursework.NotificationList;
import com.example.university_coursework.PatientCard;
import com.example.university_coursework.R;
import com.example.university_coursework.database.*;

/**
 * Фрагмент главного экрана приложения, отображающий список пациентов врача.
 * Позволяет переходить к карточке пациента и просматривать уведомления.
 */
public class HomeFragment extends Fragment {
    ArrayList<PatientInfo> doctorsPatients = StoreDatabases.getDoctorsPatients();//получаем список пациентов врача

    /**
     * Создает и возвращает представление фрагмента
     * @param inflater LayoutInflater для создания представления из XML
     * @param container Родительский ViewGroup
     * @param savedInstanceState Сохраненное состояние
     * @return Корневое представление фрагмента
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * Вызывается после создания представления фрагмента.
     * Инициализирует RecyclerView с пациентами и другие UI элементы.
     * @param view Корневое представление фрагмента
     * @param savedInstanceState Сохраненное состояние
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * Обрабатывает нажатие на карточку пациента
         * @param patient Объект пациента
         * @param position Позиция в списке
         * @param view Нажатое представление
         */
        // слушатель нажатия элемента в списке
        PatientMiniCardAdapter.OnStateClickListener stateClickListener = new PatientMiniCardAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(PatientInfo patient, int position, View view) {
                Intent intent = new Intent(view.getContext(), PatientCard.class);
                //intent.putExtra("patient_object", patient);
                intent.putExtra("patient_id", patient.getId());
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

    /**
     * Вызывается при возобновлении работы фрагмента.
     * Настраивает кнопку уведомлений.
     */
    @Override
    public void onResume() {
        super.onResume();

        //Уведомления
        ImageButton button = getView().findViewById(R.id.notificationButton);
        button.setOnClickListener(new View.OnClickListener() {

            /**
             * Обрабатывает нажатие на кнопку уведомлений
             * @param view Нажатое представление
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NotificationList.class);
                startActivity(intent);
            }
        });
    }


}