package com.example.tatapi.models;

import com.parse.ParseUser;

import java.util.Random;

public class User {
    private String username;
    private int health;
    private int overAllHealth;
    private int strength;
    private int defense;
    private int level;
    private int itemUses;

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
    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public int getLevel(){
        return level;
    }
    public void setLevel(int level){
        this.level = level;
    }

    public int getItemUses() {
        return itemUses;
    }
    public void setItemUses(int itemUses) {
        this.itemUses = itemUses;
    }

    public User(){

    }

    public void calcStats(int level){
        int baseHealth = this.overAllHealth;
        int baseStrength = this.strength;
        int baseDefense = this.defense;
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

        setOverAllHealth(baseHealth);
        //this was missing...
        setHealth(baseHealth);
        setStrength(baseStrength);
        setDefense(baseDefense);
        return;
    }
}
