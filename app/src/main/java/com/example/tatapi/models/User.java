package com.example.tatapi.models;

import com.parse.ParseUser;

public class User {
    private String username;
    private int health;
    private int overAllHealth;
    private int strength;
    private int defense;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getOverAllHealth() {
        return overAllHealth;
    }

    public void setOverAllHealth(int overAllHealth) {
        this.overAllHealth = overAllHealth;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }
    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username = username;}

    public User(){

    }
}
