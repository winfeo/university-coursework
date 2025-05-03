package com.example.university_coursework;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.university_coursework.database.*;

public class ProfileFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView fio = getView().findViewById(R.id.profileFIO);
        fio.setText(DoctorInfo.getSurname() + " " + DoctorInfo.getName() + "\n" + DoctorInfo.getFathersName());

        TextView email = getView().findViewById(R.id.profileEmail);
        email.setText(DoctorInfo.getEmail());
    }

    @Override
    public void onResume() {
        super.onResume();

        LinearLayout settingsButton = getView().findViewById(R.id.settingsButton); //Новая активность
        LinearLayout appInfoButton = getView().findViewById(R.id.appInfoButton); //Диалоговое окно
        LinearLayout exitButton = getView().findViewById(R.id.exitButton); //Диалоговое окно




        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProfileSettings.class);
                startActivity(intent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Диалоговое окно с подтверждением выхода
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_exit_confirmation, null);
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .setCancelable(false)
                        .create();

                dialogView.findViewById(R.id.buttonNO).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.buttonYES).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        dialog.dismiss();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                    }

                });

                dialog.show();
            }
        });






    }
}