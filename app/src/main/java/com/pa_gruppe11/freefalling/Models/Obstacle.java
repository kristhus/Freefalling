package com.pa_gruppe11.freefalling.Models;

import android.widget.ImageView;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Obstacle {

    private int type;
    private ImageView image;


    public Obstacle(int type, ImageView image){
        this.type = type;
        this.image = image;
    }


    public void update(long dt){

    }

}
