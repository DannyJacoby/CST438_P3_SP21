package com.example.tatapi.db;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = AppDatabase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int mUserId;

    private String mUsername;
    private String mPassword;

    private int mLevel;
    private float mHealth;
    private float mOverAllHealth;
    private float mMagic;
    private float mStrength;

    public User(String mUsername, String mPassword, int mLevel){
        this.mUsername = mUsername;
        this.mPassword = mPassword;
        this.mLevel = mLevel;
        this.mHealth = 100;
        this.mOverAllHealth = getHealth();
        this.mMagic = 25;
        this.mStrength = 10;
    }

    public int getUserId() {
        return mUserId;
    }
    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getUsername() {
        return mUsername;
    }
    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getPassword() {
        return mPassword;
    }
    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public int getLevel() {
        return mLevel;
    }
    public void setLevel(int mLevel) {
        this.mLevel = mLevel;
    }

    public float getHealth() {
        return mHealth;
    }
    public void setHealth(float mHealth) {
        this.mHealth = mHealth;
    }

    public float getMagic() {
        return mMagic;
    }
    public void setMagic(float mMagic) {
        this.mMagic = mMagic;
    }

    public float getStrength() {
        return mStrength;
    }
    public void setStrength(float mStrength) {
        this.mStrength = mStrength;
    }

    public float getOverAllHealth() {
        return mOverAllHealth;
    }
    public void setOverAllHealth(float mOverAllHealth) {
        this.mOverAllHealth = mOverAllHealth;
    }

    public void calculateStats(){
        this.mHealth += getLevel() * 10;
        this.mMagic += getLevel() * 5;
        this.mStrength += getLevel() * 2;
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
        Log.d("EVENT", "YOU DIED");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return mUserId == user.mUserId &&
                mLevel == user.mLevel &&
                Float.compare(user.mHealth, mHealth) == 0 &&
                Float.compare(user.mOverAllHealth, mOverAllHealth) == 0 &&
                Float.compare(user.mMagic, mMagic) == 0 &&
                Float.compare(user.mStrength, mStrength) == 0 &&
                Objects.equals(mUsername, user.mUsername) &&
                Objects.equals(mPassword, user.mPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUserId, mUsername, mPassword, mLevel, mHealth, mOverAllHealth, mMagic, mStrength);
    }
}
