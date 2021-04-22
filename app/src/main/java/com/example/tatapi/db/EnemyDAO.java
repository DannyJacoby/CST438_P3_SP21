package com.example.tatapi.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EnemyDAO {

    @Insert
    void insert(Enemy...enemies);

    @Update
    void update(Enemy...enemies);

    @Delete
    void delete(Enemy...enemies);

    // doubt we need this, not sure that we're even storing more than one enemy
    // unless we want to keep a record of all the ones the player defeated or something
    @Query("SELECT * FROM " + AppDatabase.ENEMY_TABLE)
    List<Enemy> getAllEnemies();

    @Query("SELECT * FROM " + AppDatabase.ENEMY_TABLE + " WHERE mName = :name")
    Enemy getEnemyByName(String name);

    @Query("SELECT * FROM " + AppDatabase.ENEMY_TABLE + " WHERE mEnemyId = :enemyId")
    Enemy getEnemyByEnemyId(int enemyId);

}
