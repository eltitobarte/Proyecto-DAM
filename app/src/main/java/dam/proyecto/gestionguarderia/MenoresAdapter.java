package dam.proyecto.gestionguarderia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Adaptador de la lista de menores.
// Coge la lista de niños y va creando una fila (item_menor.xml) para cada uno.
public class MenoresAdapter extends RecyclerView.Adapter<MenoresAdapter.MiHolder> {

    // Para avisar a la pantalla de que se ha pulsado un niño
    public interface AlPulsarMenor {
        void alPulsar(Menor menor);
    }

    private List<Menor> menores;
    private AlPulsarMenor alPulsar;

    public MenoresAdapter(List<Menor> menores, AlPulsarMenor alPulsar) {
        this.menores = menores;
        this.alPulsar = alPulsar;
    }

    @NonNull
    @Override
    public MiHolder onCreateViewHolder(@NonNull ViewGroup padre, int tipoDeFila) {
        View fila = LayoutInflater.from(padre.getContext()).inflate(R.layout.item_menor, padre, false);
        return new MiHolder(fila);
    }

    @Override
    public void onBindViewHolder(@NonNull MiHolder holder, int posicion) {
        Menor menor = menores.get(posicion);

        holder.tvNombre.setText(menor.nombre);
        holder.itemView.setOnClickListener(v -> alPulsar.alPulsar(menor));
    }

    @Override
    public int getItemCount() {
        return menores.size();
    }

    // Aquí guardamos las vistas de una fila para no buscarlas cada vez
    public static class MiHolder extends RecyclerView.ViewHolder {

        TextView tvNombre;

        public MiHolder(@NonNull View fila) {
            super(fila);
            tvNombre = fila.findViewById(R.id.tvNombre);
        }
    }
}
