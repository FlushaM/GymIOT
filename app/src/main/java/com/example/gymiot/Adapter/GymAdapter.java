package com.example.gymiot.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.gymiot.Model.Gym;
import com.example.gymiot.R;
import java.util.List;

public class GymAdapter extends RecyclerView.Adapter<GymAdapter.GymViewHolder> {

    private Context context;
    private List<Gym> gymList;

    public GymAdapter(Context context, List<Gym> gymList) {
        this.context = context;
        this.gymList = gymList;
    }

    @NonNull
    @Override
    public GymViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gym_card, parent, false);
        return new GymViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymViewHolder holder, int position) {
        Gym gym = gymList.get(position);
        holder.tituloTxt.setText(gym.getGymName());
        holder.estadoTxt.setText(gym.getUbicacion());
        Glide.with(context).load(gym.getImagenUrl()).into(holder.fotogym); // Cargar la imagen con Glide
    }

    @Override
    public int getItemCount() {
        return gymList.size();
    }

    public static class GymViewHolder extends RecyclerView.ViewHolder {
        ImageView fotogym;
        TextView tituloTxt, estadoTxt;

        public GymViewHolder(@NonNull View itemView) {
            super(itemView);
            fotogym = itemView.findViewById(R.id.fotogym);
            tituloTxt = itemView.findViewById(R.id.tituloTxt);
            estadoTxt = itemView.findViewById(R.id.estadoTxt);
        }
    }
}
