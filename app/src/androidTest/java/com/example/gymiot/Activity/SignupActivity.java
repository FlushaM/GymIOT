package com.example.gymiot.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.gymiot.databinding.ActivitySignupBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    private FirebaseAuth mAuth;

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
                String email = binding.userEdt.getText().toString();
                String password = binding.passEdt.getText().toString(); // Corregido

                if (password.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Tu contraseña debe tener 6 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crear usuario con Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                        // Aquí puedes redirigir al usuario a otra actividad o hacer otra acción
                    } else {
                        Toast.makeText(SignupActivity.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
