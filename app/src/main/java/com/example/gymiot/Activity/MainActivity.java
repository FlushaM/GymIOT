package com.example.gymiot.Activity;

import com.example.gymiot.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gymiot.Adapter.GymAdapter;
import com.example.gymiot.Model.Gym;
import com.example.gymiot.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseFirestore db;
    private GymAdapter adapter;
    private List<Gym> gymList;
    private FirebaseAuth mAuth;
    private String selectedPais = "";
    private String selectedRegion = "";
    private String selectedCiudad = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            binding.textView10.setText(currentUser.getEmail());
        }

        binding.logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        db = FirebaseFirestore.getInstance();
        gymList = new ArrayList<>();
        adapter = new GymAdapter(this, gymList);

        binding.bestGymView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.bestGymView.setAdapter(adapter);

        setupSpinners();
        loadGyms();
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> paisAdapter = ArrayAdapter.createFromResource(this,
                R.array.paises, android.R.layout.simple_spinner_item);
        paisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPais.setAdapter(paisAdapter);

        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(this,
                R.array.regiones, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRegion.setAdapter(regionAdapter);

        binding.spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPais = parent.getItemAtPosition(position).toString();
                if (selectedPais.equals("Chile")) {
                    // Configurar regiones
                    binding.spinnerRegion.setSelection(0); // Resetear región
                }
                filterGyms();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedPais = "";
                filterGyms();
            }
        });

        binding.spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRegion = parent.getItemAtPosition(position).toString();
                updateCiudadSpinner();
                filterGyms();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRegion = "";
                filterGyms();
            }
        });

        binding.spinnerCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCiudad = parent.getItemAtPosition(position).toString();
                filterGyms();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCiudad = "";
                filterGyms();
            }
        });
    }

    private void updateCiudadSpinner() {
        int arrayId = 0; // ID del array de ciudades correspondiente a la región

        switch (selectedRegion) {
            case "Atacama":
                arrayId = R.array.ciudades_atacama;
                break;
            case "Coquimbo":
                arrayId = R.array.ciudades_coquimbo;
                break;
            case "Metropolitana":
                arrayId = R.array.ciudades_metropolitana;
                break;
            default:
                arrayId = R.array.ciudades_atacama; // Por defecto
                break;
        }

        ArrayAdapter<CharSequence> ciudadAdapter = ArrayAdapter.createFromResource(this,
                arrayId, android.R.layout.simple_spinner_item);
        ciudadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCiudad.setAdapter(ciudadAdapter);
    }

    private void filterGyms() {
        binding.progressBarBestGym.setVisibility(View.VISIBLE);
        Query query = db.collection("gyms");

        if (!selectedPais.isEmpty()) {
            query = query.whereEqualTo("pais", selectedPais);
        }
        if (!selectedRegion.isEmpty()) {
            query = query.whereEqualTo("region", selectedRegion);
        }
        if (!selectedCiudad.isEmpty()) {
            query = query.whereEqualTo("ciudad", selectedCiudad);
        }

        query.get().addOnCompleteListener(task -> {
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
                Toast.makeText(MainActivity.this, "Error al filtrar los gimnasios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGyms() {
        filterGyms();
    }
}
