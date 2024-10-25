package com.example.gymiot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymiot.Model.Reserva;
import com.example.gymiot.R;

import java.util.List;

public class GymReservationsAdapter extends RecyclerView.Adapter<GymReservationsAdapter.GymReservationViewHolder> {

    private List<Reserva> reservasList;
    private Context context;
    private OnGymReservationClickListener onGymReservationClickListener;

    public GymReservationsAdapter(List<Reserva> reservasList, Context context, OnGymReservationClickListener onGymReservationClickListener) {
        this.reservasList = reservasList;
        this.context = context;
        this.onGymReservationClickListener = onGymReservationClickListener;
    }

    @NonNull
    @Override
    public GymReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gym_reservation, parent, false);
        return new GymReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymReservationViewHolder holder, int position) {
        Reserva reserva = reservasList.get(position);
        holder.diaReservaTxt.setText(reserva.getDiaReserva());
        holder.horaReservaTxt.setText(reserva.getHoraReserva());

        holder.verReservaBtn.setOnClickListener(v -> onGymReservationClickListener.onGymReservationClick(reserva));
    }

    @Override
    public int getItemCount() {
        return reservasList.size();
    }

    public static class GymReservationViewHolder extends RecyclerView.ViewHolder {

        TextView diaReservaTxt, horaReservaTxt;
        Button verReservaBtn;

        public GymReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            diaReservaTxt = itemView.findViewById(R.id.diaTxt);
            horaReservaTxt = itemView.findViewById(R.id.horaTxt);
            verReservaBtn = itemView.findViewById(R.id.verReservaBtn);
        }
    }

    public interface OnGymReservationClickListener {
        void onGymReservationClick(Reserva reserva);
    }
}
