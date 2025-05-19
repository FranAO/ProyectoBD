package com.example.proyectobd.Repository;

import android.content.Context;

import com.example.proyectobd.Data.Dao.ContratoDao;
import com.example.proyectobd.Data.Dao.JugadorDao;
import com.example.proyectobd.Data.Database.AppDatabase;
import com.example.proyectobd.Data.Entity.Contrato;
import com.example.proyectobd.Data.Entity.Jugador;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContratoRepository {
    private final ContratoDao contratoDao;
    private final JugadorDao jugadorDao;
    private final AppDatabase db;
    private final ExecutorService executorService;

    public ContratoRepository(Context context) {
        this.db = AppDatabase.getInstance(context);
        this.contratoDao = db.contratoDao();
        this.jugadorDao = db.jugadorDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public interface DataCallback<T> {
        void onDataLoaded(T data);
        void onError(Exception e);
    }

    // Operaciones básicas CRUD
    public void getAllContratos(DataCallback<List<Contrato>> callback) {
        executorService.execute(() -> {
            try {
                List<Contrato> data = contratoDao.getAllContratos();
                callback.onDataLoaded(data);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void getContratoById(int id, DataCallback<Contrato> callback) {
        executorService.execute(() -> {
            try {
                Contrato data = contratoDao.getContratoById(id);
                callback.onDataLoaded(data);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void insertContrato(Contrato contrato, DataCallback<Long> callback) {
        executorService.execute(() -> {
            try {
                long id = contratoDao.insertContrato(contrato);
                callback.onDataLoaded(id);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void updateContrato(Contrato contrato, DataCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                contratoDao.updateContrato(contrato);
                callback.onDataLoaded(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void deleteContrato(Contrato contrato, DataCallback<Void> callback) {
        executorService.execute(() -> {
            try {
                contratoDao.deleteContrato(contrato);
                callback.onDataLoaded(null);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    // Operaciones relacionadas
    public void getContratoByJugador(int jugadorId, DataCallback<Contrato> callback) {
        executorService.execute(() -> {
            try {
                Contrato data = contratoDao.getContratoByJugador(jugadorId);
                callback.onDataLoaded(data);
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void insertContratoCompleto(Jugador jugador, Contrato contrato, DataCallback<Long> callback) {
        executorService.execute(() -> {
            try {
                db.runInTransaction(() -> {
                    long jugadorId = jugadorDao.insertJugador(jugador);
                    contrato.setIdJugador((int) jugadorId); // Asigna el ID aquí
                    long contratoId = contratoDao.insertContrato(contrato);
                    callback.onDataLoaded(contratoId);
                });
            } catch (Exception e) {
                callback.onError(e);
            }
        });
    }

    public void close() {
        executorService.shutdown();
    }
}