package com.example.proyectobd.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectobd.Data.Dao.ContratoDao;
import com.example.proyectobd.Data.Dao.JugadorDao;
import com.example.proyectobd.Data.Database.AppDatabase;
import com.example.proyectobd.Data.Entity.Contrato;
import com.example.proyectobd.Data.Entity.Entrenador;
import com.example.proyectobd.Data.Entity.Jugador;
import com.example.proyectobd.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Formulario extends AppCompatActivity {

    private JugadorDao jugadorDao;
    private ContratoDao contratoDao;
    private AppDatabase db;
    // Views Jugador
    private TextInputEditText etNombre, etEmail, etEdad, etPosicion;
    private TextInputLayout tilNombre, tilEmail, tilEdad, tilPosicion;

    // Views Entrenador
    private TextInputEditText etEntrenador, etEspecialidad, etExperiencia;
    private TextInputLayout tilEntrenador, tilEspecialidad, tilExperiencia;

    // Views Contrato
    private TextInputEditText etSueldo, etFechaInicio, etFechaFin, etClausula;
    private TextInputLayout tilSueldo, tilFechaInicio, tilFechaFin, tilClausula;

    private Button btnGuardar, btnCancelar;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean esEdicion = false;
    private int posicionJugador = -1;
    private Jugador jugadorActual;
    private Entrenador entrenadorActual;
    private Contrato contratoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        db = AppDatabase.getInstance(this);
        jugadorDao = db.jugadorDao();
        contratoDao = db.contratoDao();

        inicializarVistas();
        cargarDatosSiEsEdicion();
        configurarBotones();
    }

    private void inicializarVistas() {
        // Jugador
        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        etEdad = findViewById(R.id.etEdad);
        etPosicion = findViewById(R.id.etPosicion);

        tilNombre = findViewById(R.id.tilNombre);
        tilEmail = findViewById(R.id.tilEmail);
        tilEdad = findViewById(R.id.tilEdad);
        tilPosicion = findViewById(R.id.tilPosicion);

        // Entrenador
        etEntrenador = findViewById(R.id.etEntrenador);
        etEspecialidad = findViewById(R.id.etEspecialidad);
        etExperiencia = findViewById(R.id.etExperiencia);

        tilEntrenador = findViewById(R.id.tilEntrenador);
        tilEspecialidad = findViewById(R.id.tilEspecialidad);
        tilExperiencia = findViewById(R.id.tilExperiencia);

        // Contrato
        etSueldo = findViewById(R.id.etSueldo);
        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        etClausula = findViewById(R.id.etClausula);

        tilSueldo = findViewById(R.id.tilSueldo);
        tilFechaInicio = findViewById(R.id.tilFechaInicio);
        tilFechaFin = findViewById(R.id.tilFechaFin);
        tilClausula = findViewById(R.id.tilClausula);

        // Botones
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);
    }

    private void cargarDatosSiEsEdicion() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jugadorActual = (Jugador) extras.getSerializable("jugador");
            entrenadorActual = (Entrenador) extras.getSerializable("entrenador");
            contratoActual = (Contrato) extras.getSerializable("contrato");
            posicionJugador = extras.getInt("posicion", -1);

            if (jugadorActual != null && entrenadorActual != null && contratoActual != null) {
                esEdicion = true;

                // Cargar datos del jugador
                etNombre.setText(jugadorActual.getNombre());
                etEmail.setText(jugadorActual.getEmail());
                etEdad.setText(String.valueOf(jugadorActual.getEdad()));
                etPosicion.setText(jugadorActual.getPosicion());

                // Cargar datos del entrenador
                etEntrenador.setText(entrenadorActual.getNombre());
                etEspecialidad.setText(entrenadorActual.getEspecialidad());
                etExperiencia.setText(String.valueOf(entrenadorActual.getAniosExperiencia()));

                // Cargar datos del contrato
                etSueldo.setText(String.valueOf(contratoActual.getSueldo()));
                etFechaInicio.setText(contratoActual.getFechaInicio());
                etFechaFin.setText(contratoActual.getFechaFin());
                etClausula.setText(contratoActual.getClausulaRescision());
            }
        }
    }
    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
    private void configurarBotones() {
        btnCancelar.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        btnGuardar.setOnClickListener(v -> {
            try {
                if (validarFormulario()) {
                    guardarDatos();
                    mostrarError("Datos guardados correctamente"); // Feedback al usuario
                } else {
                    mostrarError("Por favor corrige los errores en el formulario");
                }
            } catch (NumberFormatException e) {
                mostrarError("Error en formato numérico: " + e.getMessage());
            } catch (Exception e) {
                mostrarError("Error al guardar: " + e.getMessage());
            }
        });
    }

    private boolean validarFormulario() {
        boolean valido = true;

        // Validar Jugador
        if (etNombre.getText().toString().trim().isEmpty()) {
            tilNombre.setError("Nombre obligatorio");
            valido = false;
        } else {
            tilNombre.setError(null);
        }

        if (etEmail.getText().toString().trim().isEmpty()) {
            tilEmail.setError("Email obligatorio");
            valido = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            tilEmail.setError("Email inválido");
            valido = false;
        } else {
            tilEmail.setError(null);
        }

        if (etEdad.getText().toString().trim().isEmpty()) {
            tilEdad.setError("Edad obligatoria");
            valido = false;
        } else {
            try {
                int edad = Integer.parseInt(etEdad.getText().toString());
                if (edad < 15 || edad > 50) {
                    tilEdad.setError("Edad debe ser entre 15 y 50");
                    valido = false;
                } else {
                    tilEdad.setError(null);
                }
            } catch (NumberFormatException e) {
                tilEdad.setError("Edad debe ser un número");
                valido = false;
            }
        }

        if (etPosicion.getText().toString().trim().isEmpty()) {
            tilPosicion.setError("Posición obligatoria");
            valido = false;
        } else {
            tilPosicion.setError(null);
        }

        // Validar Entrenador
        if (etEntrenador.getText().toString().trim().isEmpty()) {
            tilEntrenador.setError("Nombre obligatorio");
            valido = false;
        } else {
            tilEntrenador.setError(null);
        }

        if (etEspecialidad.getText().toString().trim().isEmpty()) {
            tilEspecialidad.setError("Especialidad obligatoria");
            valido = false;
        } else {
            tilEspecialidad.setError(null);
        }

        if (etExperiencia.getText().toString().trim().isEmpty()) {
            tilExperiencia.setError("Experiencia obligatoria");
            valido = false;
        } else {
            try {
                int experiencia = Integer.parseInt(etExperiencia.getText().toString());
                if (experiencia < 0) {
                    tilExperiencia.setError("Experiencia no puede ser negativa");
                    valido = false;
                } else {
                    tilExperiencia.setError(null);
                }
            } catch (NumberFormatException e) {
                tilExperiencia.setError("Experiencia debe ser un número");
                valido = false;
            }
        }

        // Validar Contrato
        if (etSueldo.getText().toString().trim().isEmpty()) {
            tilSueldo.setError("Sueldo obligatorio");
            valido = false;
        } else {
            try {
                double sueldo = Double.parseDouble(etSueldo.getText().toString());
                if (sueldo <= 0) {
                    tilSueldo.setError("Sueldo debe ser mayor a 0");
                    valido = false;
                } else {
                    tilSueldo.setError(null);
                }
            } catch (NumberFormatException e) {
                tilSueldo.setError("Sueldo debe ser un número");
                valido = false;
            }
        }

        if (etFechaInicio.getText().toString().trim().isEmpty()) {
            tilFechaInicio.setError("Fecha inicio obligatoria");
            valido = false;
        } else if (!isValidDate(etFechaInicio.getText().toString())) {
            tilFechaInicio.setError("Formato de fecha inválido (dd/MM/yyyy)");
            valido = false;
        } else {
            tilFechaInicio.setError(null);
        }

        if (etFechaFin.getText().toString().trim().isEmpty()) {
            tilFechaFin.setError("Fecha fin obligatoria");
            valido = false;
        } else if (!isValidDate(etFechaFin.getText().toString())) {
            tilFechaFin.setError("Formato de fecha inválido (dd/MM/yyyy)");
            valido = false;
        } else {
            tilFechaFin.setError(null);
        }

        if (etClausula.getText().toString().trim().isEmpty()) {
            tilClausula.setError("Cláusula obligatoria");
            valido = false;
        } else {
            try {
                double clausula = Double.parseDouble(etClausula.getText().toString());
                if (clausula <= 0) {
                    tilClausula.setError("Cláusula debe ser mayor a 0");
                    valido = false;
                } else {
                    tilClausula.setError(null);
                }
            } catch (NumberFormatException e) {
                tilClausula.setError("Cláusula debe ser un número");
                valido = false;
            }
        }

        return valido;
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateStr);
            return date != null && dateStr.equals(sdf.format(date));
        } catch (ParseException e) {
            return false;
        }
    }

    private void guardarDatos() {
        if (!validarFormulario()) {
            mostrarError("Corrige los errores en el formulario");
            return;
        }

        executor.execute(() -> {
            try {
                db.runInTransaction(() -> {
                    // 1. Manejar Entrenador primero (si es necesario)
                    Entrenador entrenador;
                    if (esEdicion && entrenadorActual != null) {
                        entrenador = entrenadorActual;
                        entrenador.setNombre(etEntrenador.getText().toString().trim());
                        entrenador.setEspecialidad(etEspecialidad.getText().toString().trim());
                        entrenador.setAniosExperiencia(Integer.parseInt(etExperiencia.getText().toString()));
                        db.entrenadorDao().updateEntrenador(entrenador);
                    } else {
                        entrenador = new Entrenador();
                        entrenador.setNombre(etEntrenador.getText().toString().trim());
                        entrenador.setEspecialidad(etEspecialidad.getText().toString().trim());
                        entrenador.setAniosExperiencia(Integer.parseInt(etExperiencia.getText().toString()));
                        long idEntrenador = db.entrenadorDao().insertEntrenador(entrenador);
                        entrenador.setIdEntrenador((int) idEntrenador);
                    }

                    // 2. Manejar Jugador
                    Jugador jugador;
                    if (esEdicion && jugadorActual != null) {
                        jugador = jugadorActual;
                        jugador.setNombre(etNombre.getText().toString().trim());
                        jugador.setEmail(etEmail.getText().toString().trim());
                        jugador.setEdad(Integer.parseInt(etEdad.getText().toString()));
                        jugador.setPosicion(etPosicion.getText().toString().trim());
                        jugador.setIdEntrenador(entrenador.getIdEntrenador()); // Relación con entrenador
                        jugadorDao.updateJugador(jugador);
                    } else {
                        jugador = new Jugador();
                        jugador.setNombre(etNombre.getText().toString().trim());
                        jugador.setEmail(etEmail.getText().toString().trim());
                        jugador.setEdad(Integer.parseInt(etEdad.getText().toString()));
                        jugador.setPosicion(etPosicion.getText().toString().trim());
                        jugador.setIdEntrenador(entrenador.getIdEntrenador()); // Relación con entrenador
                        long idJugador = jugadorDao.insertJugador(jugador);
                        jugador.setIdJugador((int) idJugador);
                    }

                    // 3. Manejar Contrato
                    Contrato contrato;
                    if (esEdicion && contratoActual != null) {
                        contrato = contratoActual;
                        contrato.setIdJugador(jugador.getIdJugador()); // Asegurar relación
                        contrato.setSueldo(Double.parseDouble(etSueldo.getText().toString()));
                        contrato.setFechaInicio(etFechaInicio.getText().toString().trim());
                        contrato.setFechaFin(etFechaFin.getText().toString().trim());
                        contrato.setClausulaRescision(etClausula.getText().toString());
                        contratoDao.updateContrato(contrato);
                    } else {
                        contrato = new Contrato();
                        contrato.setIdJugador(jugador.getIdJugador()); // Establecer relación
                        contrato.setSueldo(Double.parseDouble(etSueldo.getText().toString()));
                        contrato.setFechaInicio(etFechaInicio.getText().toString().trim());
                        contrato.setFechaFin(etFechaFin.getText().toString().trim());
                        contrato.setClausulaRescision(etClausula.getText().toString());
                        contratoDao.insertContrato(contrato);
                    }

                    runOnUiThread(() -> {
                        Intent resultado = new Intent();
                        resultado.putExtra("jugador", jugador);
                        resultado.putExtra("entrenador", entrenador);
                        resultado.putExtra("contrato", contrato);
                        if (esEdicion) resultado.putExtra("posicion", posicionJugador);
                        setResult(RESULT_OK, resultado);
                        finish();
                    });
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Log.e("GuardarDatos", "Error completo:", e);
                    mostrarError("Error crítico: " + e.getClass().getSimpleName() + ": " + e.getMessage());
                });
            }
        });
    }

    // Añade esto en onDestroy()
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}