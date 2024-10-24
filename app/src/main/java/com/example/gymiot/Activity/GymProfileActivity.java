package com.example.gymiot.Activity;

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
    private RecyclerView additionalImagesRecyclerView; // RecyclerView para las imágenes adicionales

    private TextView gymNameTxt;
    private TextView horarioTxt;
    private TextView mensualidadTxt;
    private TextView diarioTxt;
    private TextView maquinasTxt;
    private TextView diasOperacionTxt;  // Nuevo campo para mostrar los días de operación
    private ImageView gymImage;

    private Gym gym;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_profile);

        db = FirebaseFirestore.getInstance();

        gymNameTxt = findViewById(R.id.gymNameTxt);
        horarioTxt = findViewById(R.id.horarioTxt);
        mensualidadTxt = findViewById(R.id.mensualidadTxt);
        diarioTxt = findViewById(R.id.diarioTxt);
        maquinasTxt = findViewById(R.id.maquinasTxt);
        diasOperacionTxt = findViewById(R.id.diasOperacionTxt);  // Inicializar nuevo campo
        gymImage = findViewById(R.id.gymImage);
        reservarBtn = findViewById(R.id.reservarBtn);

        String gymId = getIntent().getStringExtra("gymId");

        // Configurar el botón para abrir la actividad de reserva
        reservarBtn.setOnClickListener(v -> {
            if (gym != null) {
                Intent intent = new Intent(GymProfileActivity.this, ReservaActivity.class);
                intent.putExtra("gymId", gym.getId()); // Usa la instancia gym para obtener el ID
                startActivity(intent);
            } else {
                Toast.makeText(GymProfileActivity.this, "Gimnasio no cargado correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        // Verificar si el ID es nulo antes de proceder
        if (gymId == null || gymId.isEmpty()) {
            Toast.makeText(this, "ID del gimnasio no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        additionalImagesRecyclerView = findViewById(R.id.additionalImagesRecyclerView);
        additionalImagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        if (gym.getAdditionalImageUrls() != null && !gym.getAdditionalImageUrls().isEmpty()) {
            ImageAdapter imageAdapter = new ImageAdapter(this, gym.getAdditionalImageUrls());
            additionalImagesRecyclerView.setAdapter(imageAdapter);
        }



        loadGymData(gymId);
    }

    private void loadGymData(String gymId) {
        db.collection("gyms").document(gymId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Aquí se está asignando el gimnasio
                            gym = document.toObject(Gym.class);
                            if (gym != null) {
                                gym.setId(document.getId()); // Asignar el ID del documento al objeto Gym
                                updateUI(gym); // Actualizar la UI con los datos del gimnasio
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

        // Mostrar los días de operación
        if (gym.getDiasDisponibles() != null && !gym.getDiasDisponibles().isEmpty()) {
            String diasOperacion = String.join(", ", gym.getDiasDisponibles());
            horarioTxt.setText("Días de operación: " + diasOperacion + "\nApertura: " + gym.getHorarioApertura() + " - Cierre: " + gym.getHorarioCierre());
        } else {
            horarioTxt.setText("Días de operación no disponibles");
        }

        maquinasTxt.setText(String.join(", ", gym.getMaquinasDisponibles()));

        Glide.with(this)
                .load(gym.getImageUrl())
                .into(gymImage);
    }
}