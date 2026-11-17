package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Pantalla de inicio del perfil familia.
// Desde aquí se llega a todo: las seis tarjetas de la rejilla y los iconos de arriba.
public class PMTHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_01_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- Barra superior ---
        // TODO: el menú lateral todavía no está hecho, así que el icono no hace nada
        findViewById(R.id.ivProfile).setOnClickListener(v -> abrir(PMTProfileActivity.class));

        // --- Tarjeta del niño: lleva a su ficha ---
        findViewById(R.id.cardChild).setOnClickListener(v -> abrir(PMTChildDetailActivity.class));

        // --- Rejilla de accesos ---
        findViewById(R.id.cardBaby).setOnClickListener(v -> abrir(PMTBabyListActivity.class));
        findViewById(R.id.cardChat).setOnClickListener(v -> abrir(PMTChatListActivity.class));
        findViewById(R.id.cardNotices).setOnClickListener(v -> abrir(PMTAdvicesActivity.class));
        findViewById(R.id.cardProfile).setOnClickListener(v -> abrir(PMTProfileActivity.class));

        // Calendario y comedor ya tienen su pantalla
        findViewById(R.id.cardCalendar).setOnClickListener(v -> abrir(PMTCalendarActivity.class));
        findViewById(R.id.cardFood).setOnClickListener(v -> abrir(PMTFoodActivity.class));
    }

    // Abre otra pantalla
    private void abrir(Class<?> destino) {
        startActivity(new Intent(this, destino));
    }
}
