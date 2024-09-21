package com.example.gymiot.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gymiot.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Botón para buscar gimnasios
        findViewById(R.id.searchGymBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a MainActivity para buscar gimnasios
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        // Botón para registrar gimnasio
        findViewById(R.id.registerGymBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirige a RegisterGymActivity para registrar un gimnasio
                startActivity(new Intent(HomeActivity.this, RegisterGymActivity.class));
            }
        });
    }
}
