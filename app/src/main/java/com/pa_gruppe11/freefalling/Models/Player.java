package com.pa_gruppe11.freefalling.Models;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Player {

    private String publicid;
    private int placed;
    private PowerUp[] powerups;
    private Character character;



    public Player(String publicid, int placed, PowerUp[] powerups, Character character){
        this.publicid = publicid;
        this.placed = placed;
        this.powerups = powerups;
        this.character = character;
    }


    public Player(){

    }


    public void setCharacter(Character character){this.character = character;}

    public Character getCharacter(){return character;}

}
