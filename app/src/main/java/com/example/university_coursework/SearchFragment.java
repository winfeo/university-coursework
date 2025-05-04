package com.example.university_coursework;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.university_coursework.HomeFragment;

public class SearchFragment extends Fragment {
    LinearLayout searchBar;         //Строка поиска
    ImageButton clearSearchButton; //Кнопка очистки поиска (крестик)
    RecyclerView allPatientsList; //список всех пациентов

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchBar = view.findViewById(R.id.search_bar);
        clearSearchButton = view.findViewById(R.id.clear_search_bar);
        allPatientsList = view.findViewById(R.id.search_recyclerView_list);

        fillAllPatientsList();

    }

    @Override
    public void onResume() {
        super.onResume();

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    //заполнение RecycleView данными
    private void fillAllPatientsList(){

    }
}