package com.example.gymiot.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymiot.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import android.view.View;
import android.widget.AdapterView;


public class RegisterGymActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView gymProfileImage;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    // Spinners
    private Spinner spinnerPais;
    private Spinner spinnerRegion;
    private Spinner spinnerCiudad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gym);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        // Inicializar spinners
        spinnerPais = findViewById(R.id.spinnerPais);
        spinnerRegion = findViewById(R.id.spinnerRegion);
        spinnerCiudad = findViewById(R.id.spinnerCiudad);

        // Configurar adaptadores para spinners
        ArrayAdapter<CharSequence> paisAdapter = ArrayAdapter.createFromResource(this, R.array.paises, android.R.layout.simple_spinner_item);
        paisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPais.setAdapter(paisAdapter);

        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(this, R.array.regiones, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(regionAdapter);

        // Configurar el listener del spinner de regiones
        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRegion = parent.getItemAtPosition(position).toString();
                ArrayAdapter<CharSequence> ciudadAdapter;

                switch (selectedRegion) {
                    case "Atacama":
                        ciudadAdapter = ArrayAdapter.createFromResource(RegisterGymActivity.this, R.array.ciudades_atacama, android.R.layout.simple_spinner_item);
                        break;
                    case "Coquimbo":
                        ciudadAdapter = ArrayAdapter.createFromResource(RegisterGymActivity.this, R.array.ciudades_coquimbo, android.R.layout.simple_spinner_item);
                        break;
                    case "Metropolitana":
                        ciudadAdapter = ArrayAdapter.createFromResource(RegisterGymActivity.this, R.array.ciudades_metropolitana, android.R.layout.simple_spinner_item);
                        break;
                    default:
                        ciudadAdapter = ArrayAdapter.createFromResource(RegisterGymActivity.this, R.array.ciudades_atacama, android.R.layout.simple_spinner_item);
                        break;
                }

                ciudadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCiudad.setAdapter(ciudadAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        // Otros campos
        EditText gymNameEdt = findViewById(R.id.gymNameEdt);
        EditText calleEdt = findViewById(R.id.calleEdt);
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
            String ciudad = spinnerCiudad.getSelectedItem().toString(); // Obtener ciudad del spinner
            String region = spinnerRegion.getSelectedItem().toString(); // Obtener región del spinner
            String pais = spinnerPais.getSelectedItem().toString(); // Obtener país del spinner
            String calle = calleEdt.getText().toString().trim();
            String mensualidad = monthlyPriceEdt.getText().toString().trim();
            String diario = dailyPriceEdt.getText().toString().trim();
            String horario = workingHoursEdt.getText().toString().trim();

            // Lista de máquinas seleccionadas como ArrayList
            ArrayList<String> maquinasDisponibles = new ArrayList<>();
            if (checkBoxCardio.isChecked()) maquinasDisponibles.add("Sala de Cardio");
            if (checkBoxMusculacion.isChecked()) maquinasDisponibles.add("Sala de Musculación");
            if (checkBoxPesasLibres.isChecked()) maquinasDisponibles.add("Sala de Pesas Libres");
            if (checkBoxSauna.isChecked()) maquinasDisponibles.add("Sala de Sauna");
            if (checkBoxNatacion.isChecked()) maquinasDisponibles.add("Sala de Natación");

            if (imageUri != null) {
                uploadImageToFirebase(gymName, ciudad, region, pais, calle, mensualidad, diario, horario, maquinasDisponibles);
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

    private void uploadImageToFirebase(String gymName, String ciudad, String region, String pais, String calle, String mensualidad, String diario, String horario, ArrayList<String> maquinasDisponibles) {
        StorageReference fileReference = storageRef.child("gym_images/" + UUID.randomUUID().toString() + ".jpg");
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    registerGym(gymName, ciudad, region, pais, calle, mensualidad, diario, horario, maquinasDisponibles, imageUrl);
                }))
                .addOnFailureListener(e -> Toast.makeText(RegisterGymActivity.this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void registerGym(String gymName, String ciudad, String region, String pais, String calle, String mensualidad, String diario, String horario, ArrayList<String> maquinasDisponibles, String imageUrl) {
        Map<String, Object> gymData = new HashMap<>();
        gymData.put("gymName", gymName);
        gymData.put("ciudad", ciudad);
        gymData.put("region", region);
        gymData.put("pais", pais);
        gymData.put("calle", calle);
        gymData.put("mensualidad", mensualidad);
        gymData.put("diario", diario.isEmpty() ? "0" : diario);
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