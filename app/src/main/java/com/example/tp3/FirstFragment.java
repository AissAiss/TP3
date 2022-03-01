package com.example.tp3;

import static java.lang.Integer.parseInt;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tp3.databinding.FragmentFirstBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    public static String NOM;
    public static String PRENOM;
    public static String DATE;
    public static String NUM;
    public static String MAIL;
    public static String INTERET;

    private long downloadID;

    // Receveur qui écoute quand le fichier est recus.
    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
                Toast.makeText(context, "Téléchargement fini", Toast.LENGTH_SHORT).show();
            }
        }
    };



    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        requireActivity().registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Regarder dans le fichier json...
        File file = new File(getContext().getFilesDir(),"saveData.json");

        if(file != null ){
            // Remplir les exit text
            try {

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

                binding.editTextNom.setText(jsonObject.get("Nom").toString());
                binding.editTextPrenom.setText(jsonObject.get("Prenom").toString());
                binding.editTextNum.setText(jsonObject.get("Num").toString());
                binding.editTextMail.setText(jsonObject.get("Mail").toString());

                // Date :
                String date[] = jsonObject.get("Date").toString().split("/");
                binding.datePickerNaissance.init(parseInt(date[2]), parseInt(date[1]), parseInt(date[0]), null);


                // Interet :
                String interets = jsonObject.get("Interet").toString();

                if(interets.contains("Sport"))
                    binding.switchSport.setChecked(true);
                else
                    binding.switchSport.setChecked(false);

                if(interets.contains("Musique"))
                    binding.switchMusique.setChecked(true);
                else
                    binding.switchMusique.setChecked(false);

                if(interets.contains("Lecture"))
                    binding.switchLecture.setChecked(true);
                else
                    binding.switchLecture.setChecked(false);

                if(interets.contains("Ski"))
                    binding.switchSki.setChecked(true);
                else
                    binding.switchSki.setChecked(false);


            }catch (Exception e) {
                e.printStackTrace();
            }


        }



        binding.btnSoumettre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NOM = binding.editTextNom.getText().toString();
                PRENOM = binding.editTextPrenom.getText().toString();
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

                if(binding.switchSki.isChecked()){
                    if(INTERET == "")
                        INTERET += "Ski ";
                    else
                        INTERET += ", Ski";
                }



                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.btnTelecharger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                } else {
                    try{
                        String getUrl = "https://image.gezondheid.be/123-kat-katje-9-8.jpg";


                        String title = URLUtil.guessFileName(getUrl, null, null);

                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getUrl));
                        request.setTitle(title);
                        request.setDescription("Downloading...");
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);

                        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                        downloadID = downloadManager.enqueue(request);
                    }catch (Exception e){
                        Log.d("DOWNLOAD", e.toString());
                    }
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