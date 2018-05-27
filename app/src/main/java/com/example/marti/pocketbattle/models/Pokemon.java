package com.example.marti.pocketbattle.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Pokemon implements Serializable{

    public int id;
    public String name;

    public int level = 2;
    public int experience = 210;
    public int xpForNextLevel = 280;
    public int beginXpLevel = 190;

    public int pointsAttack;
    public int pointsHitpoints;
    public int pointsDefence;

    ArrayList<PokemonType> pokemonTypes;

    public Pokemon(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
