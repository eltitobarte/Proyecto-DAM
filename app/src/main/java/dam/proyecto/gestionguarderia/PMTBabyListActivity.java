package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Listado de menores (Baby).
// La lista es un RecyclerView: las filas ya no están escritas en el layout,
// las crea el adaptador a partir de los datos de ejemplo de abajo.
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

        // Preparamos la lista de menores con datos de ejemplo
        List<Menor> menores = datosDeEjemplo();

        RecyclerView lista = findViewById(R.id.listaMenores);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(new MenoresAdapter(menores, menor -> abrirDiario(menor)));
    }

    // Datos de ejemplo. Cuando haya base de datos, esto se pedirá de verdad.
    private List<Menor> datosDeEjemplo() {
        List<Menor> menores = new ArrayList<>();
        menores.add(new Menor("Lucía"));
        menores.add(new Menor("Mateo"));
        menores.add(new Menor("Sofía"));
        return menores;
    }

    // Abre el diario del niño, mandándole su nombre
    private void abrirDiario(Menor menor) {
        Intent intent = new Intent(this, PMTDiaryActivity.class);
        intent.putExtra("nombre", menor.nombre);
        startActivity(intent);
    }
}
