package com.example.gymiot.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.gymiot.Fragment.AgregarFragment;
import com.example.gymiot.Fragment.AjustesFragment;
import com.example.gymiot.Fragment.HomeFragment;
import com.example.gymiot.Fragment.PerfilFragment;
import com.example.gymiot.Fragment.SalirFragment;
import com.example.gymiot.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar el BottomNavigationView para manejar la navegación
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.Perfil) {
                    selectedFragment = new PerfilFragment();
                } else if (itemId == R.id.agregar) {
                    selectedFragment = new AgregarFragment();
                } else if (itemId == R.id.ajustes) {
                    selectedFragment = new AjustesFragment();
                } else if (itemId == R.id.Salir) {
                    selectedFragment = new SalirFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }

                return true;  // Devuelve true para indicar que el evento fue manejado
            }
        });

        // Cargar HomeFragment por defecto
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Configurar el FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Cuando se presiona el botón flotante, cargar AgregarFragment
                loadFragment(new AgregarFragment());
            }
        });
    }

    // Método para cargar fragmentos en el contenedor
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}