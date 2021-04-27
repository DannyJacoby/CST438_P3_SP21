package com.example.tatapi.models;

import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("Enemy")
public class Enemy extends ParseObject {

    private String name;
    private int health;
    private int overallHealth;
    private int strength;
    private int defense;
    private String description;

    public Enemy(){

    }

    public String getObjectId() {
        return getString("objectId");
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public int getHealth() {
        return getInt("health");
    }

    public void setHealth(int health) {
        put("health", health);
    }

    public int getOverallHealth() {
        return getInt("overallHealth");
    }

    public void setOverallHealth(int overallHealth) {
        put("health", overallHealth);
    }

    public int getStrength() {
        return getInt("strength");
    }

    public void setStrength(int strength) {
        put("strength", strength);
    }

    public int getDefense() {
        return getInt("defense");
    }

    public void setDefense(int defense) {
        put("defense", defense);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

}
