package com.example.gymiot.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymiot.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterGymActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView gymProfileImage;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gym);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        EditText gymNameEdt = findViewById(R.id.gymNameEdt);
        EditText gymLocationEdt = findViewById(R.id.gymLocationEdt);
        EditText monthlyPriceEdt = findViewById(R.id.monthlyPriceEdt);
        EditText dailyPriceEdt = findViewById(R.id.dailyPriceEdt);
        EditText workingHoursEdt = findViewById(R.id.workingHoursEdt);
        CheckBox checkBoxCardio = findViewById(R.id.checkBoxCardio);
        CheckBox checkBoxMusculacion = findViewById(R.id.checkBoxMusculacion);
        CheckBox checkBoxPesasLibres = findViewById(R.id.checkBoxPesasLibres);
        CheckBox checkBoxSauna = findViewById(R.id.checkBoxSauna);
        CheckBox checkBoxNatacion = findViewById(R.id.checkBoxNatacion);
        gymProfileImage = findViewById(R.id.gymProfileImage);

        Button uploadImageButton = findViewById(R.id.uploadImageButton);
        Button registerGymBtn = findViewById(R.id.registerGymBtn);

        uploadImageButton.setOnClickListener(v -> openFileChooser());

        registerGymBtn.setOnClickListener(v -> {
            String gymName = gymNameEdt.getText().toString().trim();
            String ubicacion = gymLocationEdt.getText().toString().trim();
            String mensualidad = monthlyPriceEdt.getText().toString().trim();
            String diario = dailyPriceEdt.getText().toString().trim();
            String horario = workingHoursEdt.getText().toString().trim();

            // Lista de máquinas seleccionadas
            Map<String, Boolean> maquinasDisponibles = new HashMap<>();
            maquinasDisponibles.put("Sala de Cardio", checkBoxCardio.isChecked());
            maquinasDisponibles.put("Sala de Musculación", checkBoxMusculacion.isChecked());
            maquinasDisponibles.put("Sala de Pesas Libres", checkBoxPesasLibres.isChecked());
            maquinasDisponibles.put("Sala de Sauna", checkBoxSauna.isChecked());
            maquinasDisponibles.put("Sala de Natación", checkBoxNatacion.isChecked());

            if (imageUri != null) {
                uploadImageToFirebase(gymName, ubicacion, mensualidad, diario, horario, maquinasDisponibles);
            } else {
                Toast.makeText(this, "Por favor, sube una imagen de perfil", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            gymProfileImage.setImageURI(imageUri);
        }
    }

    private void uploadImageToFirebase(String gymName, String ubicacion, String mensualidad, String diario, String horario, Map<String, Boolean> maquinasDisponibles) {
        StorageReference fileReference = storageRef.child("gym_images/" + UUID.randomUUID().toString() + ".jpg");
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    registerGym(gymName, ubicacion, mensualidad, diario, horario, maquinasDisponibles, imageUrl);
                }))
                .addOnFailureListener(e -> Toast.makeText(RegisterGymActivity.this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void registerGym(String gymName, String ubicacion, String mensualidad, String diario, String horario, Map<String, Boolean> maquinasDisponibles, String imageUrl) {
        Map<String, Object> gymData = new HashMap<>();
        gymData.put("gymName", gymName);
        gymData.put("ubicacion", ubicacion);
        gymData.put("mensualidad", Integer.parseInt(mensualidad));
        gymData.put("diario", diario.isEmpty() ? 0 : Integer.parseInt(diario)); // Si no se llena, se asigna 0
        gymData.put("horario", horario);
        gymData.put("maquinasDisponibles", maquinasDisponibles);
        gymData.put("imageUrl", imageUrl);

        db.collection("gyms").add(gymData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(RegisterGymActivity.this, "Gimnasio registrado con éxito", Toast.LENGTH_SHORT).show();
                    finish(); // Finaliza la actividad después de registrar el gimnasio
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterGymActivity.this, "Error al registrar el gimnasio: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
