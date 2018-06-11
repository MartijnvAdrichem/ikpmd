package com.example.marti.pocketbattle.models;

import java.util.Comparator;

public class User implements Comparable<User> {

    public String username;
    public int level;

    public int currentXp;
    public int nextLevelXp;
    public int coins;


    public void addXP(int amt){
        currentXp += amt;
        if(currentXp >= nextLevelXp){
            levelup();
        }
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

    public String toString() {
        return "[name: " + username + ", xp: " + currentXp + "]";
    }

    @Override
    public int compareTo(User o) {
        return Integer.valueOf(currentXp).compareTo(o.currentXp);
    }

    public int getCurrentXp() {
        return currentXp;
    }

    public static Comparator<User> UsrSorting = (s1, s2) -> {

        int xp1 = s1.getCurrentXp();
        int xp2 = s2.getCurrentXp();

        return xp2-xp1;

    };

}
