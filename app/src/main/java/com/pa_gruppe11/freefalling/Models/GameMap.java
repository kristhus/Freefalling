package com.pa_gruppe11.freefalling.Models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

import com.pa_gruppe11.freefalling.Drawable;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class GameMap implements Drawable {

    protected int id;
    protected Bitmap image;

    protected float scaledHeight;
    protected float scaledWidth;

    protected Matrix transformationMatrix;
    protected PowerUp[] powerups;
    protected Obstacle[] obstacles;

    private float dy = -0.15f;  // Percentage of height moved per second (negative= upwards)
    private float pdy;
    private float drawY = 0.0f;    // Keeps track of where in the stage first image is drawn
    private int dx = 0; // Support for translating x, but default off. Implementations must set dx.
    private float drawX = 0;

    private float scale;
    private float numberOfTimesToDraw;

    private boolean tmp = true;

    public GameMap(int id, PowerUp[] powerups, Obstacle[] obstacles){
        this(id);
        this.id = id;
        this.powerups = powerups;
        this.obstacles = obstacles;
        // Do shit with arrays then call GameMap(id)

    }

    public GameMap(int id) {
        this.id = id;
        float screenWidth = DataHandler.getInstance().screenWidth;
        //float screenWidth = 1077;
        float screenHeight = DataHandler.getInstance().screenHeight;

        Log.w("GameMap", "screenWidth: " + screenWidth);

        pdy = dy * screenHeight;

        scale = screenWidth / ResourceLoader.getInstance().getImage(id).getWidth();

        scaledHeight = ResourceLoader.getInstance().getImage(id).getHeight()*scale; // Correctly scaled
        scaledWidth = ResourceLoader.getInstance().getImage(id).getWidth()*scale;


        Log.w("GameMap", "imagewidth: " + ResourceLoader.getInstance().getImage(id).getWidth());
        Log.w("GameMap", "scale: " + scale);

        numberOfTimesToDraw = screenHeight/scaledHeight + 2;

        transformationMatrix = new Matrix();
        transformationMatrix.setTranslate(0, 0);
        transformationMatrix.postScale(scale, scale);
    }

    /**
     * Implementations of GameMap can use this constructor to create its own
     * constructor, if there are some parameters that should be included, but does not match GameMap
     */
    protected GameMap() {}


    public void update(long dt){
        float delta = (float)dt/1000;  // If fps is really choppy, try using another method.
        drawY += pdy*delta;
        drawX += dx*delta;

        if(drawY <= -scaledHeight) {    //if entire first image is off-screen, reset position (causes flicker???)
            drawY += scaledHeight;
            transformationMatrix.setTranslate(0, drawY);
            transformationMatrix.postScale(scale, scale);
        }else {                         // Condition fulfilled most of the time
            transformationMatrix.postTranslate(dx * delta, pdy * delta);
        }

        if(powerups != null) {
            for(PowerUp p : powerups) {
                p.update(dt);
            }
        }
        if(obstacles != null) {
            for(Obstacle o : obstacles) {
                o.update(dt);
            }
        }

    }

    /**
     * Remember, draw order is important
     * @param canvas The canvas to be painted on
     */
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setFilterBitmap(false);

        Matrix tmpMatrix = new Matrix(transformationMatrix);
        for(int i = 1; i <= numberOfTimesToDraw; i++) { // Very costly
            canvas.drawBitmap(ResourceLoader.getInstance().getImage(id), tmpMatrix, paint);
            tmpMatrix.postTranslate(0, scaledHeight); // Black stripes some times, drawY's fault
        }

        if(obstacles != null) {
            for (Obstacle o : obstacles) {
                o.draw(canvas);
            }
        }
        if(powerups != null) {
            for (PowerUp p : powerups) {
                p.draw(canvas);
            }
        }

    }

    /**
     * Use this to change the simulated fall speed
     * @param dy
     */
    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }


    public Obstacle[] getObstacles() {
        return obstacles;
    }

    public PowerUp[] getPowerups() {
        return powerups;
    }

   // public void addObstacle(Obstacle obstacle){for (Obstacle : obstacles)}

}