package com.pa_gruppe11.freefalling.Models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.widget.Toast;

import com.pa_gruppe11.freefalling.Collidable;
import com.pa_gruppe11.freefalling.Drawable;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

import java.util.ArrayList;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class GameMap implements Drawable {

    protected int id;
    protected Bitmap image;

    protected float scaledHeight;
    protected float scaledWidth;

    protected Matrix transformationMatrix;
    protected ArrayList<PowerUp> powerups;
    protected ArrayList<Obstacle> obstacles;

    protected float x = 0.0f;      // Model x, to indicate where the x is on the entire stage (500 / 15700 f.ex.)
    protected float y = 0.0f;      //
    protected float endY = 15000.0f; // Semi arbitrary number. when y == endY, init finish-line

    private float dy = -0.15f;  // Percentage of height moved per second (negative= upwards)
    private float pdy;
    private float drawY = 0.0f;    // Keeps track of where in the stage first image is drawn
    private int dx = 0; // Support for translating x, but default off. Implementations must set dx.
    private float drawX = 0;

    private float scale;
    private float numberOfTimesToDraw;

    private boolean tmp = true;

    // Minimap settings
    protected int minimapLineColor = 0x80000000;
    protected int minimapIndicator = 0x80CC0000;

    protected float minimapStartX = 0.95f;  // 95% of screenwidth
    protected float minimapStartY = 0.05f;
    protected float minimapEndX = minimapStartX;
    protected float minimapEndY = 0.20f;    // 20 % of height
    protected float lineThickness = 0.01f;

    protected ArrayList<Float> respawnPoints;


    private boolean initFinale = false;
    private String done = "";

    private Character thisCharacter;

    public GameMap(int id, ArrayList<PowerUp> powerups, ArrayList<Obstacle> obstacles, Character thisCharacter){
        this(id);
        this.id = id;
        this.powerups = powerups;
        this.obstacles = obstacles;
        this.thisCharacter = thisCharacter;
        // TODO: Do shit with arrays

    }

    public GameMap(int id) {
        this.id = id;
        float screenWidth = DataHandler.getInstance().getScreenWidth();
        //float screenWidth = 1077;
        float screenHeight = DataHandler.getInstance().getScreenHeight();

        powerups = new ArrayList<PowerUp>();
        obstacles = new ArrayList<Obstacle>();

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
        drawY += (thisCharacter.getPreviousPosition().y - thisCharacter.getY());
        drawX += dx*delta;

        if(y < endY) {
            //y += Math.abs(pdy*delta); //TODO: Swap this with a modification of character y-progress
            y = thisCharacter.getY();
        }else {
            //TODO: init finishline
            initFinale = true;
        }

        if(initFinale) {
            //DONEZO
            done = "FERDIG NO";
        }



        if(drawY <= -scaledHeight) {    //if entire first image is off-screen, reset position (causes flicker???)
            drawY += scaledHeight;
            transformationMatrix.setTranslate(0, drawY);
            transformationMatrix.postScale(scale, scale);
        } /*else if(drawY <0) {
            drawY += scaledHeight;
            transformationMatrix.setTranslate(0, drawY);
            transformationMatrix.postScale(scale, scale);
        }*/
        else {                         // Condition fulfilled most of the time
            transformationMatrix.postTranslate(dx * delta, thisCharacter.getPreviousPosition().y - thisCharacter.getY());
        }





        if(powerups != null) {
            for(PowerUp p : powerups) {
                p.update(dt);
            }
        }
        if(obstacles != null) {
            for(Obstacle o : obstacles) {
                if(!outsideScreen(o))
                    o.update(dt);
            }
        }

    }

    /**
     * Remember, draw order is important
     * @param canvas The canvas to be painted on
     */
    public void draw(Canvas canvas) {
        Paint paint = new Paint();      // Do not set antialiasflag, as the smoothing makes lines appear between bitmaps
        paint.setFilterBitmap(false);



        Matrix tmpMatrix = new Matrix(transformationMatrix);
            // if()     //if image is within canvasbounds, draw it
        // if(drawY + (scaledHeight*i) < DataHandler.getInstance().getScreenHeight())
        int i = 0;
        while(drawY + (scaledHeight*i) < DataHandler.getInstance().getScreenHeight()) {
            canvas.drawBitmap(ResourceLoader.getInstance().getImage(id), tmpMatrix, paint);
            tmpMatrix.postTranslate(0, scaledHeight);
            i++;
        }

        if(obstacles != null) {
            for (Obstacle o : obstacles) {
                if(!outsideScreen(o))
                    o.draw(canvas);
            }
        }
        if(powerups != null) {
            for (PowerUp p : powerups) {
                p.draw(canvas);
            }
        }

        // Draw stage_progress
        if(!DataHandler.getInstance().isHideMinimap()) {
            // map-line
            float screenWidth = DataHandler.getInstance().getScreenWidth();
            float screenHeight = DataHandler.getInstance().getScreenHeight();
            paint.setColor(minimapLineColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(lineThickness * screenWidth);
            canvas.drawLine(minimapStartX * screenWidth,    // startX
                    minimapStartY * screenHeight,           // startY
                    minimapEndX * screenWidth,              // endX
                    minimapEndY * screenHeight,             // endY
                    paint);

            // progress indicator
            paint.setColor(minimapIndicator);
            //paint.setStrokeWidth(lineThickness/2);
            canvas.drawLine(minimapStartX * screenWidth - (0.02f*screenWidth),  // startX
                    minimapStartY*screenHeight + (y/endY)*(minimapEndY * screenHeight - minimapStartY*screenHeight),          // startY
                    minimapEndX * screenWidth + (0.02f*screenWidth),            // endX
                    minimapStartY*screenHeight + (y/endY)*(minimapEndY * screenHeight - minimapStartY*screenHeight),          // endY
                    paint
                    );
            if(done != "") {
                canvas.drawText(done, screenWidth/2, screenHeight/2, new Paint());
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



    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public ArrayList<PowerUp> getPowerups() {
        return powerups;
    }

    public void addPowerup(PowerUp powerUp){
        powerups.add(powerUp);
    }

    public void addObstacle(Obstacle obstacle){
        obstacles.add(obstacle);
    }

    public void setThisCharacter(Character c) {
        thisCharacter = c;
    }


    public float getY() {
        return y;
    }

    public ArrayList<Float> getRespawnPoints() {
        return respawnPoints;
    }

    public float getClosestRespawnPoint(float posY) {
        float closest = 0;
        for(float respawnY : respawnPoints) {
            float distance = posY - respawnY;
            if(distance >= 0 && distance > closest) {   // closest point current y has passed
                closest = respawnY;
            }else
                return closest;
        }

        return 0.0f;
    }

    public static boolean outsideScreen(Collidable c) {
        return c.getDrawY()+c.getHeight() < 0 - DataHandler.getInstance().getScreenHeight()*0.2 ||
                c.getDrawY() > DataHandler.getInstance().getScreenHeight() + DataHandler.getInstance().getScreenHeight()*0.2; // 20% margin
    }

}