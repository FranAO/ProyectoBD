package com.example.proyectobd.Repository;

import android.content.Context;

import com.example.proyectobd.Data.Dao.ContratoDao;
import com.example.proyectobd.Data.Dao.EntrenadorDao;
import com.example.proyectobd.Data.Dao.JugadorDao;
import com.example.proyectobd.Data.Database.AppDatabase;
import com.example.proyectobd.Data.Entity.Contrato;
import com.example.proyectobd.Data.Entity.Jugador;
import com.example.proyectobd.Data.Entity.JugadorCompleto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JugadorRepository {
    private final JugadorDao jugadorDao;
    private final ContratoDao contratoDao;
    private final EntrenadorDao entrenadorDao;
    private final ExecutorService executorService;
    private final AppDatabase db;

    public JugadorRepository(Context context) {
        this.db = AppDatabase.getInstance(context);
        this.jugadorDao = db.jugadorDao();
        this.contratoDao = db.contratoDao();
        this.entrenadorDao = db.entrenadorDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public interface DataCallback<T> {
        void onDataLoaded(T data);
        void onError(Exception e);
    }

    // Operaciones básicas CRUD
    public void getAllJugadores(DataCallback<List<Jugador>> callback) {
        executorService.execute(() -> {
            try {
                List<Jugador> data = jugadorDao.getAllJugadores();
                callback.onDataLoaded(data);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getJugadorById(int id, DataCallback<Jugador> callback) {
        executorService.execute(() -> {
            try {
                Jugador data = jugadorDao.getJugadorById(id);
                callback.onDataLoaded(data);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void insertJugador(Jugador jugador, DataCallback<Long> callback) {
        executorService.execute(() -> {
            try {
                long id = jugadorDao.insertJugador(jugador);
                callback.onDataLoaded(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void updateJugador(Jugador jugador, DataCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                jugadorDao.updateJugador(jugador);
                callback.onDataLoaded(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void deleteJugador(Jugador jugador, DataCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                jugadorDao.deleteJugador(jugador);
                callback.onDataLoaded(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    // Operaciones relacionadas
    public void getJugadoresCompletos(DataCallback<List<JugadorCompleto>> callback) {
        executorService.execute(() -> {
            try {
                List<Jugador> jugadores = jugadorDao.getAllJugadores();
                List<JugadorCompleto> jugadoresCompletos = new ArrayList<>();

                for (Jugador jugador : jugadores) {
                    JugadorCompleto completo = new JugadorCompleto();
                    completo.jugador = jugador;
                    completo.contrato = contratoDao.getContratoByJugador(jugador.getIdJugador());
                    // Asumiendo que tienes acceso al EntrenadorDao
                    completo.entrenador = db.entrenadorDao().getEntrenadorById(jugador.getIdEntrenador());
                    jugadoresCompletos.add(completo);
                }
                callback.onDataLoaded(jugadoresCompletos);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getJugadorCompleto(int jugadorId, DataCallback<JugadorCompleto> callback) {
        executorService.execute(() -> {
            try {
                Jugador jugador = jugadorDao.getJugadorById(jugadorId);
                Contrato contrato = contratoDao.getContratoByJugador(jugadorId);

                JugadorCompleto jugadorCompleto = new JugadorCompleto();
                jugadorCompleto.jugador = jugador;
                jugadorCompleto.contrato = contrato;

                callback.onDataLoaded(jugadorCompleto);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void jugadorTieneContrato(int jugadorId, DataCallback<Boolean> callback) {
        executorService.execute(() -> {
            try {
                int count = jugadorDao.tieneContrato(jugadorId);
                callback.onDataLoaded(count > 0);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void close() {
        executorService.shutdown();
    }
}