package com.example.gymiot.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.gymiot.Model.Gym;
import com.example.gymiot.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class GymProfileActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView gymNameTxt;
    private TextView horarioTxt;
    private TextView mensualidadTxt;
    private TextView diarioTxt;
    private TextView maquinasTxt;
    private ImageView gymImage;

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
        gymImage = findViewById(R.id.gymImage);

        String gymId = getIntent().getStringExtra("gymId"); // Asumiendo que pasas el ID del gimnasio

        loadGymData(gymId);
    }

    private void loadGymData(String gymId) {
        db.collection("gyms").document(gymId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Gym gym = document.toObject(Gym.class);
                            updateUI(gym);
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
        horarioTxt.setText(gym.getHorario());
        mensualidadTxt.setText(gym.getMensualidad());
        diarioTxt.setText(gym.getDiario());
        maquinasTxt.setText(String.join(", ", gym.getMaquinasDisponibles()));

        Glide.with(this)
                .load(gym.getImageUrl())
                .into(gymImage);
    }
}
