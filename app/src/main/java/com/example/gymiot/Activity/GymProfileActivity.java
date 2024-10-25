package com.example.gymiot.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gymiot.Adapter.ImageAdapter;
import com.example.gymiot.Model.Gym;
import com.example.gymiot.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GymProfileActivity extends AppCompatActivity {

    private ImageView gymImage;
    private TextView gymNameTxt, mensualidadTxt, diarioTxt, horarioTxt, maquinasTxt;
    private Button ubicacionBtn;
    private RecyclerView additionalImagesRecyclerView;

    private FirebaseFirestore db;
    private String locationUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_profile);

        gymImage = findViewById(R.id.gymImage);
        gymNameTxt = findViewById(R.id.gymNameTxt);
        mensualidadTxt = findViewById(R.id.mensualidadTxt);
        diarioTxt = findViewById(R.id.diarioTxt);
        horarioTxt = findViewById(R.id.horarioTxt);
        maquinasTxt = findViewById(R.id.maquinasTxt);
        ubicacionBtn = findViewById(R.id.ubicacionBtn);
        additionalImagesRecyclerView = findViewById(R.id.additionalImagesRecyclerView);

        db = FirebaseFirestore.getInstance();

        String gymId = getIntent().getStringExtra("gymId");
        if (gymId != null) {
            loadGymData(gymId);
        }
    }

    private void loadGymData(String gymId) {
        db.collection("gyms").document(gymId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Gym gym = documentSnapshot.toObject(Gym.class);
                if (gym != null) {
                    gymNameTxt.setText(gym.getGymName());
                    mensualidadTxt.setText(gym.getMensualidad());
                    diarioTxt.setText(gym.getDiario());
                    horarioTxt.setText(gym.getHorarioApertura() + " - " + gym.getHorarioCierre());
                    maquinasTxt.setText(gym.getMaquinasDisponibles().toString());

                    Glide.with(this).load(gym.getImageUrl()).into(gymImage);

                    // Cargar las imágenes adicionales en el RecyclerView
                    if (gym.getAdditionalImageUrls() != null) {
                        loadAdditionalImages(gym.getAdditionalImageUrls());
                    }
                }
            }
        });
    }

    private void loadAdditionalImages(List<String> additionalImageUrls) {
        additionalImagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ImageAdapter adapter = new ImageAdapter(additionalImageUrls, this);
        additionalImagesRecyclerView.setAdapter(adapter);
    }
}
