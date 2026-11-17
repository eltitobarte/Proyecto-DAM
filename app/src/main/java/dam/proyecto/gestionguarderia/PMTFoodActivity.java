package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Pantalla del comedor (menú semanal).
 * Es solo la parte visual: se puede cambiar de mes y elegir una de las 4 semanas,
 * pero el menú que sale es el fijo del diseño. Cuando haya datos reales se cargará ahí.
 * TODO: pedir el menú del mes a los datos del centro y rellenar la tarjeta.
 */
public class PMTFoodActivity extends AppCompatActivity {

    // Nombres de los meses para la cabecera
    private final String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    // Septiembre es el mes que trae el diseño por defecto (enero = 0)
    private int mesActual = 8;

    private TextView tvMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diary_04_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvMonth = findViewById(R.id.tvMonth);

        // Flechas para pasar de mes
        findViewById(R.id.btnPrevMonth).setOnClickListener(v -> cambiarMes(-1));
        findViewById(R.id.btnNextMonth).setOnClickListener(v -> cambiarMes(1));

        // Las 4 semanas. Guardamos los ids en un array para no repetir el mismo código
        int[] semanas = {R.id.btnWeek1, R.id.btnWeek2, R.id.btnWeek3, R.id.btnWeek4};
        for (int i = 0; i < semanas.length; i++) {
            int posicion = i;
            findViewById(semanas[i]).setOnClickListener(v -> marcarSemana(posicion, semanas));
        }

        // Barra superior
        findViewById(R.id.ivMenu).setOnClickListener(v -> finish());
        findViewById(R.id.ivProfile).setOnClickListener(v -> startActivity(new Intent(this, PMTProfileActivity.class)));
    }

    /**
     * Cambia el mes que se ve en la cabecera.
     * Si nos pasamos de diciembre vuelve a enero, y al revés.
     */
    private void cambiarMes(int salto) {
        mesActual = mesActual + salto;

        if (mesActual > 11) {
            mesActual = 0;
        }
        if (mesActual < 0) {
            mesActual = 11;
        }

        tvMonth.setText(getString(R.string.food_month_format, meses[mesActual]));
    }

    /**
     * Pone la semana elegida de color y deja las demás apagadas.
     * (Todavía no cambia el menú porque no hay datos)
     */
    private void marcarSemana(int elegida, int[] semanas) {
        for (int i = 0; i < semanas.length; i++) {
            TextView boton = findViewById(semanas[i]);
            if (i == elegida) {
                boton.setBackgroundResource(R.drawable.bg_week_on);
            } else {
                boton.setBackgroundResource(R.drawable.bg_week_off);
            }
        }
    }
}
