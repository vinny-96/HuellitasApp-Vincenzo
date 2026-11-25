package com.vincenzoratti.huellitasapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MascotaAdapter
        extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {

    public interface AlPulsarMascota {
        void abrir(Mascota mascota);
    }

    private final List<Mascota> mascotas = new ArrayList<>();
    private final AlPulsarMascota alPulsarMascota;

    public MascotaAdapter(AlPulsarMascota alPulsarMascota) {
        this.alPulsarMascota = alPulsarMascota;
    }

    public void actualizar(List<Mascota> nuevasMascotas) {
        mascotas.clear();
        mascotas.addAll(nuevasMascotas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType
    ) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mascota, parent, false);

        return new MascotaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MascotaViewHolder holder, int position
    ) {
        Mascota mascota = mascotas.get(position);

        holder.txtNombre.setText(mascota.getNombre());

        String edad = mascota.getEdad() == null
                ? "Edad no indicada"
                : mascota.getEdad() + " años";

        holder.txtResumen.setText(
                mascota.getEspecie() + " · " + edad
                        + " · " + (mascota.isVacunado()
                        ? "Vacunado" : "No vacunado")
        );

        holder.txtEstado.setText("Estado: " + mascota.getEstado());

        holder.itemView.setOnClickListener(
                vista -> alPulsarMascota.abrir(mascota)
        );
    }

    @Override
    public int getItemCount() {
        return mascotas.size();
    }

    static class MascotaViewHolder extends RecyclerView.ViewHolder {

        final TextView txtNombre;
        final TextView txtResumen;
        final TextView txtEstado;

        MascotaViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtResumen = itemView.findViewById(R.id.txtResumen);
            txtEstado = itemView.findViewById(R.id.txtEstado);
        }
    }
}
