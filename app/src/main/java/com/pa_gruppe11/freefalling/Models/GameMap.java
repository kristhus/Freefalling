package com.pa_gruppe11.freefalling.Models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Drawable;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class GameMap implements Drawable {

    protected int id;
    protected Bitmap image;
    protected Matrix transformationMatrix;
    protected PowerUp[] powerups;
    protected Obstacle[] obstacles;

    private int dy = -5;  // Percentage of height moved per second (negative= upwards)
    private float drawY = 0;

    public GameMap(int id, PowerUp[] powerups, Obstacle[] obstacles){
        this.id = id;
        this.powerups = powerups;
        this.obstacles = obstacles;


        int screenWidth = DataHandler.getInstance().screenWidth;
        int screenHeight = DataHandler.getInstance().screenHeight;

        float scale = screenWidth / ResourceLoader.getInstance().getImage(id).getWidth();

        transformationMatrix = new Matrix();
        transformationMatrix.postTranslate(screenWidth, 0.0f);
        transformationMatrix.preScale(scale, scale);

    }

    protected GameMap() {}


    public void update(long dt){
        float delta = dt/1000;
        drawY += dy*delta;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setFilterBitmap(false);
        canvas.drawBitmap(ResourceLoader.getInstance().getImage(id), transformationMatrix, paint);
    }


}