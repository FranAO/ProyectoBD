package com.example.proyectobd.Data.Entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;

public class JugadorCompleto implements Serializable
{
    @Embedded
    public Jugador jugador;

    @Relation(
            parentColumn = "idEntrenador",
            entityColumn = "idEntrenador"
    )
    public Entrenador entrenador;

    @Relation(
            parentColumn = "idJugador",
            entityColumn = "idJugador"
    )
    public Contrato contrato;
}
