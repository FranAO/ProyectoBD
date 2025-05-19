package com.example.proyectobd.Data.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.proyectobd.Data.Entity.Entrenador;

import java.util.List;

@Dao
public interface EntrenadorDao {
    @Query("SELECT * FROM entrenadores ORDER BY idEntrenador DESC")
    List<Entrenador> getAllEntrenadores();

    @Query("SELECT * FROM entrenadores WHERE idEntrenador = :id")
    Entrenador getEntrenadorById(int id);

    @Insert
    long insertEntrenador(Entrenador entrenador);

    @Update
    void updateEntrenador(Entrenador entrenador);

    @Delete
    void deleteEntrenador(Entrenador entrenador);

    @Query("DELETE FROM entrenadores WHERE idEntrenador = :id")
    void deleteEntrenadorById(int id);

    @Query("SELECT COUNT(*) FROM jugadores WHERE idEntrenador = :entrenadorId")
    int countJugadoresByEntrenador(int entrenadorId);
}
