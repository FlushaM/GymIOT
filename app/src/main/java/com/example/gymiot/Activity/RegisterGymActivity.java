package com.example.gymiot.Activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymiot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RegisterGymActivity extends AppCompatActivity {

    private CheckBox checkBoxLunes, checkBoxMartes, checkBoxMiercoles, checkBoxJueves, checkBoxViernes, checkBoxSabado, checkBoxDomingo;
    private EditText openingTimeEdt, closingTimeEdt, locationUrlEdt; // Nuevo campo para la URL de Google Maps
    private static final int PICK_IMAGES_REQUEST = 2; // Corregir aquí
    private List<Uri> imageUris = new ArrayList<>(); // Lista para almacenar las imágenes seleccionadas

    private Uri selectedImageUri;
    private ImageView gymProfileImage;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private FirebaseAuth mAuth; // Para obtener el UID del usuario

    // Spinners
    private Spinner spinnerPais;
    private Spinner spinnerRegion;
    private Spinner spinnerCiudad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_gym);

        mAuth = FirebaseAuth.getInstance(); // Inicializar FirebaseAuth

        checkBoxLunes = findViewById(R.id.checkBoxLunes);
        checkBoxMartes = findViewById(R.id.checkBoxMartes);
        checkBoxMiercoles = findViewById(R.id.checkBoxMiercoles);
        checkBoxJueves = findViewById(R.id.checkBoxJueves);
        checkBoxViernes = findViewById(R.id.checkBoxViernes);
        checkBoxSabado = findViewById(R.id.checkBoxSabado);
        checkBoxDomingo = findViewById(R.id.checkBoxDomingo);
        openingTimeEdt = findViewById(R.id.openingTimeEdt);
        closingTimeEdt = findViewById(R.id.closingTimeEdt);
        locationUrlEdt = findViewById(R.id.locationUrlEdt); // Inicializar el campo de URL
        gymProfileImage = findViewById(R.id.gymProfileImage);


        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        // Configurar los EditTexts para la hora
        openingTimeEdt.setOnClickListener(v -> showTimePickerDialog(openingTimeEdt));
        closingTimeEdt.setOnClickListener(v -> showTimePickerDialog(closingTimeEdt));

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
            String ciudad = spinnerCiudad.getSelectedItem().toString();
            String region = spinnerRegion.getSelectedItem().toString();
            String pais = spinnerPais.getSelectedItem().toString();
            String calle = calleEdt.getText().toString().trim();
            String mensualidad = monthlyPriceEdt.getText().toString().trim();
            String diario = dailyPriceEdt.getText().toString().trim();
            String openingTime = openingTimeEdt.getText().toString().trim();
            String closingTime = closingTimeEdt.getText().toString().trim();
            String locationUrl = locationUrlEdt.getText().toString().trim(); // Obtener URL de ubicación

            // Días de operación seleccionados
            ArrayList<String> diasDisponibles = new ArrayList<>();
            if (checkBoxLunes.isChecked()) diasDisponibles.add("Lunes");
            if (checkBoxMartes.isChecked()) diasDisponibles.add("Martes");
            if (checkBoxMiercoles.isChecked()) diasDisponibles.add("Miércoles");
            if (checkBoxJueves.isChecked()) diasDisponibles.add("Jueves");
            if (checkBoxViernes.isChecked()) diasDisponibles.add("Viernes");
            if (checkBoxSabado.isChecked()) diasDisponibles.add("Sábado");
            if (checkBoxDomingo.isChecked()) diasDisponibles.add("Domingo");

            // Lista de máquinas seleccionadas
            ArrayList<String> maquinasDisponibles = new ArrayList<>();
            if (checkBoxCardio.isChecked()) maquinasDisponibles.add("Sala de Cardio");
            if (checkBoxMusculacion.isChecked()) maquinasDisponibles.add("Sala de Musculación");
            if (checkBoxPesasLibres.isChecked()) maquinasDisponibles.add("Sala de Pesas Libres");
            if (checkBoxSauna.isChecked()) maquinasDisponibles.add("Sala de Sauna");
            if (checkBoxNatacion.isChecked()) maquinasDisponibles.add("Sala de Natación");

            // Obtener UID del usuario actual
            String userId = mAuth.getCurrentUser().getUid();


            if (!imageUris.isEmpty()) {
                uploadImagesToFirebase(gymName, ciudad, region, pais, calle, mensualidad, diario, openingTime, closingTime, maquinasDisponibles, diasDisponibles, locationUrl, userId);
            } else {
                Toast.makeText(this, "Por favor, sube al menos una imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona imágenes"), PICK_IMAGES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            if (data.getClipData() != null) { // Si se seleccionaron múltiples imágenes
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris.add(imageUri);
                }
                gymProfileImage.setImageURI(imageUris.get(0)); // Mostrar la primera imagen seleccionada
            } else if (data.getData() != null) { // Si se seleccionó una sola imagen
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
                gymProfileImage.setImageURI(imageUri);
            }
        }
    }

    private void uploadImagesToFirebase(String gymName, String ciudad, String region, String pais, String calle, String mensualidad, String diario, String openingTime, String closingTime, ArrayList<String> maquinasDisponibles, ArrayList<String> diasDisponibles, String locationUrl, String userId) {
        List<String> imageUrls = new ArrayList<>();

        for (Uri imageUri : imageUris) {
            StorageReference fileReference = storageRef.child("gym_images/" + UUID.randomUUID().toString() + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUrls.add(uri.toString());

                        // Si todas las imágenes han sido subidas
                        if (imageUrls.size() == imageUris.size()) {
                            registerGym(gymName, ciudad, region, pais, calle, mensualidad, diario, openingTime, closingTime, maquinasDisponibles, diasDisponibles, imageUrls, locationUrl, userId);
                        }
                    }))
                    .addOnFailureListener(e -> Toast.makeText(RegisterGymActivity.this, "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
    private void registerGym(String gymName, String ciudad, String region, String pais, String calle, String mensualidad, String diario, String openingTime, String closingTime, ArrayList<String> maquinasDisponibles, ArrayList<String> diasDisponibles, List<String> imageUrls, String locationUrl, String userId) {
        Map<String, Object> gymData = new HashMap<>();
        gymData.put("gymName", gymName);
        gymData.put("ciudad", ciudad);
        gymData.put("region", region);
        gymData.put("pais", pais);
        gymData.put("calle", calle);
        gymData.put("mensualidad", mensualidad);
        gymData.put("diario", diario.isEmpty() ? "0" : diario);
        gymData.put("horarioApertura", openingTime);
        gymData.put("horarioCierre", closingTime);
        gymData.put("diasDisponibles", diasDisponibles);
        gymData.put("maquinasDisponibles", maquinasDisponibles);
        gymData.put("imageUrls", imageUrls); // Guardar lista de URLs de imágenes
        gymData.put("locationUrl", locationUrl); // Guardar la URL de ubicación
        gymData.put("ownerId", userId);

        db.collection("gyms").add(gymData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(RegisterGymActivity.this, "Gimnasio registrado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(RegisterGymActivity.this, "Error al registrar el gimnasio: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Mostrar TimePickerDialog
    private void showTimePickerDialog(final EditText timeEditText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            String time = String.format("%02d:%02d", hourOfDay, minute1);
            timeEditText.setText(time);
        }, hour, minute, true);

        timePickerDialog.show();
    }
}