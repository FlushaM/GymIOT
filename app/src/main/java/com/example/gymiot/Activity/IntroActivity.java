package com.example.gymiot.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.gymiot.R;
import com.example.gymiot.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    ActivityIntroBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setVariable();
        // Color hexadecimal correcto para la barra de estado
        getWindow().setStatusBarColor(Color.parseColor("#FFE4B5"));
    }

    private void setVariable(){
        // Corrección: setOnClickListener
        binding.imageView3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // Lógica para el botón de login
            }
        });

        binding.textView7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Lógica para el botón de registro
            }
        });
    }
}

