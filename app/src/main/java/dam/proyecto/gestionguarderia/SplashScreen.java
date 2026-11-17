package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dam.proyecto.gestionguarderia.services.ConfigApp;

public class SplashScreen extends AppCompatActivity {

    private ConfigApp config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_01_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TODO SERVICIOS: inicializar BD local y comprobar sesión antes de decidir a dónde ir
        Intent login = new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(login);
        // Se cierra el splash para que «atrás» desde el login no vuelva aquí en bucle
        finish();

    }
}