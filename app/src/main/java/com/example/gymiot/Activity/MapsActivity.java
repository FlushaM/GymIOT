package com.example.gymiot.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gymiot.R;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Mapbox;
import com.mapbox.maps.Style;

public class MapsActivity extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializa Mapbox con tu token
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        setContentView(R.layout.activity_maps);

        // Configura el MapView
        mapView = findViewById(R.id.mapView);

        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, style -> {
            // El mapa está listo
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Libera los recursos del mapa
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mapView != null) {
            mapView.onStop();
        }
    }
}
