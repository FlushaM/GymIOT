package com.example.gymiot.Fragment;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gymiot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AgregarFragment extends Fragment {

    private CheckBox checkBoxLunes, checkBoxMartes, checkBoxMiercoles, checkBoxJueves, checkBoxViernes, checkBoxSabado, checkBoxDomingo;
    private EditText openingTimeEdt, closingTimeEdt;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageView gymProfileImage;
    private FirebaseFirestore db;
    private StorageReference storageRef;
    private FirebaseAuth mAuth;

    private Spinner spinnerPais;
    private Spinner spinnerRegion;
    private Spinner spinnerCiudad;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agregar, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        checkBoxLunes = view.findViewById(R.id.checkBoxLunes);
        checkBoxMartes = view.findViewById(R.id.checkBoxMartes);
        checkBoxMiercoles = view.findViewById(R.id.checkBoxMiercoles);
        checkBoxJueves = view.findViewById(R.id.checkBoxJueves);
        checkBoxViernes = view.findViewById(R.id.checkBoxViernes);
        checkBoxSabado = view.findViewById(R.id.checkBoxSabado);
        checkBoxDomingo = view.findViewById(R.id.checkBoxDomingo);
        openingTimeEdt = view.findViewById(R.id.openingTimeEdt);
        closingTimeEdt = view.findViewById(R.id.closingTimeEdt);
        gymProfileImage = view.findViewById(R.id.gymProfileImage);

        openingTimeEdt.setOnClickListener(v -> showTimePickerDialog(openingTimeEdt));
        closingTimeEdt.setOnClickListener(v -> showTimePickerDialog(closingTimeEdt));

        spinnerPais = view.findViewById(R.id.spinnerPais);
        spinnerRegion = view.findViewById(R.id.spinnerRegion);
        spinnerCiudad = view.findViewById(R.id.spinnerCiudad);

        ArrayAdapter<CharSequence> paisAdapter = ArrayAdapter.createFromResource(getContext(), R.array.paises, android.R.layout.simple_spinner_item);
        paisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPais.setAdapter(paisAdapter);

        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(getContext(), R.array.regiones, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(regionAdapter);

        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> ciudadAdapter;

                switch (parent.getItemAtPosition(position).toString()) {
                    case "Atacama":
                        ciudadAdapter = ArrayAdapter.createFromResource(getContext(), R.array.ciudades_atacama, android.R.layout.simple_spinner_item);
                        break;
                    case "Coquimbo":
                        ciudadAdapter = ArrayAdapter.createFromResource(getContext(), R.array.ciudades_coquimbo, android.R.layout.simple_spinner_item);
                        break;
                    case "Metropolitana":
                        ciudadAdapter = ArrayAdapter.createFromResource(getContext(), R.array.ciudades_metropolitana, android.R.layout.simple_spinner_item);
                        break;
                    default:
                        ciudadAdapter = ArrayAdapter.createFromResource(getContext(), R.array.ciudades_atacama, android.R.layout.simple_spinner_item);
                        break;
                }

                ciudadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCiudad.setAdapter(ciudadAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action
            }
        });

        Button uploadImageButton = view.findViewById(R.id.uploadImageButton);
        Button registerGymBtn = view.findViewById(R.id.registerGymBtn);

        uploadImageButton.setOnClickListener(v -> openFileChooser());
        registerGymBtn.setOnClickListener(v -> registerGym());

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            gymProfileImage.setImageURI(imageUri);
        }
    }

    private void showTimePickerDialog(final EditText timeEditText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute1) -> {
            String time = String.format("%02d:%02d", hourOfDay, minute1);
            timeEditText.setText(time);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void registerGym() {
        String gymName = ((EditText) getView().findViewById(R.id.gymNameEdt)).getText().toString().trim();
        String ciudad = spinnerCiudad.getSelectedItem().toString();
        String region = spinnerRegion.getSelectedItem().toString();
        String pais = spinnerPais.getSelectedItem().toString();
        String calle = ((EditText) getView().findViewById(R.id.calleEdt)).getText().toString().trim();
        String mensualidad = ((EditText) getView().findViewById(R.id.monthlyPriceEdt)).getText().toString().trim();
        String diario = ((EditText) getView().findViewById(R.id.dailyPriceEdt)).getText().toString().trim();
        String openingTime = openingTimeEdt.getText().toString().trim();
        String closingTime = closingTimeEdt.getText().toString().trim();

        ArrayList<String> diasDisponibles = new ArrayList<>();
        if (checkBoxLunes.isChecked()) diasDisponibles.add("Lunes");
        if (checkBoxMartes.isChecked()) diasDisponibles.add("Martes");
        if (checkBoxMiercoles.isChecked()) diasDisponibles.add("Miércoles");
        if (checkBoxJueves.isChecked()) diasDisponibles.add("Jueves");
        if (checkBoxViernes.isChecked()) diasDisponibles.add("Viernes");
        if (checkBoxSabado.isChecked()) diasDisponibles.add("Sábado");
        if (checkBoxDomingo.isChecked()) diasDisponibles.add("Domingo");

        ArrayList<String> maquinasDisponibles = new ArrayList<>();
        if (((CheckBox) getView().findViewById(R.id.checkBoxCardio)).isChecked()) maquinasDisponibles.add("Sala de Cardio");
        if (((CheckBox) getView().findViewById(R.id.checkBoxMusculacion)).isChecked()) maquinasDisponibles.add("Sala de Musculación");
        if (((CheckBox) getView().findViewById(R.id.checkBoxPesasLibres)).isChecked()) maquinasDisponibles.add("Sala de Pesas Libres");
        if (((CheckBox) getView().findViewById(R.id.checkBoxSauna)).isChecked()) maquinasDisponibles.add("Sala de Sauna");
        if (((CheckBox) getView().findViewById(R.id.checkBoxNatacion)).isChecked()) maquinasDisponibles.add("Sala de Natación");

        String userId = mAuth.getCurrentUser().getUid();

        if (imageUri != null) {
            uploadImageToFirebase(gymName, ciudad, region, pais, calle, mensualidad, diario, openingTime, closingTime, maquinasDisponibles, diasDisponibles, userId);
        } else {
            Toast.makeText(getContext(), "Por favor, sube una imagen de perfil", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToFirebase(String gymName, String ciudad, String region, String pais, String calle, String mensualidad, String diario, String openingTime, String closingTime, ArrayList<String> maquinasDisponibles, ArrayList<String> diasDisponibles, String userId) {
        StorageReference fileReference = storageRef.child("gym_images/" + UUID.randomUUID().toString() + ".jpg");
        fileReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    registerGymInFirestore(gymName, ciudad, region, pais, calle, mensualidad, diario, openingTime, closingTime, maquinasDisponibles, diasDisponibles, imageUrl, userId);
                }))
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al subir la imagen: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void registerGymInFirestore(String gymName, String ciudad, String region, String pais, String calle, String mensualidad, String diario, String openingTime, String closingTime, ArrayList<String> maquinasDisponibles, ArrayList<String> diasDisponibles, String imageUrl, String userId) {
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
        gymData.put("imageUrl", imageUrl);
        gymData.put("ownerId", userId);

        db.collection("gyms").add(gymData)
                .addOnSuccessListener(documentReference -> {
                    String gymId = documentReference.getId();
                    db.collection("users").document(userId)
                            .update("gymId", gymId)
                            .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Gimnasio registrado correctamente", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al asociar el gimnasio con el usuario", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error al registrar el gimnasio", Toast.LENGTH_SHORT).show());
    }
}
