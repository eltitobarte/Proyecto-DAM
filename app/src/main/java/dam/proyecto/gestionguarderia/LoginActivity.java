package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import dam.proyecto.gestionguarderia.services.ConfigApp;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilUser;
    private TextInputLayout tilPassword;
    private TextInputEditText etUser;
    private TextInputEditText etPassword;
    private CheckBox cbRemember;
    private ConfigApp config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth_01_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        config = new ConfigApp(this);

        tilUser = findViewById(R.id.tilUser);
        tilPassword = findViewById(R.id.tilPassword);
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextView tvForgot = findViewById(R.id.tvForgot);

        tvForgot.setOnClickListener(v -> {
            Intent forgot = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(forgot);
        });

        btnLogin.setOnClickListener(v -> acceder());

        // Al rellenar el usuario se quita el error del campo anterior
        etUser.setOnFocusChangeListener((v, tieneFoco) -> {
            if (tieneFoco) {
                tilUser.setError(null);
            }
        });
        etPassword.setOnFocusChangeListener((v, tieneFoco) -> {
            if (tieneFoco) {
                tilPassword.setError(null);
            }
        });
    }

    // Mira que los dos campos estén rellenos y, si todo está bien, entra al inicio.
    // TODO: cuando haya conexión de verdad, esto se comprobará contra la API y se guardará el token.
    private void acceder() {
        String usuario = textoSeguro(etUser);
        String contrasena = textoSeguro(etPassword);

        boolean valido = true;

        if (usuario.isEmpty()) {
            tilUser.setError(getString(R.string.login_error_user));
            valido = false;
        }
        if (contrasena.isEmpty()) {
            tilPassword.setError(getString(R.string.login_error_pwd));
            valido = false;
        }
        if (!valido) {
            return;
        }

        // De momento guardamos el usuario y el rol a mano, sin servidor
        // TODO: lo de "recordar contraseña" habrá que guardarlo cuando exista la conexión
        config.setUser(usuario);
        config.setRole("FAMILY");

        Intent home = new Intent(this, PMTHomeActivity.class);
        startActivity(home);
        finish();
    }

    // Coge el texto del campo sin espacios y sin petar si está vacío
    private String textoSeguro(TextInputEditText campo) {
        if (campo.getText() == null) {
            return "";
        }
        return campo.getText().toString().trim();
    }
}
