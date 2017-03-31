package com.pa_gruppe11.freefalling.Models;

import android.widget.ImageView;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class GameMap {

    private int id;
    private ImageView image;
    private PowerUp[] powerups;
    private Obstacle[] obstacles;


    public GameMap(int id, ImageView image, PowerUp[] powerups, Obstacle[] obstacles){
        this.id = id;
        this.image = image;
        this.powerups = powerups;
        this.obstacles = obstacles;
    }


    public void update(long dt){

    }



}