package com.example.gymiot.Activity;

import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.example.gymiot.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
        // Color hexadecimal correcto para la barra de estado
        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(v -> {
            // Siempre redirige a LoginActivity
            startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            finish(); // Finaliza IntroActivity para no volver a esta pantalla
        });

        binding.signupBtn.setOnClickListener(v -> startActivity(new Intent(IntroActivity.this, SignupActivity.class)));
    }
}


