package com.example.university_coursework;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.university_coursework.database.*;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    ArrayList<PatientInfo> allPatients = StoreDatabases.getAllPatients();

    LinearLayout searchBar;         //Строка поиска
    ImageButton clearSearchButton; //Кнопка очистки поиска (крестик)
    RecyclerView allPatientsRecyclerView; //список всех пациентов
    EditText searchId;           //текст искомого ID

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
        allPatientsRecyclerView = view.findViewById(R.id.search_recyclerView_list);
        searchId = view.findViewById(R.id.searchID);

        Log.d("SearchFragment", "allPatients size = " + allPatients.size());
        PatientMiniCardAdapter adapter = new PatientMiniCardAdapter(getContext(), allPatients);
        allPatientsRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchId.requestFocus();

                //Показать клавиатуру при нажатии на поисковую область
                InputMethodManager showKeyboard = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                showKeyboard.showSoftInput(searchId, InputMethodManager.SHOW_IMPLICIT);
            }
        });

    }

}