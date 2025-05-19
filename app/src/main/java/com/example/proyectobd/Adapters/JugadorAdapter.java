package com.example.proyectobd.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectobd.Data.Entity.JugadorCompleto;
import com.example.proyectobd.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class JugadorAdapter extends RecyclerView.Adapter<JugadorAdapter.JugadorViewHolder>
{

    private Context context;
    private List<JugadorCompleto> listaJugadores;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditarClick(JugadorCompleto jugador);
        void onEliminarClick(JugadorCompleto jugador);
    }

    public JugadorAdapter(Context context, List<JugadorCompleto> listaJugadores, OnItemClickListener listener) {
        this.context = context;
        this.listaJugadores = listaJugadores;
        this.listener = listener;
    }

    public int getPosition(JugadorCompleto jugador) {
        return listaJugadores != null ? listaJugadores.indexOf(jugador) : -1;
    }

    @NonNull
    @Override
    public JugadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_jugador, parent, false);
        return new JugadorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JugadorViewHolder holder, int position) {
        JugadorCompleto jugadorCompleto = listaJugadores.get(position);

        if (jugadorCompleto == null) return;

        // Datos del jugador
        if (jugadorCompleto.jugador != null) {
            holder.tvNombreJugador.setText(
                    String.format("Jugador: %s",
                            jugadorCompleto.jugador.getNombre() != null ?
                                    jugadorCompleto.jugador.getNombre() : "N/A")
            );
        } else {
            holder.tvNombreJugador.setText("Jugador: No disponible");
        }

        // Datos del entrenador
        if (jugadorCompleto.entrenador != null && jugadorCompleto.entrenador.getNombre() != null) {
            holder.tvNombreEntrenador.setText(
                    String.format("Entrenador: %s", jugadorCompleto.entrenador.getNombre())
            );
        } else {
            holder.tvNombreEntrenador.setText("Entrenador: No asignado");
        }

        // Datos del contrato
        if (jugadorCompleto.contrato != null) {
            NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "PE"));
            String sueldoFormateado = formatoMoneda.format(jugadorCompleto.contrato.getSueldo());
            holder.tvSueldo.setText(String.format("Sueldo: %s", sueldoFormateado));

            try {
                String clausulaFormateada = formatoMoneda.format(
                        Double.parseDouble(jugadorCompleto.contrato.getClausulaRescision())
                );
                holder.tvClausula.setText(String.format("Cláusula: %s", clausulaFormateada));
            } catch (NumberFormatException e) {
                holder.tvClausula.setText("Cláusula: Formato inválido");
            }
        } else {
            holder.tvSueldo.setText("Sueldo: No especificado");
            holder.tvClausula.setText("Cláusula: No especificada");
        }

        holder.imgIcono.setImageResource(R.mipmap.jugador);

        // Listeners
        holder.imgEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditarClick(jugadorCompleto);
            }
        });

        holder.imgBorrar.setOnClickListener(v -> {
            if (listener != null && jugadorCompleto.jugador != null) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmar eliminación")
                        .setMessage("¿Eliminar a " + jugadorCompleto.jugador.getNombre() + "?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            listener.onEliminarClick(jugadorCompleto);
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaJugadores != null ? listaJugadores.size() : 0;
    }

    public static class JugadorViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcono, imgEditar, imgBorrar;
        TextView tvNombreJugador, tvNombreEntrenador, tvSueldo, tvClausula;

        public JugadorViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcono = itemView.findViewById(R.id.imgIcono);
            imgEditar = itemView.findViewById(R.id.imgEditar);
            imgBorrar = itemView.findViewById(R.id.imgBorrar);
            tvNombreJugador = itemView.findViewById(R.id.tvNombreJugador);
            tvNombreEntrenador = itemView.findViewById(R.id.tvNombreEntrenador);
            tvSueldo = itemView.findViewById(R.id.tvSueldo);
            tvClausula = itemView.findViewById(R.id.tvClausula);
        }
    }

    public void actualizarLista(List<JugadorCompleto> nuevaLista) {
        this.listaJugadores = nuevaLista;
        notifyDataSetChanged();
    }
}