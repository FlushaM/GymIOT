package com.example.gymiot.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gymiot.Activity.GymProfileActivity;
import com.example.gymiot.Model.Gym;
import com.example.gymiot.R;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_mejor_gym, parent, false);
        return new GymViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GymViewHolder holder, int position) {
        Gym gym = gymList.get(position);

        // Cargar la imagen del gimnasio usando Glide
        Glide.with(context)
                .load(gym.getImageUrls())
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.fotogym);

        // Establecer el nombre del gimnasio
        holder.tituloTxt.setText(gym.getGymName());

        // Verificación del ID antes de iniciar la actividad de perfil
        Log.d("GymAdapter", "ID del gimnasio enviado: " + gym.getId());

        // Configurar el botón `>` para redirigir al perfil del gimnasio
        holder.perfilBtn.setOnClickListener(v -> {
            String gymId = gym.getId();
            if (gymId != null && !gymId.isEmpty()) {
                Intent intent = new Intent(context, GymProfileActivity.class);
                intent.putExtra("gymId", gymId);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Error: ID del gimnasio no disponible", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return gymList.size();
    }

    public static class GymViewHolder extends RecyclerView.ViewHolder {

        ImageView fotogym;
        TextView tituloTxt;
        TextView perfilBtn;

        public GymViewHolder(@NonNull View itemView) {
            super(itemView);
            fotogym = itemView.findViewById(R.id.fotogym);
            tituloTxt = itemView.findViewById(R.id.tituloTxt);
            perfilBtn = itemView.findViewById(R.id.textView14); // Asegúrate de que este sea el botón `>`
        }
    }
}
