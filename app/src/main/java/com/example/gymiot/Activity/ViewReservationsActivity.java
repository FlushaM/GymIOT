package com.example.gymiot.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymiot.Adapter.GymReservationsAdapter;
import com.example.gymiot.Model.Reserva;
import com.example.gymiot.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ViewReservationsActivity extends AppCompatActivity {

    private RecyclerView reservationsRecyclerView;
    private FirebaseFirestore db;
    private List<Reserva> reservasList;
    private GymReservationsAdapter adapter;
    private String gymId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservations);

        reservationsRecyclerView = findViewById(R.id.reservationsRecyclerView);
        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        reservasList = new ArrayList<>();
        adapter = new GymReservationsAdapter(reservasList, this, reserva -> {
            // Aquí puedes manejar el evento de clic en una reserva
        });
        reservationsRecyclerView.setAdapter(adapter);

        // Recibe el gymId pasado desde ModifyGymProfileActivity
        gymId = getIntent().getStringExtra("gymId");
        if (gymId != null) {
            loadReservations(gymId);
        } else {
            Toast.makeText(this, "Error: Gym ID no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadReservations(String gymId) {
        db.collection("reservas")
                .whereEqualTo("gymId", gymId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        reservasList.clear();
                        reservasList.addAll(queryDocumentSnapshots.toObjects(Reserva.class));
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "No hay reservas para este gimnasio", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar reservas", Toast.LENGTH_SHORT).show();
                });
    }
}
