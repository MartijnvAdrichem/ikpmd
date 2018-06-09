package com.example.marti.pocketbattle.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Pokemon implements Serializable{

    public int id;
    public String identifier;
    public String key;

    public int weight;
    public int height;

    public int iv = 0;
    public int level = 0;
    public int experience = 0;
    public int xpForNextLevel = 0;
    public int beginXpLevel = 0;

    public int base_experience;

    public int attack;
    public int hp;
    public int currentHp;
    public int defence;

    public int base_attack;
    public int base_hp;
    public int base_defence;


    ArrayList<PokemonType> pokemonTypes;
   public ArrayList<Move> moves;


    public Pokemon(int id, String identifier) {
        this.id = id;
        this.identifier = identifier;
    }


    public Pokemon(Pokemon pokemon) {
        iv = ThreadLocalRandom.current().nextInt(0, 30);

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
        this.base_attack = pokemon.attack;
        this.base_hp = pokemon.hp;
        this.base_defence = pokemon.defence;
        this.pokemonTypes = pokemon.pokemonTypes;
        this.moves = pokemon.moves;
        levelup(true);
    }

    public Pokemon() {
    }

    public void levelUpMultiple(int amount){
        for (int i = 0; i < amount; i++) {
            levelup(true);
        }
    }

    public void levelup(boolean cheatXp){

        hp = ((iv + 2 * base_hp) * level/100 ) + 10 + level;
        defence = ((iv + 2 * base_defence) * level/100 ) + 5;
        attack = ((iv + 2 * base_hp) * level/100 ) + 5;

        level++;
        beginXpLevel = xpForNextLevel;
        xpForNextLevel += (int)(base_experience * (1.0 + (0.25 * level)));
        if(cheatXp){
            experience = beginXpLevel;
        }
    }

    public void addExperience(int xp){
        experience += xp;
        if(experience >= xpForNextLevel){
            levelup(false);
            //to check if pokemon gained multiple levels this fight
            addExperience(0);
        }
    }
}
