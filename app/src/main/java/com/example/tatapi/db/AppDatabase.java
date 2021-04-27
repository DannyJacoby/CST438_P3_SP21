package com.example.tatapi.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Enemy.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "db-local";
    public static final String USER_TABLE = "user_table";
    public static final String ENEMY_TABLE = "enemy_table";
    // insert more table names here
    public abstract UserDAO getUserDAO();
    public abstract  EnemyDAO getEnemyDAO();
    // insert more DAOs here

}
