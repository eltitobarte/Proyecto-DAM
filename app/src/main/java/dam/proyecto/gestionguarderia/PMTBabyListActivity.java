package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Listado de menores del perfil familia (Baby).
 */
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

        // Barra superior: el icono de menú actúa como "volver" en las pantallas internas
        findViewById(R.id.ivMenu).setOnClickListener(v -> finish());
        findViewById(R.id.ivProfile).setOnClickListener(v -> startActivity(new Intent(this, PMTProfileActivity.class)));

        // TODO Diario y ficha del menor: pendientes de su Activity
        findViewById(R.id.rowChild1).setOnClickListener(v -> avisoProximamente());
        findViewById(R.id.rowChild2).setOnClickListener(v -> avisoProximamente());
    }

    private void avisoProximamente() {
        Toast.makeText(this, R.string.comun_proximamente, Toast.LENGTH_SHORT).show();
    }
}
