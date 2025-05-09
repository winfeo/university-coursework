package com.example.university_coursework;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.university_coursework.database.PatientInfo;

import java.util.List;

//Адаптер для списка карточек RecyclerView
public class PatientMiniCardAdapter extends RecyclerView.Adapter<PatientMiniCardAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private List<PatientInfo> patients;

    public PatientMiniCardAdapter(Context context, List<PatientInfo> patients){
        this.patients = patients;
        this.inflater = LayoutInflater.from(context);
    }

    //Возвращает объект ViewHolder, который будет хранить данные по одному объекту PatientMiniCard
    @Override
    public PatientMiniCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.patient_card_list_item, parent, false);
        return new ViewHolder(view);
    }

    //выполняет привязку объекта ViewHolder к объекту PatientMiniCard по определенной позиции
    @Override
    public void onBindViewHolder(PatientMiniCardAdapter.ViewHolder holder, int position) {
        String age = holder.itemView.getContext().getString(R.string.age);
        String gender = holder.itemView.getContext().getString(R.string.gender);
        String diagnosis = holder.itemView.getContext().getString(R.string.diagnosis);

        PatientInfo patient = patients.get(position);
        //holder.photoResource.setImageResource(patient.getPhotoResource());
        holder.fioView.setText(patient.getSurname() + " " + patient.getName() + " " + patient.getFathersName());
        holder.ageAndGenderView.setText(age + " " + patient.getAge() + "    " + gender + " " + patient.getGender());
        holder.diagnosisView.setText(diagnosis + " " + patient.getDiagnosis());
        holder.policyNumberView.setText(String.valueOf(patient.getPolicyNumber()));
        holder.idView.setText("ID: " + patient.getId());

    }

    //Кол-во объектов в списке
    @Override
    public int getItemCount() {
        return patients.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //final ImageView photoResource;
        final TextView fioView;
        final TextView ageAndGenderView, diagnosisView;
        final TextView policyNumberView, idView;

        ViewHolder(View view){
            super(view);
            //photoResource = view.findViewById(R.id.flag);
            fioView = view.findViewById(R.id.fio_card_list_item);
            ageAndGenderView = view.findViewById(R.id.age_gender_name_card_list_item);
            diagnosisView = view.findViewById(R.id.diagnosis_name_card_list_item);
            policyNumberView = view.findViewById(R.id.policy_name_card_list_item);
            idView = view.findViewById(R.id.id_name_card_list_item);
        }
    }

    //Объект выводимого списка при поиске нужного ID
    public void updateList(List<PatientInfo> filteredList) {
        this.patients = filteredList;
        notifyDataSetChanged(); // сообщает адаптеру, что данные изменились
    }

}
