package com.example.gymiot.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gymiot.R;

public class RegistroActivity extends AppCompatActivity {
    ActivityRegistroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBiding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
    }

    private void setVariable(){
        binding.registroBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email=binding.userEdt.getText().toString();
                String password=binding.userEdt.getText().toString();
                
                if(password.length()<6){
                    Toast.makeText(RegistroActivity.this, "Tu contraseña debe tener 6 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener
            }
        });
    }

}