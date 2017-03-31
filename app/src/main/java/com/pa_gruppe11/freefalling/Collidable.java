package com.pa_gruppe11.freefalling;

import android.graphics.*;
import android.hardware.camera2.params.MeteringRectangle;
import android.widget.ImageView;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Collidable {

    private int x;
    private int y;
    private float dx;
    private float dy;
    private int width;
    private int height;


    public Collidable(int width, int height, int x, int y){
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
    }


    public boolean collides(){
        return true;
    }


    public Rect getNextRect(){
        Rect rect = new Rect(x, y, x + width, y + width);
        return rect;
    }


    public void update(long dt){

    }





}
