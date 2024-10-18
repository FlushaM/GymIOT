package com.example.gymiot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymiot.Activity.MisReservasActivity;
import com.example.gymiot.Model.Reserva;
import com.example.gymiot.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ReservaViewHolder> {

    private List<Reserva> reservasList;
    private Context context;

    public ReservasAdapter(List<Reserva> reservasList, Context context) {
        this.reservasList = reservasList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva, parent, false);
        return new ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        Reserva reserva = reservasList.get(position);

        // Asignar el día y la hora
        holder.diaTxt.setText(reserva.getDiaReserva());
        holder.horaTxt.setText(reserva.getHoraReserva());

        // Cargar el nombre del gimnasio desde Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("gyms").document(reserva.getGymId()).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String gymName = documentSnapshot.getString("gymName");
                holder.gymNameTxt.setText(gymName); // Asignar el nombre del gimnasio al TextView
            } else {
                holder.gymNameTxt.setText("Gimnasio no encontrado");
            }
        }).addOnFailureListener(e -> {
            holder.gymNameTxt.setText("Error al cargar nombre");
        });

        // Botón para eliminar la reserva
        holder.eliminarBtn.setOnClickListener(v -> {
            if (context instanceof MisReservasActivity) {
                ((MisReservasActivity) context).eliminarReserva(reserva.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservasList.size();
    }

    public static class ReservaViewHolder extends RecyclerView.ViewHolder {

        TextView diaTxt, horaTxt, gymNameTxt;  // Se añade gymNameTxt
        Button eliminarBtn;

        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
            diaTxt = itemView.findViewById(R.id.diaTxt);
            horaTxt = itemView.findViewById(R.id.horaTxt);
            gymNameTxt = itemView.findViewById(R.id.gymNameTxt);  // Enlazar gymNameTxt
            eliminarBtn = itemView.findViewById(R.id.eliminarBtn);
        }
    }
}
