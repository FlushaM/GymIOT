package com.example.gymiot.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.example.gymiot.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AjustesFragment extends Fragment {

    private Switch darkModeSwitch;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public AjustesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtener las preferencias para guardar el estado del modo oscuro
        sharedPreferences = getActivity().getSharedPreferences("AppSettingsPrefs", 0);
        editor = sharedPreferences.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ajustes, container, false);

        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);

        // Leer el estado guardado del modo oscuro
        boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        // Configurar el estado inicial del Switch
        darkModeSwitch.setChecked(isDarkModeOn);

        // Cambiar el modo oscuro según el estado del switch
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Activar modo oscuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("isDarkModeOn", true);
            } else {
                // Desactivar modo oscuro
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("isDarkModeOn", false);
            }
            editor.apply(); // Guardar los cambios
        });

        return view;
    }
}
