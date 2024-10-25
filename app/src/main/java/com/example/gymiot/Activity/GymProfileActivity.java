package com.example.gymiot.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button ubicacionBtn, reservarBtn;  // Botón de reservar hora
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
        reservarBtn = findViewById(R.id.reservarBtn);  // Inicializar botón de reservar hora
        additionalImagesRecyclerView = findViewById(R.id.additionalImagesRecyclerView);

        db = FirebaseFirestore.getInstance();

        String gymId = getIntent().getStringExtra("gymId");
        if (gymId != null) {
            loadGymData(gymId);
        }

        // Agregar funcionalidad al botón de ubicación
        ubicacionBtn.setOnClickListener(v -> {
            if (locationUrl != null && !locationUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl));
                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // Si no hay Google Maps, abrir la URL en el navegador
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl));
                    startActivity(browserIntent);
                }
            } else {
                // Si no hay URL de ubicación disponible
                Toast.makeText(GymProfileActivity.this, "No hay ubicación disponible", Toast.LENGTH_SHORT).show();
            }
        });

        // Agregar funcionalidad al botón de Reservar Hora
        reservarBtn.setOnClickListener(v -> {
            Intent intent = new Intent(GymProfileActivity.this, ReservaActivity.class);  // Asegúrate de tener esta actividad configurada
            intent.putExtra("gymId", gymId);  // Pasar el ID del gimnasio para realizar la reserva
            startActivity(intent);
        });
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

                    // Guardar la URL de la ubicación para usarla en el botón de ubicación
                    locationUrl = gym.getLocationUrl();

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
