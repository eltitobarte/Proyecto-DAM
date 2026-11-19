package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Ficha médica del menor: se elige la categoría (alergia, intolerancia,
// medicación u observación) y se escribe el ingrediente y el protocolo.
// Como el resto de pantallas, de momento no guarda nada.
public class PMTHealthActivity extends AppCompatActivity {

    // Los cuatro circulitos de las categorías y sus textos, para pintarlos
    private View[] radios = new View[4];
    private TextView[] etiquetas = new TextView[4];

    // Categoría elegida, por defecto la primera (alergia alimentaria)
    private int elegida = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diary_05_health);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        radios[0] = findViewById(R.id.radio1);
        radios[1] = findViewById(R.id.radio2);
        radios[2] = findViewById(R.id.radio3);
        radios[3] = findViewById(R.id.radio4);

        etiquetas[0] = findViewById(R.id.label1);
        etiquetas[1] = findViewById(R.id.label2);
        etiquetas[2] = findViewById(R.id.label3);
        etiquetas[3] = findViewById(R.id.label4);

        // Toda la fila se puede pulsar, no solo el circulito
        findViewById(R.id.rowCat1).setOnClickListener(v -> elegirCategoria(0));
        findViewById(R.id.rowCat2).setOnClickListener(v -> elegirCategoria(1));
        findViewById(R.id.rowCat3).setOnClickListener(v -> elegirCategoria(2));
        findViewById(R.id.rowCat4).setOnClickListener(v -> elegirCategoria(3));

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            Toast.makeText(this, R.string.health_saved, Toast.LENGTH_SHORT).show();
            finish();
        });

        // Eliminar: antes de borrar preguntamos, para no cargárnosla sin querer
        findViewById(R.id.btnDelete).setOnClickListener(v -> preguntarEliminar());

        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());

        // Barra superior
        findViewById(R.id.ivMenu).setOnClickListener(v -> finish());
        findViewById(R.id.ivProfile).setOnClickListener(v -> startActivity(new Intent(this, PMTProfileActivity.class)));

        // Empezamos con la primera marcada
        elegirCategoria(elegida);
    }

    // Pregunta antes de eliminar la etiqueta médica.
    // Uso un AlertDialog normal porque no hay diseño hecho para este aviso
    // y así sale centrado y con los dos botones bien colocados.
    private void preguntarEliminar() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.health_delete_title)
                .setMessage(R.string.health_delete_body)
                .setPositiveButton(R.string.health_delete, (dialogo, opcion) -> {
                    // El usuario dice que sí: avisamos y volvemos al perfil
                    Toast.makeText(this, R.string.health_deleted, Toast.LENGTH_SHORT).show();
                    finish();
                })
                // Null = solo cierra el aviso y no borra nada
                .setNegativeButton(R.string.health_cancel, null)
                .show();
    }

    // Marca la categoría elegida y apaga las demás
    private void elegirCategoria(int nueva) {
        elegida = nueva;

        for (int i = 0; i < radios.length; i++) {
            if (i == nueva) {
                radios[i].setBackgroundResource(R.drawable.bg_radio_on);
                etiquetas[i].setTextColor(getColor(R.color.tertiary));
            } else {
                radios[i].setBackgroundResource(R.drawable.bg_radio_off);
                etiquetas[i].setTextColor(getColor(R.color.grey));
            }
        }
    }
}
