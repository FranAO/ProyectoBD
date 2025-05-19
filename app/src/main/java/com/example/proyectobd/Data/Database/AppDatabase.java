package com.example.proyectobd.Data.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.proyectobd.Data.Dao.ContratoDao;
import com.example.proyectobd.Data.Dao.EntrenadorDao;
import com.example.proyectobd.Data.Dao.JugadorDao;
import com.example.proyectobd.Data.Entity.Contrato;
import com.example.proyectobd.Data.Entity.Entrenador;
import com.example.proyectobd.Data.Entity.Jugador;

@Database(
        entities = {Jugador.class, Contrato.class, Entrenador.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "futbol_db";

    private static AppDatabase instance;

    // DAOs expuestos por la base de datos
    public abstract JugadorDao jugadorDao();
    public abstract ContratoDao contratoDao();
    public abstract EntrenadorDao entrenadorDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
