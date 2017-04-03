package com.pa_gruppe11.freefalling.Models;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Drawable;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class GameMap implements Drawable {

    private int id;
    private ImageView image;
    private PowerUp[] powerups;
    private Obstacle[] obstacles;
    private int x;
    private int y;


    public GameMap(int id, ImageView image, PowerUp[] powerups, Obstacle[] obstacles){
        this.id = id;
        this.image = image;
        this.powerups = powerups;
        this.obstacles = obstacles;
    }


    public void update(long dt){

    }


    @Override
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