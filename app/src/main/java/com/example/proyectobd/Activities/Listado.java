package com.example.proyectobd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyectobd.Adapters.JugadorAdapter;
import com.example.proyectobd.Data.Entity.JugadorCompleto;
import com.example.proyectobd.R;
import com.example.proyectobd.Repository.JugadorRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Listado extends AppCompatActivity implements JugadorAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private JugadorRepository jugadorRepository;
    private JugadorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        // Inicializar vistas
        recyclerView = findViewById(R.id.rvJugadores);
        progressBar = findViewById(R.id.progressBar);
        fabAdd = findViewById(R.id.fabAdd);

        // Configurar RecyclerView
        adapter = new JugadorAdapter(this, null, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Inicializar repositorio
        jugadorRepository = new JugadorRepository(this);

        // Cargar datos
        loadJugadores();

        // Configurar FAB
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(Listado.this, Formulario.class);
            startActivityForResult(intent, 100);
        });
    }

    private void loadJugadores() {
        progressBar.setVisibility(View.VISIBLE);

        try {
            jugadorRepository.getJugadoresCompletos(new JugadorRepository.DataCallback<List<JugadorCompleto>>() {
                @Override
                public void onDataLoaded(List<JugadorCompleto> data) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        if (data == null || data.isEmpty()) {
                            Toast.makeText(Listado.this, "No hay jugadores registrados", Toast.LENGTH_SHORT).show();
                        } else {
                            adapter.actualizarLista(data);
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Listado.this, "Error al cargar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Listado", "Error al cargar jugadores", e);
                    });
                }
            });
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Error inesperado: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("Listado", "Error inesperado", e);
        }
    }

    @Override
    public void onEditarClick(JugadorCompleto jugadorCompleto) {
        Intent intent = new Intent(Listado.this, Formulario.class);

        // Pasar todos los datos necesarios para edición
        intent.putExtra("jugador", jugadorCompleto.jugador);
        intent.putExtra("entrenador", jugadorCompleto.entrenador);
        intent.putExtra("contrato", jugadorCompleto.contrato);
        intent.putExtra("posicion", adapter.getPosition(jugadorCompleto));
        intent.putExtra("esEdicion", true);

        startActivityForResult(intent, 200); // Código diferente al de creación (100)
    }

    @Override
    public void onEliminarClick(JugadorCompleto jugador) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Eliminar a " + jugador.jugador.getNombre() + "?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    deleteJugador(jugador);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteJugador(JugadorCompleto jugador) {
        progressBar.setVisibility(View.VISIBLE);
        jugadorRepository.deleteJugador(jugador.jugador, new JugadorRepository.DataCallback<Void>() {
            @Override
            public void onDataLoaded(Void data) {
                progressBar.setVisibility(View.GONE);
                loadJugadores(); // Recargar lista
                Toast.makeText(Listado.this, "Jugador eliminado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(Listado.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Mostrar mensaje diferente según si fue creación o edición
            String mensaje = (requestCode == 100) ?
                    "Jugador creado correctamente" : "Jugador actualizado correctamente";
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
            loadJugadores();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadJugadores();
    }
}