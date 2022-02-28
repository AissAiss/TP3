package com.example.tp3;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tp3.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    public static String NOM;
    public static String PRENOM;
    public static String DATE;
    public static String NUM;
    public static String MAIL;
    public static String INTERET;



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSoumettre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NOM = binding.editTextLastName.getText().toString();
                PRENOM = binding.editTextFirstName.getText().toString();
                DATE = binding.datePickerNaissance.getDayOfMonth() + "/" + binding.datePickerNaissance.getMonth() + "/" + binding.datePickerNaissance.getYear();
                NUM = binding.editTextNum.getText().toString();
                MAIL = binding.editTextMail.getText().toString();

                // Faire une boucle for...
                INTERET = "";

                if(binding.switchSport.isChecked()){
                    INTERET += "Sport";
                }

                if(binding.switchLecture.isChecked()){
                    if(INTERET == "")
                        INTERET += "Lecture";
                    else
                        INTERET += ", Lecture";

                }

                if(binding.switchMusique.isChecked()){
                    if(INTERET == "")
                        INTERET += "Musique ";
                    else
                        INTERET += ", Musique";

                }

                if(binding.switchSex.isChecked()){
                    if(INTERET == "")
                        INTERET += "Sex ";
                    else
                        INTERET += ", Sex";
                }



                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}