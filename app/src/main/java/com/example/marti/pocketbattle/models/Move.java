package com.example.marti.pocketbattle.models;

import java.io.Serializable;

public class Move implements Serializable {

    public String identifier;
    public int accuracy;
    public int id;
    public int power;
    public int pp;
    public int ppLeft;
    public int type_id;

    public Pivot pivot;
    public Move() {
    }
}
