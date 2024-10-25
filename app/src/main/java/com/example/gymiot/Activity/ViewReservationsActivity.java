package com.example.gymiot.Activity;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reservations);

        reservationsRecyclerView = findViewById(R.id.reservationsRecyclerView);
        db = FirebaseFirestore.getInstance();
        reservasList = new ArrayList<>();

        gymId = getIntent().getStringExtra("gymId");
        if (gymId != null) {
            loadReservations(gymId);
        }

        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GymReservationsAdapter(reservasList, this, reserva -> {
            // Manejar el evento cuando se haga clic en una reserva
        });
        reservationsRecyclerView.setAdapter(adapter);
    }

    private void loadReservations(String gymId) {
        db.collection("reservas").whereEqualTo("gymId", gymId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reservasList.clear();
                    reservasList.addAll(queryDocumentSnapshots.toObjects(Reserva.class));
                    adapter.notifyDataSetChanged();
                });
    }
}
