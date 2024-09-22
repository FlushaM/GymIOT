package com.example.gymiot.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.gymiot.Adapter.GymAdapter;
import com.example.gymiot.Model.Gym;
import com.example.gymiot.databinding.ActivityMainBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseFirestore db;
    private GymAdapter adapter;
    private List<Gym> gymList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        gymList = new ArrayList<>();
        adapter = new GymAdapter(this, gymList);

        binding.bestGymView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.bestGymView.setAdapter(adapter);

        loadGyms();
    }

    private void loadGyms() {
        binding.progressBarBestGym.setVisibility(View.VISIBLE);
        db.collection("gyms")
                .get()
                .addOnCompleteListener(task -> {
                    binding.progressBarBestGym.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        gymList.clear();
                        QuerySnapshot snapshots = task.getResult();
                        if (snapshots != null) {
                            for (QueryDocumentSnapshot document : snapshots) {
                                Gym gym = document.toObject(Gym.class);
                                gymList.add(gym);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("MainActivity", "Error getting documents: ", task.getException());
                    }
                });
    }
}
