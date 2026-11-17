package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Ficha del menor.
 *
 * Los datos se ven como texto normal, pero el número de tarjeta y el teléfono
 * del pediatra se pueden editar: se activan al pulsar el botón "Editar".
 * Lo que se escribe todavía no se guarda en ningún sitio, porque eso es parte
 * de los datos y de momento solo hacemos pantallas.
 */
public class PMTChildDetailActivity extends AppCompatActivity {

    private EditText etCard;
    private EditText etPediatrician;
    private TextView btnEdit;
    private TextView btnSave;
    private TextView btnCancel;

    // Aquí apuntamos lo que ponía antes de editar, para poder dejarlo igual si se cancela
    private String tarjetaAntes;
    private String pediatraAntes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diary_02_child_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etCard = findViewById(R.id.etCard);
        etPediatrician = findViewById(R.id.etPediatrician);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Al entrar solo se ve el botón de editar
        modoEdicion(false);

        btnEdit.setOnClickListener(v -> modoEdicion(true));

        btnSave.setOnClickListener(v -> {
            modoEdicion(false);
            Toast.makeText(this, R.string.child_saved, Toast.LENGTH_SHORT).show();
        });

        btnCancel.setOnClickListener(v -> {
            // Dejamos los datos como estaban antes de empezar a editar
            etCard.setText(tarjetaAntes);
            etPediatrician.setText(pediatraAntes);
            modoEdicion(false);
        });

        // Barra superior
        findViewById(R.id.ivMenu).setOnClickListener(v -> finish());
        findViewById(R.id.ivProfile).setOnClickListener(v -> startActivity(new Intent(this, PMTProfileActivity.class)));
    }

    /**
     * Activa o desactiva la edición de los dos campos.
     * Si se empieza a editar, guardamos una copia de lo que había.
     */
    private void modoEdicion(boolean editando) {
        etCard.setEnabled(editando);
        etPediatrician.setEnabled(editando);

        if (editando) {
            tarjetaAntes = etCard.getText().toString();
            pediatraAntes = etPediatrician.getText().toString();
        }

        // Cuando estamos editando no tiene sentido el botón de editar, y al revés
        btnEdit.setVisibility(editando ? View.GONE : View.VISIBLE);
        btnSave.setVisibility(editando ? View.VISIBLE : View.GONE);
        btnCancel.setVisibility(editando ? View.VISIBLE : View.GONE);
    }
}
