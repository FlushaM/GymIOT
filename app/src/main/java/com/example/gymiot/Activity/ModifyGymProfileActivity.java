package com.example.gymiot.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gymiot.Model.Gym;
import com.example.gymiot.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ModifyGymProfileActivity extends AppCompatActivity {

    private EditText gymNameEdt, mensualidadEdt, diarioEdt, horarioAperturaEdt, horarioCierreEdt;
    private ImageView gymImage;
    private Button saveChangesBtn, viewReservationsBtn;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private String gymId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_gym_profile);

        gymNameEdt = findViewById(R.id.gymNameEdt);
        mensualidadEdt = findViewById(R.id.mensualidadEdt);
        diarioEdt = findViewById(R.id.diarioEdt);
        horarioAperturaEdt = findViewById(R.id.horarioAperturaEdt);
        horarioCierreEdt = findViewById(R.id.horarioCierreEdt);
        gymImage = findViewById(R.id.gymImage);
        saveChangesBtn = findViewById(R.id.saveChangesBtn);
        viewReservationsBtn = findViewById(R.id.viewReservationsBtn);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // Obtener el gymId pasado desde el perfil
        gymId = getIntent().getStringExtra("gymId");
        if (gymId != null) {
            loadGymData(gymId);
        }

        saveChangesBtn.setOnClickListener(v -> saveGymChanges());
        viewReservationsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ModifyGymProfileActivity.this, ViewReservationsActivity.class);
            intent.putExtra("gymId", gymId); // Asegúrate de pasar el gymId aquí
            startActivity(intent);
        });
    }

    private void loadGymData(String gymId) {
        db.collection("gyms").document(gymId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Gym gym = documentSnapshot.toObject(Gym.class);
                if (gym != null) {
                    gymNameEdt.setText(gym.getGymName());
                    mensualidadEdt.setText(gym.getMensualidad());
                    diarioEdt.setText(gym.getDiario());
                    horarioAperturaEdt.setText(gym.getHorarioApertura());
                    horarioCierreEdt.setText(gym.getHorarioCierre());

                    // Cargar la imagen del gimnasio
                    Glide.with(this).load(gym.getImageUrl()).into(gymImage);
                }
            }
        });
    }

    private void saveGymChanges() {
        String gymName = gymNameEdt.getText().toString().trim();
        String mensualidad = mensualidadEdt.getText().toString().trim();
        String diario = diarioEdt.getText().toString().trim();
        String horarioApertura = horarioAperturaEdt.getText().toString().trim();
        String horarioCierre = horarioCierreEdt.getText().toString().trim();

        Map<String, Object> gymData = new HashMap<>();
        gymData.put("gymName", gymName);
        gymData.put("mensualidad", mensualidad);
        gymData.put("diario", diario);
        gymData.put("horarioApertura", horarioApertura);
        gymData.put("horarioCierre", horarioCierre);

        db.collection("gyms").document(gymId).update(gymData)
                .addOnSuccessListener(aVoid -> Toast.makeText(ModifyGymProfileActivity.this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(ModifyGymProfileActivity.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show());
    }
}
