package com.pa_gruppe11.freefalling.Models;

import android.widget.ImageView;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Character {

    private int dt;
    ImageView image;

    public Character(int dt, ImageView image){
        this.dt = dt;
        this.image = image
    }


    public void update(long dt){

    }

}
