package com.example.proyectobd.Repository;

import android.content.Context;

import com.example.proyectobd.Data.Dao.EntrenadorDao;
import com.example.proyectobd.Data.Dao.JugadorDao;
import com.example.proyectobd.Data.Database.AppDatabase;
import com.example.proyectobd.Data.Entity.Entrenador;
import com.example.proyectobd.Data.Entity.Jugador;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EntrenadorRepository {
    private final EntrenadorDao entrenadorDao;
    private final JugadorDao jugadorDao;
    private final ExecutorService executorService;

    public EntrenadorRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        entrenadorDao = db.entrenadorDao();
        jugadorDao = db.jugadorDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public interface DataCallback<T> {
        void onDataLoaded(T data);
        void onError(Exception e);
    }

    // Operaciones básicas CRUD
    public void getAllEntrenadores(DataCallback<List<Entrenador>> callback) {
        executorService.execute(() -> {
            try {
                List<Entrenador> data = entrenadorDao.getAllEntrenadores();
                callback.onDataLoaded(data);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getEntrenadorById(int id, DataCallback<Entrenador> callback) {
        executorService.execute(() -> {
            try {
                Entrenador data = entrenadorDao.getEntrenadorById(id);
                callback.onDataLoaded(data);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void insertEntrenador(Entrenador entrenador, DataCallback<Long> callback) {
        executorService.execute(() -> {
            try {
                long id = entrenadorDao.insertEntrenador(entrenador);
                callback.onDataLoaded(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void updateEntrenador(Entrenador entrenador, DataCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                entrenadorDao.updateEntrenador(entrenador);
                callback.onDataLoaded(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void deleteEntrenador(Entrenador entrenador, DataCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                entrenadorDao.deleteEntrenador(entrenador);
                callback.onDataLoaded(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    // Operaciones relacionadas
    public void getEntrenadorConJugadores(int entrenadorId, DataCallback<EntrenadorConJugadores> callback) {
        executorService.execute(() -> {
            try {
                Entrenador entrenador = entrenadorDao.getEntrenadorById(entrenadorId);
                List<Jugador> jugadores = jugadorDao.getJugadoresByEntrenador(entrenadorId);

                EntrenadorConJugadores resultado = new EntrenadorConJugadores();
                resultado.entrenador = entrenador;
                resultado.jugadores = jugadores;

                callback.onDataLoaded(resultado);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void countJugadoresByEntrenador(int entrenadorId, DataCallback<Integer> callback) {
        executorService.execute(() -> {
            try {
                int count = entrenadorDao.countJugadoresByEntrenador(entrenadorId);
                callback.onDataLoaded(count);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void close() {
        executorService.shutdown();
    }

    // Clase POJO para relación entrenador-jugadores
    public static class EntrenadorConJugadores {
        public Entrenador entrenador;
        public List<Jugador> jugadores;
    }
}