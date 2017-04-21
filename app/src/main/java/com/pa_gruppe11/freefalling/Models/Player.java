package com.pa_gruppe11.freefalling.Models;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Player {

    private String participantId;
    private int placed;
    private PowerUp[] powerups;
    private Character character;
    private String displayName = "Default";
    private int deathCounter = 0;
    private long elapsedTime;


    public Player(String participantId, int placed, PowerUp[] powerups, Character character){
        this.participantId = participantId;
        this.placed = placed;
        this.powerups = powerups;
        this.character = character;
    }


    public Player(){

    }


    public void setCharacter(Character character){this.character = character;}

    public Character getCharacter(){return character;}

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }
    public String getParticipantId() {
        return participantId;
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        if(character != null)
            character.setDisplayName(displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void incrementDeaths() {
        deathCounter++;
    }
    
    public int getDeathCounter() {
        return deathCounter;
    }

    public void setDeathCounter(int deathCounter) {
        this.deathCounter = deathCounter;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }
}
