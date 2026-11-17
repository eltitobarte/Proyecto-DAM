package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Calendario del perfil familia.
 * De momento no hay eventos guardados, así que solo avisamos del día que se toca.
 * TODO: cuando existan datos de eventos, cargarlos aquí y escribir los del día.
 */
public class PMTCalendarActivity extends AppCompatActivity {

    // Texto que hay dentro de la tarjeta de eventos
    private TextView tvEventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diary_03_calendar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvEventos = findViewById(R.id.tvEventos);
        CalendarView calendario = findViewById(R.id.calendarView2);

        // Cuando se toca un día del calendario, escribimos la fecha en la tarjeta
        calendario.setOnDateChangeListener((view, anio, mes, dia) -> {
            // Ojo: el mes empieza en 0 (enero), por eso le sumamos 1
            String fecha = dia + "/" + (mes + 1) + "/" + anio;
            tvEventos.setText(getString(R.string.calendar_empty_day, fecha));
        });

        // Barra superior: el menú vuelve atrás y el muñeco lleva al perfil
        findViewById(R.id.ivMenu).setOnClickListener(v -> finish());
        findViewById(R.id.ivProfile).setOnClickListener(v -> startActivity(new Intent(this, PMTProfileActivity.class)));
    }
}
