package com.example.proyectobd.Data.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        tableName = "jugadores",
        foreignKeys = @ForeignKey(
                entity = Entrenador.class,
                parentColumns = "idEntrenador",
                childColumns = "idEntrenador",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("idEntrenador")
)
public class Jugador implements Serializable
{

    @PrimaryKey(autoGenerate = true)
    private int idJugador;

    private String nombre;
    private int edad;
    private String posicion;
    private String email;
    private int idEntrenador;

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdEntrenador() {
        return idEntrenador;
    }

    public void setIdEntrenador(int idEntrenador) {
        this.idEntrenador = idEntrenador;
    }
}