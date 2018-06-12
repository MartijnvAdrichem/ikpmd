package com.example.marti.pocketbattle.models;

public class User {

    public String username;
    public int level;
    public int currentXp;
    public int nextLevelXp;
    public int coins;
    public int wins;
    public int loses;
    public int damageDone;
    public int damageTaken;


    public void addXP(int amt){
        currentXp += amt;
        if(currentXp >= nextLevelXp){
            levelup();
        }
    }

    public void addWin(){
        wins++;
    }

    public void addLose(){
        loses++;
    }

    public void addDamageDone(int dmg){
        damageDone += dmg;
    }

    public void addDamageTaken(int dmg){
        damageTaken += dmg;
    }

    User(){

    }

    public void levelup(){

        if(currentXp >= nextLevelXp){
            level += 1;
            currentXp -= nextLevelXp;
            nextLevelXp = (int)(100 * (1 + (level * (1 + level * 0.25))));
            coins += 100;
            levelup();
        }
    }
}
