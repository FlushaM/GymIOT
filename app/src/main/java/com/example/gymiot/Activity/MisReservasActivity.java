package com.example.gymiot.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymiot.Adapter.ReservasAdapter;
import com.example.gymiot.Model.Reserva;
import com.example.gymiot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MisReservasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservasAdapter adapter;
    private List<Reserva> reservasList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reservas);

        recyclerView = findViewById(R.id.recyclerViewReservas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservasList = new ArrayList<>();
        adapter = new ReservasAdapter(reservasList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        cargarReservas();
    }

    private void cargarReservas() {
        String userId = auth.getCurrentUser().getUid();
        db.collection("reservas")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reservasList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Reserva reserva = document.toObject(Reserva.class);
                        reserva.setId(document.getId());
                        reservasList.add(reserva);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar las reservas", Toast.LENGTH_SHORT).show();
                });
    }

    public void eliminarReserva(String reservaId) {
        db.collection("reservas").document(reservaId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Reserva eliminada", Toast.LENGTH_SHORT).show();
                    cargarReservas(); // Volver a cargar las reservas
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al eliminar la reserva", Toast.LENGTH_SHORT).show();
                });
    }
}
