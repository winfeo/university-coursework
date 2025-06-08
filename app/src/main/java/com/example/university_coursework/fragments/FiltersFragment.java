package com.example.university_coursework.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.university_coursework.R;
import com.example.university_coursework.database.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FiltersFragment extends Fragment {
    private static final String PREFS_NAME = "FilterPrefs";
    private static final String SORT_TYPE = "sortType";
    private static final String DIAGNOSIS_FILTER = "diagnosisFilter";
    private static final String DOCTOR_FILTER = "doctorFilter";

    public interface OnFiltersAppliedListener {
        void onFiltersApplied(int sortType, String diagnosisFilter, String doctorFilter);
    }

    private OnFiltersAppliedListener filtersListener;
    private RadioGroup sortRadioGroup;
    private Spinner diagnosisSpinner;
    private Spinner doctorSpinner;
    private int currentSortType = 0;
    private String currentDiagnosisFilter = null;
    private String currentDoctorFilter = null;

    public static FiltersFragment newInstance(int sortType, String diagnosis, String doctor) {
        FiltersFragment fragment = new FiltersFragment();
        Bundle args = new Bundle();
        args.putInt("sort_type", sortType);
        args.putString("diagnosis", diagnosis);
        args.putString("doctor", doctor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters, container, false);
        sortRadioGroup = view.findViewById(R.id.sort_radio_group);
        diagnosisSpinner = view.findViewById(R.id.diagnosis_spinner);
        doctorSpinner = view.findViewById(R.id.doctor_spinner);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFilters();
        setupSpinners();
        restoreSelections();
        setupButtons(view);
    }

    private void loadFilters() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        currentSortType = prefs.getInt(SORT_TYPE, 0);
        currentDiagnosisFilter = prefs.getString(DIAGNOSIS_FILTER, null);
        currentDoctorFilter = prefs.getString(DOCTOR_FILTER, null);
    }

    private void setupSpinners() {
        // Диагнозы
        Set<String> uniqueDiagnoses = new HashSet<>();
        for (PatientInfo patient : StoreDatabases.getAllPatients()) {
            if (patient.getDiagnosis() != null && !patient.getDiagnosis().isEmpty()) {
                uniqueDiagnoses.add(patient.getDiagnosis());
            }
        }

        List<String> diagnosesList = new ArrayList<>(uniqueDiagnoses);
        diagnosesList.add(0, getString(R.string.all1));
        ArrayAdapter<String> diagnosisAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, diagnosesList);
        diagnosisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diagnosisSpinner.setAdapter(diagnosisAdapter);

        // Врачи
        Map<String, String> doctorIdToNameMap = new HashMap<>();
        Set<String> uniqueDoctorNames = new HashSet<>();
        ArrayList<DoctorInfo> allDoctors = StoreDatabases.getAllDoctors();

        for (PatientInfo patient : StoreDatabases.getAllPatients()) {
            String leadingPhysicianId = patient.getLeadingPhysician();
            if (leadingPhysicianId != null && !leadingPhysicianId.isEmpty()) {
                for(DoctorInfo doctor: allDoctors) {
                    if(doctor.getId().equals(leadingPhysicianId)) {
                        String fullName = doctor.getSurname() + " " + doctor.getName() + " " + doctor.getFathersName();
                        doctorIdToNameMap.put(leadingPhysicianId, fullName);
                        uniqueDoctorNames.add(fullName);
                        break;
                    }
                }
            }
        }

        List<String> doctorsList = new ArrayList<>(uniqueDoctorNames);
        doctorsList.add(0, getString(R.string.all2));
        ArrayAdapter<String> doctorAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, doctorsList);
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpinner.setAdapter(doctorAdapter);
    }

    private void restoreSelections() {
        // Восстановление сортировки
        switch (currentSortType) {
            case 0: sortRadioGroup.check(R.id.sort_by_id); break;
            case 1: sortRadioGroup.check(R.id.sort_by_registration); break;
            case 2: sortRadioGroup.check(R.id.sort_by_birthdate); break;
        }

        // Восстановление диагноза
        if (currentDiagnosisFilter != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) diagnosisSpinner.getAdapter();
            int position = adapter.getPosition(currentDiagnosisFilter);
            if (position >= 0) diagnosisSpinner.setSelection(position);
        }

        // Восстановление врача
        if (currentDoctorFilter != null) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) doctorSpinner.getAdapter();
            int position = adapter.getPosition(currentDoctorFilter);
            if (position >= 0) doctorSpinner.setSelection(position);
        }
    }

    private void setupButtons(View view) {
        Button applyButton = view.findViewById(R.id.apply_filters_button);
        Button resetButton = view.findViewById(R.id.reset_filters_button);

        applyButton.setOnClickListener(v -> applyFilters());
        resetButton.setOnClickListener(v -> resetFilters());
    }

    private void applyFilters() {
        int selectedSortId = sortRadioGroup.getCheckedRadioButtonId();
        int sortType = selectedSortId == R.id.sort_by_id ? 0 :
                selectedSortId == R.id.sort_by_registration ? 1 : 2;

        String diagnosisFilter = diagnosisSpinner.getSelectedItemPosition() == 0 ?
                null : (String) diagnosisSpinner.getSelectedItem();

        String doctorFilter = doctorSpinner.getSelectedItemPosition() == 0 ?
                null : (String) doctorSpinner.getSelectedItem();

        if (filtersListener != null) {
            filtersListener.onFiltersApplied(sortType, diagnosisFilter, doctorFilter);
        }

        // Сохраняем настройки
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putInt(SORT_TYPE, sortType)
                .putString(DIAGNOSIS_FILTER, diagnosisFilter)
                .putString(DOCTOR_FILTER, doctorFilter)
                .apply();

        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void resetFilters() {
        sortRadioGroup.check(R.id.sort_by_id);
        diagnosisSpinner.setSelection(0);
        doctorSpinner.setSelection(0);

        // Очищаем сохраненные настройки
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putInt(SORT_TYPE, 0)
                .putString(DIAGNOSIS_FILTER, null)
                .putString(DOCTOR_FILTER, null)
                .apply();

        if (filtersListener != null) {
            filtersListener.onFiltersApplied(0, null, null);
        }

        requireActivity().getSupportFragmentManager().popBackStack();
    }

    public void setFiltersListener(OnFiltersAppliedListener listener) {
        this.filtersListener = listener;
    }
}