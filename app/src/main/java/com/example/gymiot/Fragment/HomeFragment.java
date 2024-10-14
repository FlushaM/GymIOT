package com.example.gymiot.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gymiot.Adapter.GymAdapter;
import com.example.gymiot.Model.Gym;
import com.example.gymiot.R;
import com.example.gymiot.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private FirebaseFirestore db;
    private GymAdapter adapter;
    private List<Gym> gymList;
    private FirebaseAuth mAuth;
    private String selectedPais = "";
    private String selectedRegion = "";
    private String selectedCiudad = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            binding.textView10.setText(currentUser.getEmail());
        }

        binding.logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(getActivity(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
        });

        db = FirebaseFirestore.getInstance();
        gymList = new ArrayList<>();
        adapter = new GymAdapter(getContext(), gymList);

        binding.bestGymView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.bestGymView.setAdapter(adapter);

        setupSpinners();
        loadGyms();

        return binding.getRoot();
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> paisAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.paises, android.R.layout.simple_spinner_item);
        paisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPais.setAdapter(paisAdapter);

        ArrayAdapter<CharSequence> regionAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.regiones, android.R.layout.simple_spinner_item);
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRegion.setAdapter(regionAdapter);

        binding.spinnerPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPais = parent.getItemAtPosition(position).toString();
                if (selectedPais.equals("Chile")) {
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
        int arrayId = 0;

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
                arrayId = R.array.ciudades_atacama;
                break;
        }

        ArrayAdapter<CharSequence> ciudadAdapter = ArrayAdapter.createFromResource(getContext(),
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
                        if (gym != null) {
                            gym.setId(document.getId()); // Asignar el ID del documento al objeto Gym
                            gymList.add(gym);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            } else {
                Log.e("HomeFragment", "Error getting documents: ", task.getException());
                Toast.makeText(getActivity(), "Error al filtrar los gimnasios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadGyms() {
        filterGyms();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
