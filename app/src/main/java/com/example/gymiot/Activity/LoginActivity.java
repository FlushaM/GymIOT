package com.example.gymiot.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.gymiot.R;
import com.example.gymiot.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity {
    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LoginActivity", "LoginActivity created");
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance(); // Inicializa aquí
        setVariable();
    }

    private void setVariable() {
        binding.loginBtn.setOnClickListener(v -> {
            String email = binding.userEdt.getText().toString();
            String password = binding.passEdt.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {
                Log.d("LoginActivity", "Intentando iniciar sesión con el correo: " + email);

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("LoginActivity", "Inicio de sesión exitoso");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else {
                        Log.e("LoginActivity", "Error de inicio de sesión: " + task.getException().getMessage());
                        Toast.makeText(LoginActivity.this, "Autenticación Fallida", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(LoginActivity.this, "Por favor, ingrese nombre y contraseña", Toast.LENGTH_SHORT).show();
            }
        });
    }
}