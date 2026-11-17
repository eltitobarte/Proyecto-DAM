package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Pantalla de seguridad de la cuenta: cambiar la contraseña.
// Los cuatro requisitos se van marcando solos según se escribe, y el botón de
// actualizar solo se activa cuando la nueva contraseña está bien y coincide.
public class PMTPasswordActivity extends AppCompatActivity {

    private EditText etCurrent;
    private EditText etNew;
    private EditText etConfirm;
    private TextView btnShow;
    private TextView btnUpdate;

    // Los cuatro cuadraditos de los requisitos
    private TextView[] checks = new TextView[4];

    // Para saber si estamos mostrando la contraseña o no
    private boolean mostrando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_02_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etCurrent = findViewById(R.id.etCurrent);
        etNew = findViewById(R.id.etNew);
        etConfirm = findViewById(R.id.etConfirm);
        btnShow = findViewById(R.id.btnShow);
        btnUpdate = findViewById(R.id.btnUpdate);

        checks[0] = findViewById(R.id.check1);
        checks[1] = findViewById(R.id.check2);
        checks[2] = findViewById(R.id.check3);
        checks[3] = findViewById(R.id.check4);

        btnShow.setOnClickListener(v -> mostrarOcultar());

        // Cada vez que se escribe en la nueva contraseña o en la confirmación,
        // volvemos a mirar los requisitos
        TextWatcher vigilar = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                comprobarRequisitos();
            }
        };
        etNew.addTextChangedListener(vigilar);
        etConfirm.addTextChangedListener(vigilar);

        // Al entrar, la contraseña está vacía, así que todo apagado
        comprobarRequisitos();

        btnUpdate.setOnClickListener(v -> actualizar());
        findViewById(R.id.btnCancel).setOnClickListener(v -> finish());

        // Barra superior
        findViewById(R.id.ivMenu).setOnClickListener(v -> finish());
        findViewById(R.id.ivProfile).setOnClickListener(v -> startActivity(new Intent(this, PMTProfileActivity.class)));
    }

    // Pone o quita el puntito de la contraseña en los tres campos
    private void mostrarOcultar() {
        mostrando = !mostrando;

        int tipo;
        if (mostrando) {
            tipo = InputType.TYPE_CLASS_TEXT;
            btnShow.setText(R.string.password_hide);
        } else {
            tipo = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            btnShow.setText(R.string.password_show);
        }

        etCurrent.setInputType(tipo);
        etNew.setInputType(tipo);
        etConfirm.setInputType(tipo);
    }

    // Mira si la contraseña nueva cumple los cuatro requisitos y si las dos coinciden
    private void comprobarRequisitos() {
        String pass = etNew.getText().toString();

        boolean largo = pass.length() >= 8;
        boolean numero = pass.matches(".*[0-9].*");
        boolean mayuscula = pass.matches(".*[A-Z].*");
        boolean simbolo = pass.matches(".*[^A-Za-z0-9].*");

        marcar(0, largo);
        marcar(1, numero);
        marcar(2, mayuscula);
        marcar(3, simbolo);

        boolean todoBien = largo && numero && mayuscula && simbolo;
        boolean coinciden = !pass.isEmpty() && pass.equals(etConfirm.getText().toString());

        // El botón solo se puede pulsar si está todo bien
        boolean sePuede = todoBien && coinciden;
        btnUpdate.setEnabled(sePuede);
        btnUpdate.setAlpha(sePuede ? 1f : 0.5f);
    }

    // Enciende o apaga uno de los cuadraditos de los requisitos
    private void marcar(int posicion, boolean cumple) {
        if (cumple) {
            checks[posicion].setBackgroundResource(R.drawable.bg_check_on);
            checks[posicion].setText("✓");
        } else {
            checks[posicion].setBackgroundResource(R.drawable.bg_check_off);
            checks[posicion].setText("");
        }
    }

    // De momento no se cambia de verdad, solo avisamos y salimos
    // TODO: cuando exista la conexión, aquí se manda la contraseña nueva al servidor
    private void actualizar() {
        Toast.makeText(this, R.string.password_updated, Toast.LENGTH_SHORT).show();
        finish();
    }
}
