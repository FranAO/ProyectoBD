package com.example.proyectobd.Data.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proyectobd.Data.Entity.Contrato;

import java.util.List;

@Dao
public interface ContratoDao {
    @Query("SELECT * FROM contratos ORDER BY idContrato DESC")
    List<Contrato> getAllContratos();

    @Query("SELECT * FROM contratos WHERE idContrato = :id")
    Contrato getContratoById(int id);

    // Especial para relación 1-1 con Jugador
    @Query("SELECT * FROM contratos WHERE idJugador = :jugadorId")
    Contrato getContratoByJugador(int jugadorId);

    @Insert
    long insertContrato(Contrato contrato);

    @Update
    void updateContrato(Contrato contrato);

    @Delete
    void deleteContrato(Contrato contrato);

    @Query("DELETE FROM contratos WHERE idContrato = :id")
    void deleteContratoById(int id);

    @Query("DELETE FROM contratos WHERE idJugador = :jugadorId")
    void deleteContratoByJugador(int jugadorId);
}
