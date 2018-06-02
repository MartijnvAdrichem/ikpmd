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

    public void levelUpMultiple(int amount){
        for (int i = 0; i < amount; i++) {
            levelup(true);
        }
    }

    public void levelup(boolean cheatXp){
        for (int i = 0; i < (level > 50 ? 50 : level); i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 3);
            if(randomNum == 0){
                hp +=  1;
            } else if(randomNum == 1){
                attack += 1;
            } else if(randomNum == 2){
                defence += 1;
            }
        }
        level++;
        beginXpLevel = xpForNextLevel;
        xpForNextLevel += (int)(base_experience * (1 + (0.25 * level)));
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
