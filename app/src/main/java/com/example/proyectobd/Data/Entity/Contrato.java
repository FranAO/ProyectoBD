package com.example.proyectobd.Data.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
        tableName = "contratos",
        foreignKeys = @ForeignKey(
                entity = Jugador.class,
                parentColumns = "idJugador",
                childColumns = "idJugador",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index(value = "idJugador", unique = true) // Relación 1–1
)
public class Contrato implements Serializable
{
    @PrimaryKey(autoGenerate = true)
    private int idContrato;

    private double sueldo;
    private String fechaInicio;
    private String fechaFin;
    private String clausulaRescision;
    private int idJugador;

    public int getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(int idContrato) {
        this.idContrato = idContrato;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getClausulaRescision() {
        return clausulaRescision;
    }

    public void setClausulaRescision(String clausulaRescision) {
        this.clausulaRescision = clausulaRescision;
    }

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }
}

