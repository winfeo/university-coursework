package com.example.university_coursework;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


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

    }
}