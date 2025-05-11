package com.example.university_coursework.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.university_coursework.MainActivity;
import com.example.university_coursework.ProfileSettings;
import com.example.university_coursework.R;
import com.example.university_coursework.database.*;

public class ProfileFragment extends Fragment {
    LinearLayout settingsButton; //Новая активность
    LinearLayout appInfoButton; //Диалоговое окно
    LinearLayout exitButton;   //Диалоговое окно

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
        fio.setText(DoctorInfo.getObject().getSurname() + " " +
                DoctorInfo.getObject().getName() + "\n" + DoctorInfo.getObject().getFathersName());

        TextView email = getView().findViewById(R.id.profileEmail);
        email.setText(DoctorInfo.getObject().getEmail());

        settingsButton = view.findViewById(R.id.settingsButton);
        appInfoButton = view.findViewById(R.id.appInfoButton);
        exitButton = view.findViewById(R.id.exitButton);
    }

    @Override
    public void onResume() {
        super.onResume();

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ProfileSettings.class);
                startActivity(intent);
            }
        });


        appInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Диалоговое окно с информацией о приложении
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_app_info, null);
                AlertDialog dialogInfo = new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .setCancelable(false)
                        .create();

                //для отображения без зданего фона
                if (dialogInfo.getWindow() != null) {
                    dialogInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }

                dialogView.findViewById(R.id.buttonInfoConfirmation).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { dialogInfo.dismiss(); }
                });

                dialogInfo.show();
            }
        });


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Диалоговое окно с подтверждением выхода
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_exit_confirmation, null);
                AlertDialog dialogExit = new AlertDialog.Builder(getContext())
                        .setView(dialogView)
                        .setCancelable(false)
                        .create();

                //для отображения без зданего фона
                if (dialogExit.getWindow() != null) {
                    dialogExit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }

                dialogView.findViewById(R.id.buttonNO).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogExit.dismiss();
                    }
                });

                dialogView.findViewById(R.id.buttonYES).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        dialogExit.dismiss();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        StoreDatabases.clearLocaleDatabases();
                        startActivity(intent);
                        getActivity().finish();
                    }

                });

                dialogExit.show();
            }
        });


    }
}