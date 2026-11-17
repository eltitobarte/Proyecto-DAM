package dam.proyecto.gestionguarderia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Adaptador de la lista de conversaciones.
// Igual que el de menores, pero cada fila es item_chat.xml y además
// enciende o apaga el puntito de "sin leer".
public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MiHolder> {

    public interface AlPulsarChat {
        void alPulsar(Conversacion conversacion);
    }

    private List<Conversacion> conversaciones;
    private AlPulsarChat alPulsar;

    public ChatsAdapter(List<Conversacion> conversaciones, AlPulsarChat alPulsar) {
        this.conversaciones = conversaciones;
        this.alPulsar = alPulsar;
    }

    @NonNull
    @Override
    public MiHolder onCreateViewHolder(@NonNull ViewGroup padre, int tipoDeFila) {
        View fila = LayoutInflater.from(padre.getContext()).inflate(R.layout.item_chat, padre, false);
        return new MiHolder(fila);
    }

    @Override
    public void onBindViewHolder(@NonNull MiHolder holder, int posicion) {
        Conversacion conversacion = conversaciones.get(posicion);

        holder.tvNombre.setText(conversacion.nombre);

        // Si no hay mensajes sin leer, el puntito se oculta
        if (conversacion.sinLeer) {
            holder.puntoNoLeido.setVisibility(View.VISIBLE);
        } else {
            holder.puntoNoLeido.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(v -> alPulsar.alPulsar(conversacion));
    }

    @Override
    public int getItemCount() {
        return conversaciones.size();
    }

    public static class MiHolder extends RecyclerView.ViewHolder {

        TextView tvNombre;
        View puntoNoLeido;

        public MiHolder(@NonNull View fila) {
            super(fila);
            tvNombre = fila.findViewById(R.id.tvNombre);
            puntoNoLeido = fila.findViewById(R.id.puntoNoLeido);
        }
    }
}
