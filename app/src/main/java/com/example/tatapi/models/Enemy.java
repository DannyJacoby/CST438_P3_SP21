package com.example.tatapi.models;

import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.Random;

@ParseClassName("Enemy")
public class Enemy extends ParseObject {

    private String name;
    private int health;
    private int overallHealth;
    private int strength;
    private int defense;
    private String description;

    public Enemy(){
        this.name = "";
        this.health = 0;
        this.overallHealth = 0;
        this.strength = 0;
        this.defense = 0;
        this.description = "";
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
        put("overallHealth", overallHealth);
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

    public void calcStats(int level){
        int baseHealth = getOverallHealth();
        int baseStrength = getStrength();
        int baseDefense = getDefense();
        Random randomGen = new Random();
        //range is 0 to 5, they should get at least 1 point
        int randomHealthModifier = randomGen.nextInt(5);
        if(randomHealthModifier == 0){
            randomHealthModifier += 1;
        }

        int randomStrengthModifier = randomGen.nextInt(5);
        if(randomStrengthModifier == 0){
            randomStrengthModifier += 1;
        }

        int randomDefenseModifier = randomGen.nextInt(5);
        if(randomDefenseModifier == 0){
            randomDefenseModifier += 1;
        }
        /*so formula is:
        base stat + (randomStat * level)
        * */
        baseHealth += (randomHealthModifier * level);
        baseStrength += (randomStrengthModifier * level);
        baseDefense += (randomDefenseModifier * level);

        setOverallHealth(baseHealth);
        setHealth(baseHealth);
        setStrength(baseStrength);
        setDefense(baseDefense);
        return;
    }
}
