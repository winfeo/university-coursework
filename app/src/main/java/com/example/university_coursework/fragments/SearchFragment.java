package com.example.university_coursework.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.university_coursework.PatientCard;
import com.example.university_coursework.R;
import com.example.university_coursework.database.*;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    ArrayList<PatientInfo> allPatients = StoreDatabases.getAllPatients(); //Список всех пациентов
    ArrayList<PatientInfo> filteredList = null; //список отфильтрованных пациентов

    LinearLayout searchBar;         //Строка поиска
    ImageButton clearSearchButton; //Кнопка очистки поиска (крестик)
    EditText searchId;           //EditText искомого ID
    PatientMiniCardAdapter adapter;
    RecyclerView allPatientsRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        searchBar = view.findViewById(R.id.search_bar);
        clearSearchButton = view.findViewById(R.id.clear_search_bar);
        allPatientsRecyclerView = view.findViewById(R.id.search_recyclerView_list);
        searchId = view.findViewById(R.id.searchID);

        //Log.d("SearchFragment", "allPatients size = " + allPatients.size());
        adapter = new PatientMiniCardAdapter(getContext(), allPatients, stateClickListener);
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


        //Фильтр вводимого текста
        searchId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchID = s.toString();
                filteredList = new ArrayList<>();
                for (PatientInfo patient : allPatients) {
                    if (String.valueOf(patient.getId()).contains(searchID)) {
                        filteredList.add(patient);
                    }
                }

                //adapter = new PatientMiniCardAdapter(getContext(), filteredList);
                //allPatientsRecyclerView.setAdapter(adapter);
                adapter.updateList(filteredList);

                //Кнопка очистки вводимых данных
                if(!searchID.isEmpty()){
                    clearSearchButton.setClickable(true);
                    clearSearchButton.setVisibility(View.VISIBLE);
                }
                else{
                    clearSearchButton.setClickable(false);
                    clearSearchButton.setVisibility(View.INVISIBLE);
                }

                clearSearchButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSearchButton.setClickable(false);
                        clearSearchButton.setVisibility(View.INVISIBLE);
                        searchId.setText("");
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

}