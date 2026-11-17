package dam.proyecto.gestionguarderia;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import dam.proyecto.gestionguarderia.services.ConfigApp;

/**
 * Perfil del usuario de familia: menores, salud, seguridad y cierre de sesión.
 */
public class PMTProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_01_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // En el perfil el icono de menú vuelve atrás; el de perfil no hace nada (ya estamos aquí)
        findViewById(R.id.ivMenu).setOnClickListener(v -> finish());

        // Menores: lleva al listado de niños
        findViewById(R.id.cardMinors).setOnClickListener(v -> startActivity(new Intent(this, PMTBabyListActivity.class)));
        findViewById(R.id.rowChild1).setOnClickListener(v -> startActivity(new Intent(this, PMTBabyListActivity.class)));
        findViewById(R.id.rowChild2).setOnClickListener(v -> startActivity(new Intent(this, PMTBabyListActivity.class)));

        // TODO Salud/alergias y cambio de contraseña: pendientes de su Activity
        findViewById(R.id.cardHealth).setOnClickListener(v -> avisoProximamente());
        findViewById(R.id.cardSecurity).setOnClickListener(v -> avisoProximamente());
        findViewById(R.id.btnChangePassword).setOnClickListener(v -> avisoProximamente());

        // Cierre de sesión: primero preguntamos con el diálogo del diseño
        findViewById(R.id.btnLogout).setOnClickListener(v -> preguntarCerrarSesion());
    }

    /**
     * Saca el diálogo de "¿seguro que quieres cerrar sesión?".
     * El diseño ya estaba hecho (activity_profile_03_logout_confirm),
     * así que lo usamos dentro de un Dialog normal.
     */
    private void preguntarCerrarSesion() {
        Dialog dialogo = new Dialog(this);
        dialogo.setContentView(R.layout.activity_profile_03_logout_confirm);

        // Sin esto el diálogo sale muy estrecho y se cortan los botones:
        // le decimos que ocupe el ancho de la pantalla y quitamos su fondo,
        // porque el fondo blanco ya lo pone la tarjeta del layout.
        if (dialogo.getWindow() != null) {
            dialogo.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogo.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Cancelar: solo cierra el diálogo y nos quedamos donde estamos
        dialogo.findViewById(R.id.btnCancel).setOnClickListener(v -> dialogo.dismiss());

        // Cerrar sesión: borramos lo que teníamos guardado y volvemos al login
        dialogo.findViewById(R.id.btnConfirm).setOnClickListener(v -> {
            dialogo.dismiss();
            cerrarSesion();
        });

        dialogo.show();
    }

    /** Borra los datos de sesión y vuelve al login dejando la pila limpia. */
    private void cerrarSesion() {
        ConfigApp config = new ConfigApp(this);
        config.setToken("");
        config.setRole("GUEST");
        config.setUserId(0);

        Intent login = new Intent(this, LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(login);
        finish();
    }

    private void avisoProximamente() {
        Toast.makeText(this, R.string.comun_proximamente, Toast.LENGTH_SHORT).show();
    }
}
