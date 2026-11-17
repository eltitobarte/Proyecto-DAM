package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Diario del niño: lo que ha hecho durante el día (ánimo, comida, siesta...).
// El nombre del niño llega desde el listado de menores con un extra del Intent.
public class PMTDiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diary_01_diary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recogemos el nombre que nos manda el listado de menores
        String nombre = getIntent().getStringExtra("nombre");
        TextView tvHeading = findViewById(R.id.tvHeading);
        if (nombre != null && !nombre.isEmpty()) {
            tvHeading.setText(getString(R.string.diary_heading_name, nombre));
        }

        // El avatar lleva a la ficha del menor
        findViewById(R.id.ivAvatar).setOnClickListener(v -> startActivity(new Intent(this, PMTChildDetailActivity.class)));

        // Las tarjetas que ya tienen pantalla
        findViewById(R.id.cardFood).setOnClickListener(v -> startActivity(new Intent(this, PMTFoodActivity.class)));
        findViewById(R.id.cardHealth).setOnClickListener(v -> startActivity(new Intent(this, PMTHealthActivity.class)));
        findViewById(R.id.cardNotifications).setOnClickListener(v -> startActivity(new Intent(this, PMTAdvicesActivity.class)));

        // Estas todavía no: son solo informativas
        findViewById(R.id.cardStatus).setOnClickListener(v -> avisoProximamente());
        findViewById(R.id.cardMood).setOnClickListener(v -> avisoProximamente());
        findViewById(R.id.cardSleep).setOnClickListener(v -> avisoProximamente());

        // Barra superior
        findViewById(R.id.ivMenu).setOnClickListener(v -> finish());
        findViewById(R.id.ivProfile).setOnClickListener(v -> startActivity(new Intent(this, PMTProfileActivity.class)));
    }

    private void avisoProximamente() {
        Toast.makeText(this, R.string.comun_proximamente, Toast.LENGTH_SHORT).show();
    }
}
