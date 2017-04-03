package com.pa_gruppe11.freefalling.Models;

import android.graphics.Canvas;
import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Drawable;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Character implements Drawable{

    private int dt;
    private ImageView image;

    public Character(int dt, ImageView image){
        this.dt = dt;
        this.image = image;
    }


    public void update(long dt){

    }

    public void Drawable(int x, int y, int id) {

    }

    @Override
    public void draw(Canvas canvas) {

    }
}
