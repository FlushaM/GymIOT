package com.example.gymiot.Activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gymiot.Model.Gym;
import com.example.gymiot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReservaActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private TextView gymNameTxt;
    private Spinner spinnerDia, spinnerHora;
    private Button buttonReservar;
    private ImageView gymImage;
    private Gym gym;
    private String gymId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        // Inicialización de Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Referencias a los elementos de la interfaz
        gymNameTxt = findViewById(R.id.gymNameTxt);
        spinnerDia = findViewById(R.id.spinnerDia);
        spinnerHora = findViewById(R.id.spinnerHora);
        buttonReservar = findViewById(R.id.buttonReservar);
        gymImage = findViewById(R.id.gymImage);

        // Obtener el ID del gimnasio desde el intent
        gymId = getIntent().getStringExtra("gymId");

        if (gymId == null || gymId.isEmpty()) {
            Toast.makeText(this, "ID del gimnasio no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Cargar datos del gimnasio
        loadGymData(gymId);

        // Acción al hacer clic en "Reservar"
        buttonReservar.setOnClickListener(v -> {
            String selectedDia = spinnerDia.getSelectedItem().toString();
            String selectedHora = spinnerHora.getSelectedItem().toString();
            makeReservation(gymId, auth.getCurrentUser().getUid(), selectedDia, selectedHora);
        });
    }

    // Método para cargar los datos del gimnasio desde Firestore
    private void loadGymData(String gymId) {
        db.collection("gyms").document(gymId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            gym = document.toObject(Gym.class);
                            if (gym != null) {
                                gym.setId(document.getId());
                                // Actualizar la UI con los datos del gimnasio
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

    // Método para actualizar la interfaz de usuario con los datos del gimnasio
    private void updateUI(Gym gym) {
        // Establecer el nombre del gimnasio
        gymNameTxt.setText(gym.getGymName());

        // Cargar la imagen del gimnasio utilizando Glide
        Glide.with(this)
                .load(gym.getImageUrls())

                .into(gymImage);

        // Configurar el spinner para los días disponibles del gimnasio
        ArrayAdapter<String> diaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gym.getDiasDisponibles());
        diaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDia.setAdapter(diaAdapter);

        // Configurar el spinner para las horas disponibles (horario de apertura a cierre)
        ArrayList<String> horasDisponibles = generarHorasDisponibles(gym.getHorarioApertura(), gym.getHorarioCierre());
        ArrayAdapter<String> horaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, horasDisponibles);
        horaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHora.setAdapter(horaAdapter);
    }

    // Método para generar una lista de horas disponibles basadas en el horario de apertura y cierre
    private ArrayList<String> generarHorasDisponibles(String apertura, String cierre) {
        ArrayList<String> horas = new ArrayList<>();
        int aperturaHora = Integer.parseInt(apertura.split(":")[0]);
        int cierreHora = Integer.parseInt(cierre.split(":")[0]);

        // Generar cada hora en formato "HH:00" entre el rango de apertura y cierre
        for (int i = aperturaHora; i <= cierreHora; i++) {
            horas.add(String.format("%02d:00", i));
        }
        return horas;
    }

    // Método para realizar una reserva en Firestore
    private void makeReservation(String gymId, String userId, String dia, String hora) {
        Map<String, Object> reservaData = new HashMap<>();
        reservaData.put("gymId", gymId);
        reservaData.put("userId", userId);
        reservaData.put("fechaReserva", dia);
        reservaData.put("horaReserva", hora);
        reservaData.put("estado", "confirmada");
        reservaData.put("creadaEn", com.google.firebase.firestore.FieldValue.serverTimestamp());

        db.collection("reservas")
                .add(reservaData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ReservaActivity.this, "Reserva realizada con éxito", Toast.LENGTH_SHORT).show();
                    finish(); // Cerrar la actividad después de reservar
                })
                .addOnFailureListener(e -> Toast.makeText(ReservaActivity.this, "Error al realizar la reserva", Toast.LENGTH_SHORT).show());
    }
}
