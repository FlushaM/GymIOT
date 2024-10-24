package com.example.gymiot.Activity;

import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gymiot.Adapter.ImageAdapter;
import com.example.gymiot.Model.Gym;
import com.example.gymiot.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GymProfileActivity extends AppCompatActivity {

    private Button reservarBtn, ubicacionBtn;
    private FirebaseFirestore db;
    private TextView gymNameTxt, mensualidadTxt, diarioTxt, maquinasTxt, horarioTxt;
    private ImageView gymProfileImage;
    private RecyclerView gymImagesRecyclerView;
    private Gym gym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_profile);

        db = FirebaseFirestore.getInstance();

        gymNameTxt = findViewById(R.id.gymNameTxt);
        mensualidadTxt = findViewById(R.id.mensualidadTxt);
        diarioTxt = findViewById(R.id.diarioTxt);
        maquinasTxt = findViewById(R.id.maquinasTxt);
        horarioTxt = findViewById(R.id.horarioTxt);
        gymProfileImage = findViewById(R.id.gymProfileImage);
        reservarBtn = findViewById(R.id.reservarBtn);
        ubicacionBtn = findViewById(R.id.ubicacionBtn);
        gymImagesRecyclerView = findViewById(R.id.gymImagesRecyclerView);

        gymImagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // Layout horizontal para las imágenes

        String gymId = getIntent().getStringExtra("gymId");

        // Configurar botón para abrir la actividad de reserva
        reservarBtn.setOnClickListener(v -> {
            if (gym != null) {
                Intent intent = new Intent(GymProfileActivity.this, ReservaActivity.class);
                intent.putExtra("gymId", gym.getId());
                startActivity(intent);
            } else {
                Toast.makeText(GymProfileActivity.this, "Gimnasio no cargado correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        // Cargar los datos del gimnasio
        if (gymId != null && !gymId.isEmpty()) {
            loadGymData(gymId);
        } else {
            Toast.makeText(this, "ID del gimnasio no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadGymData(String gymId) {
        db.collection("gyms").document(gymId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            gym = document.toObject(Gym.class);
                            if (gym != null) {
                                gym.setId(document.getId());
                                updateUI(gym);
                            }
                        } else {
                            Toast.makeText(this, "Gimnasio no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(Gym gym) {
        gymNameTxt.setText(gym.getGymName());
        mensualidadTxt.setText(gym.getMensualidad());
        diarioTxt.setText(gym.getDiario());
        horarioTxt.setText("Apertura: " + gym.getHorarioApertura() + " - Cierre: " + gym.getHorarioCierre());
        maquinasTxt.setText(String.join(", ", gym.getMaquinasDisponibles()));

        // Mostrar la imagen principal del gimnasio
        if (gym.getImageUrls() != null && !gym.getImageUrls().isEmpty()) {
            Glide.with(this).load(gym.getImageUrls().get(0)).into(gymProfileImage); // Mostrar la primera imagen como imagen principal
        }

        // Cargar las imágenes adicionales en el RecyclerView
        if (gym.getImageUrls() != null && gym.getImageUrls().size() > 1) {
            // Excluye la primera imagen ya que se está usando como la imagen principal
            ImageAdapter imageAdapter = new ImageAdapter(this, gym.getImageUrls().subList(1, gym.getImageUrls().size()));
            gymImagesRecyclerView.setAdapter(imageAdapter);
        }

        // Configurar botón de ubicación
        ubicacionBtn.setOnClickListener(v -> {
            if (gym.getLocationUrl() != null && !gym.getLocationUrl().isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(gym.getLocationUrl()));
                startActivity(intent);
            } else {
                Toast.makeText(GymProfileActivity.this, "Ubicación no disponible", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
