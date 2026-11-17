package dam.proyecto.gestionguarderia;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// Listado de conversaciones del chat.
// Como el listado de menores, ahora la lista la pinta un adaptador.
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

        List<Conversacion> conversaciones = datosDeEjemplo();

        RecyclerView lista = findViewById(R.id.listaChats);
        lista.setLayoutManager(new LinearLayoutManager(this));
        lista.setAdapter(new ChatsAdapter(conversaciones, conversacion -> abrirConversacion()));
    }

    // Datos de ejemplo, escritos a mano hasta que haya datos de verdad
    private List<Conversacion> datosDeEjemplo() {
        List<Conversacion> conversaciones = new ArrayList<>();
        conversaciones.add(new Conversacion("Lucía - Clase Ranita", true));
        conversaciones.add(new Conversacion("Mateo - Clase Cebra", false));
        conversaciones.add(new Conversacion("Sofía - Clase Tortuga", true));
        return conversaciones;
    }

    private void abrirConversacion() {
        startActivity(new Intent(this, PMTChatDetailActivity.class));
    }
}
