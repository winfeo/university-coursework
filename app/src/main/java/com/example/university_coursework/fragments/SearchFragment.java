package com.example.university_coursework.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.university_coursework.PatientCard;
import com.example.university_coursework.R;
import com.example.university_coursework.database.*;
import java.util.ArrayList;

public class SearchFragment extends Fragment implements FiltersFragment.OnFiltersAppliedListener {
    private static final String PREFS_NAME = "FilterPrefs";
    private static final String SORT_TYPE = "sortType";
    private static final String DIAGNOSIS_FILTER = "diagnosisFilter";
    private static final String DOCTOR_FILTER = "doctorFilter";

    ArrayList<PatientInfo> allPatients = StoreDatabases.getAllPatients();
    ArrayList<PatientInfo> filteredList = null;
    LinearLayout searchBar;
    ImageButton clearSearchButton;
    EditText searchId;
    PatientMiniCardAdapter adapter;
    RecyclerView allPatientsRecyclerView;
    ImageButton filterButton;
    private int currentSortType = 0;
    private String currentDiagnosisFilter = null;
    private String currentDoctorFilter = null;
    TextView filterBadge;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFilters();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PatientMiniCardAdapter.OnStateClickListener stateClickListener = (patient, position, v) -> {
            Intent intent = new Intent(v.getContext(), PatientCard.class);
            intent.putExtra("patient_id", patient.getId());
            startActivity(intent);
        };

        searchBar = view.findViewById(R.id.search_bar);
        clearSearchButton = view.findViewById(R.id.clear_search_bar);
        allPatientsRecyclerView = view.findViewById(R.id.search_recyclerView_list);
        searchId = view.findViewById(R.id.searchID);

        adapter = new PatientMiniCardAdapter(getContext(), allPatients, stateClickListener);
        allPatientsRecyclerView.setAdapter(adapter);

        filterButton = view.findViewById(R.id.filter_button);
        filterButton.setOnClickListener(v -> showFiltersDialog());

        applyFiltersAndSort();

        filterBadge = view.findViewById(R.id.filter_badge);
        updateFilterBadge();
    }

    private void showFiltersDialog() {
        FiltersFragment filtersFragment = FiltersFragment.newInstance(
                currentSortType,
                currentDiagnosisFilter,
                currentDoctorFilter
        );
        filtersFragment.setFiltersListener(this);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, filtersFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFiltersApplied(int sortType, String diagnosisFilter, String doctorFilter) {
        currentSortType = sortType;
        currentDiagnosisFilter = diagnosisFilter;
        currentDoctorFilter = doctorFilter;
        saveFilters();
        applyFiltersAndSort();
    }

    private void applyFiltersAndSort() {
        ArrayList<PatientInfo> filteredList = new ArrayList<>(allPatients);

        //Фильтрация
        if (currentDiagnosisFilter != null) {
            filteredList.removeIf(patient -> !currentDiagnosisFilter.equals(patient.getDiagnosis()));
        }

        if (currentDoctorFilter != null) {
            filteredList.removeIf(patient -> {
                String leadingPhysicianId = patient.getLeadingPhysician();
                DoctorInfo doctorObject = null;
                ArrayList<DoctorInfo> allDoctors = StoreDatabases.getAllDoctors();
                for(DoctorInfo doctor: allDoctors){
                    if(doctor.getId().equals(leadingPhysicianId)){
                        doctorObject = doctor;
                    }
                }
                if (doctorObject == null) return true;
                String fullName = doctorObject.getSurname() + " " + doctorObject.getName() + " " + doctorObject.getFathersName();
                return !currentDoctorFilter.equals(fullName);
            });
        }

        //Сортировка
        switch (currentSortType) {
            case 1: // Сортировка по дате обращения
                filteredList.sort((p1, p2) -> p1.getRegistrationDate().compareTo(p2.getRegistrationDate()));
                break;
            case 2: // Сортировка по дате рождения (возрасту)
                filteredList.sort((p1, p2) -> {
                    // Сравниваем даты рождения в обратном порядке (от старых к новым)
                    return p2.getBirthDateAsDate().compareTo(p1.getBirthDateAsDate());
                });
                break;
            default: // Сортировка по ID
                filteredList.sort((p1, p2) -> p1.getId().compareTo(p2.getId()));
        }
        adapter.updateList(filteredList);
    }

    private void saveFilters() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putInt(SORT_TYPE, currentSortType)
                .putString(DIAGNOSIS_FILTER, currentDiagnosisFilter)
                .putString(DOCTOR_FILTER, currentDoctorFilter)
                .apply();
    }

    private void loadFilters() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentSortType = prefs.getInt(SORT_TYPE, 0);
        currentDiagnosisFilter = prefs.getString(DIAGNOSIS_FILTER, null);
        currentDoctorFilter = prefs.getString(DOCTOR_FILTER, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        applyFiltersAndSort();

        searchBar.setOnClickListener(v -> {
            searchId.requestFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchId, InputMethodManager.SHOW_IMPLICIT);
        });

        searchId.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchID = s.toString();
                filteredList = new ArrayList<>();

                for (PatientInfo patient : allPatients) {
                    boolean matchesFilters = true;
                    if (currentDiagnosisFilter != null && !currentDiagnosisFilter.equals(patient.getDiagnosis())) {
                        matchesFilters = false;
                    }
                    if (currentDoctorFilter != null && !currentDoctorFilter.equals(patient.getLeadingPhysician())) {
                        matchesFilters = false;
                    }
                    boolean matchesSearch = searchID.isEmpty() || String.valueOf(patient.getId()).contains(searchID);
                    if (matchesFilters && matchesSearch) filteredList.add(patient);
                }

                applySorting(filteredList);
                adapter.updateList(filteredList);

                if(!searchID.isEmpty()){
                    clearSearchButton.setClickable(true);
                    clearSearchButton.setVisibility(View.VISIBLE);
                } else {
                    clearSearchButton.setClickable(false);
                    clearSearchButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        clearSearchButton.setOnClickListener(v -> {
            clearSearchButton.setClickable(false);
            clearSearchButton.setVisibility(View.INVISIBLE);
            searchId.setText("");
        });
    }

    private void applySorting(ArrayList<PatientInfo> list) {
        switch (currentSortType) {
            case 1:
                list.sort((p1, p2) -> p1.getRegistrationDate().compareTo(p2.getRegistrationDate()));
                break;
            case 2:
                list.sort((p1, p2) -> p2.getBirthDateAsDate().compareTo(p1.getBirthDateAsDate()));
                break;
            default:
                list.sort((p1, p2) -> p1.getId().compareTo(p2.getId()));
        }
    }

    private void updateFilterBadge() {
        int count = 0;
        if (currentDiagnosisFilter != null) count++;
        if (currentDoctorFilter != null) count++;
        if (currentSortType != 0) count++;

        if (count > 0) {
            filterBadge.setText(String.valueOf(count));
            filterBadge.setVisibility(View.VISIBLE);
        } else {
            filterBadge.setVisibility(View.GONE);
        }
    }
}



