package com.pa_gruppe11.freefalling;

import android.graphics.*;
import android.hardware.camera2.params.MeteringRectangle;
import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Collidable implements Drawable{

    private int id;
    private int x;
    private int y;
    private float dx;
    private float dy;
    private int width;
    private int height;


    //Returns true if a collision occurs
    public boolean collides(){
        return true;
    }

    //Returns the next position to prepare for a collision
    public Rect getNextRect(){
        Rect rect = new Rect(x, y, x + width, y + width);
        return rect;
    }

    //Basic update function
    public void update(long dt){

    }


    @Override
    public void Drawable(int x, int y, int id) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(ResourceLoader.getInstance().getImageList().get(id), x, y);
    }

}
