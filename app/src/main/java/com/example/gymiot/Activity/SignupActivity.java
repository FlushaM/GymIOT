package com.example.gymiot.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent; // Añadir esta importación para Intent
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.gymiot.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    private FirebaseAuth mAuth;
    private static final String TAG = "SignupActivity"; // Definir TAG para logs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance(); // Inicializar Firebase Auth
        setVariable();
    }

    private void setVariable(){
        binding.signupBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = binding.userEdt.getText().toString().trim(); // Correo con trim para evitar espacios
                String password = binding.passEdt.getText().toString().trim(); // Contraseña

                if (email.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Por favor ingrese un correo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Por favor ingrese una contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Tu contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crear usuario con correo y contraseña en Firebase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "Usuario registrado con éxito");
                            startActivity(new Intent(SignupActivity.this, MainActivity.class)); // Corrección aquí
                        } else {
                            Log.e(TAG, "Error al registrar: " + task.getException());
                            Toast.makeText(SignupActivity.this, "Autenticación fallida: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
