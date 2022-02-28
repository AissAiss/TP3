package com.example.tp3;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
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
                Toast.makeText(getContext(), "Téléchargement fini", Toast.LENGTH_SHORT).show();
            }
        }
    };



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