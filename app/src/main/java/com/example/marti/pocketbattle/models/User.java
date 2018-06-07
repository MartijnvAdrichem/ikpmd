package com.example.marti.pocketbattle.models;

public class User {

    public String username;
    public int level;
    public int currentXp;
    public int nextLevelXp;


    public void addXP(int amt){
        currentXp += amt;
        if(currentXp >= nextLevelXp){
            levelup();
        }
    }

    public void levelup(){

        if(currentXp >= nextLevelXp){
            level += 1;
            currentXp -= nextLevelXp;
            nextLevelXp = (int)(100 * (1 + (level * (1 + level * 0.25))));
            levelup();
        }
    }
}
