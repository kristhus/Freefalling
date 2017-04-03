package com.pa_gruppe11.freefalling;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Collidable implements Drawable {

    private int id;
    private int x;
    private int y;
    private float dx;
    private float dy;
    private int height;
    private int width;


    public Collidable(int height, int width){
        this.height = height;
        this.width = width;
    }


    public boolean collides(){
        //some condition
        return true;
    }

    public Rect getNextRect(){
        Rect rect = new Rect(x, y, x + width, y + height);
        return rect;
    }

    public void update(long dt){

    }


    public void Drawable(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(ResourceLoader.getInstance().getImageList().get(id), x, y, new Paint());
    }

}
