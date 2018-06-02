package com.example.marti.pocketbattle.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Pokemon implements Serializable{

    public int id;
    public String identifier;
    public String key;

    public int weight;
    public int height;


    public int level = 1;
    public int experience = 0;
    public int xpForNextLevel = 0;
    public int beginXpLevel = 0;

    public int base_experience;

    public int attack;
    public int hp;
    public int defence;

    ArrayList<PokemonType> pokemonTypes;
   public ArrayList<Move> moves;


    public Pokemon(int id, String identifier) {
        this.id = id;
        this.identifier = identifier;
    }


    public Pokemon(Pokemon pokemon) {
        this.id = pokemon.id;
        this.identifier = pokemon.identifier;
        this.key = pokemon.key;
        this.weight = pokemon.weight;
        this.height = pokemon.height;
        this.level = pokemon.level;
        this.experience = pokemon.experience;
        this.xpForNextLevel = pokemon.xpForNextLevel;
        this.beginXpLevel = pokemon.beginXpLevel;
        this.base_experience = pokemon.base_experience;
        this.attack = pokemon.attack;
        this.hp = pokemon.hp;
        this.defence = pokemon.defence;
        this.pokemonTypes = pokemon.pokemonTypes;
        this.moves = pokemon.moves;
    }

    public Pokemon() {
    }
}
