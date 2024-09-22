package com.example.gymiot.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gymiot.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        setupUI();
        setupListeners();
    }

    private void setupUI() {
        // Mostrar el nombre del usuario
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            binding.textView10.setText(user.getDisplayName() != null ? user.getDisplayName() : user.getEmail());
        }
    }

    private void setupListeners() {
        // Funcionalidad de cerrar sesión
        binding.logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        // Configuración de filtros en los Spinners
        setupFilterListeners();
    }

    private void setupFilterListeners() {
        // Filtro por ubicación
        binding.locationSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String location = parentView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Filtrando por ubicación: " + location, Toast.LENGTH_SHORT).show();
                // Implementa la lógica del filtro por ubicación aquí
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada
            }
        });

        // Filtro por precio
        binding.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String price = parentView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Filtrando por precio: " + price, Toast.LENGTH_SHORT).show();
                // Implementa la lógica del filtro por precio aquí
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada
            }
        });

        // Filtro por horario
        binding.spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String schedule = parentView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Filtrando por horario: " + schedule, Toast.LENGTH_SHORT).show();
                // Implementa la lógica del filtro por horario aquí
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada
            }
        });
    }
}
