package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Listado de menores (Baby).
// Cada fila lleva a la ficha del niño.
public class PMTBabyListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_baby_01_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Arriba: el menú hace de "volver" y el muñeco lleva al perfil
        findViewById(R.id.ivMenu).setOnClickListener(v -> finish());
        findViewById(R.id.ivProfile).setOnClickListener(v -> startActivity(new Intent(this, PMTProfileActivity.class)));

        // Cada fila abre la ficha de ese menor
        findViewById(R.id.rowChild1).setOnClickListener(v -> abrirFicha());
        findViewById(R.id.rowChild2).setOnClickListener(v -> abrirFicha());
    }

    // Abre la ficha. De momento siempre la misma, porque los datos están fijos
    private void abrirFicha() {
        startActivity(new Intent(this, PMTChildDetailActivity.class));
    }
}
