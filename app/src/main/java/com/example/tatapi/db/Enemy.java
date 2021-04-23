package com.example.tatapi.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Random;

@Entity(tableName = AppDatabase.ENEMY_TABLE)
public class Enemy {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int mEnemyId;
    // Stats
    private String mName;
    // keep track of max hp
    private float mHealth;
    // this will be used for current
    private float mOverAllHealth;
    private float mStrength;
    private float mDefense;
    // Other resources
    // might be nice to have a description for enemies
    private String mDescription;
    // if we implement items
    //private int mItemId;

    // these are all base things that should be brought down from the DB
    public Enemy(String mName, float mHealth, float mStrength, float mDefense){
        this.mName = mName;
        this.mHealth = mHealth;
        this.mOverAllHealth = getHealth();
        this.mStrength = mStrength;
        this.mDefense = mDefense;
    }

    public String getName() {return mName; }
    public void SetName(String mName) { this.mName = mName; }

    public int getEnemyId() {
        return mEnemyId;
    }
    public void setEnemyId(int mEnemyId) {
        this.mEnemyId = mEnemyId;
    }

    public float getHealth() {
        return mHealth;
    }
    public void setHealth(float mHealth) {
        this.mHealth = mHealth;
    }

    public float getStrength() {
        return mStrength;
    }
    public void setStrength(float mStrength) {
        this.mStrength = mStrength;
    }

    public float getDefense() {
        return mDefense;
    }
    public void setDefense(float mDefense) {
        this.mDefense = mDefense;
    }

    public float getOverAllHealth() {
        return mOverAllHealth;
    }
    public void setOverAllHealth(float mOverAllHealth) {
        this.mOverAllHealth = mOverAllHealth;
    }

    public String getDescription(){ return mDescription; }
    public void setDescription(String mDescription){ this.mDescription = mDescription; }

    public void calculateStats(){
        // never done random generation with this before, hope I set it up right
        // set it up as ints just for nice whole numbers
        // tweak as necessary
        Random rand_health = new Random();
        rand_health.ints(1, 7);
        Random rand_strength = new Random();
        rand_strength.ints(1, 5);
        Random rand_defense = new Random();
        rand_defense.ints(1, 4);
        this.mHealth += rand_health.nextInt();
        this.mStrength += rand_strength.nextInt();
        this.mDefense += rand_defense.nextInt();
    }

    public void takeDamage(float damage){
        if(mHealth - damage <= 0){
            die();
        }
        this.mHealth -= damage;
    }

    public float dealDamage(){
        return 0.1f;
    }

    public void die(){
        Log.d("EVENT", mName + " DIED");
    }
}
