package com.example.gymiot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gymiot.Activity.GymProfileActivity; // Asegúrate de tener una actividad para el perfil del gym
import com.example.gymiot.Model.Gym;
import com.example.gymiot.R;

import java.util.List;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_mejor_gym, parent, false);
        return new GymViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymViewHolder holder, int position) {
        Gym gym = gymList.get(position);

        // Cargar la imagen del gimnasio usando Glide con el campo corregido
        Glide.with(context)
                .load(gym.getImageUrl()) // Asegúrate de usar getImageUrl()
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(holder.fotogym);

        // Establecer el nombre del gimnasio
        holder.tituloTxt.setText(gym.getGymName());

        // Configurar el botón `>` para redirigir al perfil del gimnasio
        holder.perfilBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, GymProfileActivity.class);
            intent.putExtra("gymId", gym.getGymName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return gymList.size();
    }

    public static class GymViewHolder extends RecyclerView.ViewHolder {

        ImageView fotogym;
        TextView tituloTxt;
        TextView perfilBtn; // Este será el botón `>`

        public GymViewHolder(@NonNull View itemView) {
            super(itemView);
            fotogym = itemView.findViewById(R.id.fotogym);
            tituloTxt = itemView.findViewById(R.id.tituloTxt);
            perfilBtn = itemView.findViewById(R.id.textView14); // Asegúrate de que `textView14` es el botón `>`
        }
    }
}
