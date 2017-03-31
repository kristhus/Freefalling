package com.pa_gruppe11.freefalling.Models;

import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Collidable;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Obstacle extends Collidable{

    private int type;
    private ImageView image;


    public Obstacle(int type, ImageView image, int height, int width){
        super(height, width);
        this.type = type;
        this.image = image;
    }


    public void update(long dt){

    }

}
