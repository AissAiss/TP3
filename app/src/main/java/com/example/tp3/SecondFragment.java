package com.example.tp3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tp3.databinding.FragmentSecondBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        binding.txtNom.setText(FirstFragment.NOM);
        binding.txtPrenom.setText(FirstFragment.PRENOM);
        binding.txtDate.setText(FirstFragment.DATE);
        binding.txtNum.setText(FirstFragment.NUM);
        binding.txtMail.setText(FirstFragment.MAIL);
        binding.txtInteret.setText(FirstFragment.INTERET);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);


            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Nom", FirstFragment.NOM);
                    jsonObject.put("Prenom", FirstFragment.PRENOM);
                    jsonObject.put("Date", FirstFragment.DATE);
                    jsonObject.put("Num", FirstFragment.NUM);
                    jsonObject.put("Mail", FirstFragment.MAIL);
                    jsonObject.put("Interet", FirstFragment.INTERET);

                    // Convert JsonObject to String Format
                    String userString = jsonObject.toString();
                    // Define the File Path and its Name
                    File file = new File(getContext().getFilesDir(), "saveData.json");
                    Log.d("PATHTOJSON", getContext().getFilesDir() +  "saveData.json");

                    FileWriter fileWriter = null;

                    fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(userString);
                    bufferedWriter.close();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.btnJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = new File(getActivity().getFilesDir(),"saveData.json");
                    FileReader fileReader = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = bufferedReader.readLine();
                    while (line != null){
                        stringBuilder.append(line).append("\n");
                        line = bufferedReader.readLine();
                    }
                    bufferedReader.close();
                    // This responce will have Json Format String
                    String responce = stringBuilder.toString();


                    JSONObject jsonObject  = new JSONObject(responce);
                    //Java Object
                    Log.d("PATHTOJSON", jsonObject.get("Nom").toString());

                    Toast.makeText(getContext(), jsonObject.get("Nom").toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), jsonObject.get("Prenom").toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), jsonObject.get("Date").toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), jsonObject.get("Num").toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), jsonObject.get("Mail").toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), jsonObject.get("Interet").toString(), Toast.LENGTH_SHORT).show();

                }catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}