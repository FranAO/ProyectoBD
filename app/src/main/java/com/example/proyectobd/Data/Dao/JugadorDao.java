package com.example.proyectobd.Data.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.proyectobd.Data.Entity.Jugador;
import com.example.proyectobd.Data.Entity.JugadorCompleto;

import java.util.List;

@Dao
public interface JugadorDao {
    // Consultas básicas
    @Query("SELECT * FROM jugadores ORDER BY idJugador DESC")
    List<Jugador> getAllJugadores();

    @Query("SELECT * FROM jugadores WHERE idJugador = :id")
    Jugador getJugadorById(int id);

    @Insert
    long insertJugador(Jugador jugador);

    @Update
    void updateJugador(Jugador jugador);

    @Delete
    void deleteJugador(Jugador jugador);

    @Query("DELETE FROM jugadores WHERE idJugador = :id")
    void deleteJugadorById(int id);

    @Transaction
    @Query("SELECT * FROM jugadores WHERE idEntrenador = :entrenadorId")
    List<Jugador> getJugadoresByEntrenador(int entrenadorId);

    @Transaction
    @Query("SELECT * FROM jugadores")
    List<JugadorCompleto> getJugadoresCompletos();

    @Query("SELECT COUNT(*) FROM contratos WHERE idJugador = :jugadorId")
    int tieneContrato(int jugadorId);
}
