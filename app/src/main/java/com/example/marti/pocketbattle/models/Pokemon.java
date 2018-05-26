package com.example.marti.pocketbattle.models;

import java.util.ArrayList;

public class Pokemon {

    public int id;
   public String name;

    int level;
    int experience;
    int xpForNextLevel;

    int pointsAttack;
    int pointsAccuracy;
    int pointsHitpoints;
    int pointsDefence;

    ArrayList<PokemonType> pokemonTypes;

    public Pokemon(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
