package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Conversación del chat con el profesor.
 *
 * De momento los mensajes solo están en pantalla: al escribir uno se añade
 * una burbuja nueva abajo, pero no se envía a ningún sitio.
 * Las fotos, el código y el micro son solo decorativos por ahora.
 */
public class PMTChatDetailActivity extends AppCompatActivity {

    private LinearLayout listaMensajes;
    private ScrollView scrollMensajes;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_02_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listaMensajes = findViewById(R.id.listaMensajes);
        scrollMensajes = findViewById(R.id.scrollMensajes);
        etMessage = findViewById(R.id.etMessage);

        findViewById(R.id.btnSend).setOnClickListener(v -> enviarMensaje());

        // Estos tres botones todavía no hacen nada
        findViewById(R.id.btnImage).setOnClickListener(v -> avisoProximamente());
        findViewById(R.id.btnCode).setOnClickListener(v -> avisoProximamente());
        findViewById(R.id.btnMic).setOnClickListener(v -> avisoProximamente());

        // Barra superior: la flecha y el menú vuelven atrás, el muñeco va al perfil
        findViewById(R.id.ivMenu).setOnClickListener(v -> finish());
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        findViewById(R.id.ivProfile).setOnClickListener(v -> startActivity(new Intent(this, PMTProfileActivity.class)));
    }

    /**
     * Coge lo que hay escrito y lo pone como una burbuja nueva al final.
     * Si no hay nada escrito, avisamos y no hacemos nada.
     */
    private void enviarMensaje() {
        String texto = etMessage.getText().toString().trim();

        if (texto.isEmpty()) {
            Toast.makeText(this, R.string.chat_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // Creamos la burbuja con el layout que hicimos para eso
        TextView burbuja = (TextView) getLayoutInflater().inflate(R.layout.burbuja_enviada, listaMensajes, false);
        burbuja.setText(texto);
        listaMensajes.addView(burbuja);

        // Limpiamos el campo y bajamos hasta el último mensaje
        etMessage.setText("");
        scrollMensajes.post(() -> scrollMensajes.fullScroll(View.FOCUS_DOWN));
    }

    private void avisoProximamente() {
        Toast.makeText(this, R.string.comun_proximamente, Toast.LENGTH_SHORT).show();
    }
}
