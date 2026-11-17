package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Listado de conversaciones del chat.
// Cada fila abre la conversación con el profesor.
public class PMTChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_01_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.ivMenu).setOnClickListener(v -> finish());
        findViewById(R.id.ivProfile).setOnClickListener(v -> startActivity(new Intent(this, PMTProfileActivity.class)));

        // Cada fila abre la conversación con el profesor
        findViewById(R.id.rowChild1).setOnClickListener(v -> abrirConversacion());
        findViewById(R.id.rowChild2).setOnClickListener(v -> abrirConversacion());
    }

    // Abre la conversación. De momento siempre la misma, porque no hay datos
    private void abrirConversacion() {
        startActivity(new Intent(this, PMTChatDetailActivity.class));
    }
}
