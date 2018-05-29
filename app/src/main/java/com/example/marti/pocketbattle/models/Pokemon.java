package com.example.marti.pocketbattle.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Pokemon implements Serializable{

    public int id;
    public String identifier;

    public int weight;
    public int height;


    public int level = 2;
    public int experience = 210;
    public int xpForNextLevel = 280;
    public int beginXpLevel = 190;

    public int base_experience;

    public int pointsAttack;
    public int pointsHitpoints;
    public int pointsDefence;

    ArrayList<PokemonType> pokemonTypes;

    public Pokemon(int id, String identifier) {
        this.id = id;
        this.identifier = identifier;
    }

    public Pokemon() {
    }
}
