package com.pa_gruppe11.freefalling.Models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Collidable;
import com.pa_gruppe11.freefalling.Drawable;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

import java.util.ArrayList;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Character extends Collidable{

    // The maximum distance, in percentage, the touch can be from centre of model
    //      before it gets automatically set to that max value
    // The closer the actual touch is to the centre, the less impact this gives to the player.
    // For now, we say this radius is 10% of the height, not the width
    private final float pMaxTouch = 10.0f;

    private float maxTouchRadius;
    private float maxDx = 25; // max velocity    - not necessarily final (powerup?)
    private float maxDy = 25; // 5 percent of screen changed per second

    private float maxAccelerationX = 10; // max acceleration
    private float maxAccelerationY = 10; // would take 5 seconds to fully change 180degrees with 10 acc, and 25 vel.

    private int dt;

    private ArrayList<ArrayList<Float>> touches;

    private Collidable collidesWith;



/*
    public Character(int dt, ImageView image){
        super();
        this.dt = dt;
    }
*/

    public Character(int id){

        super(ResourceLoader.getInstance().getImage(id).getWidth(), ResourceLoader.getInstance().getImage(id).getHeight());
        this.id = id;

        transformationMatrix = new Matrix();
        transformationMatrix.setTranslate(0, 0);

        maxTouchRadius = DataHandler.getInstance().screenHeight*(100/pMaxTouch);

    }

    public void update(long dt){
        respondToTouch(); // we do this here, to assure the order is done correctly
        super.update(dt);




    }

    public void respondToTouch() {
        float centreX = x + width/2;
        float centreY = y + height/2;

        if(touches != null && touches.size() > 0) {
            // TODO: handle only 1 touch to begin with, find out if loop through list would be wurt
            int i = 0;  // replace this with loop if necessary or wanted
            float touchedX = touches.get(i).get(0);
            float touchedY = touches.get(i).get(1);

            float distX = Math.abs(centreX - touchedX);
            float distY = Math.abs(centreY - touchedY);

            float distance = (float) Math.sqrt(Math.pow(centreX-touchedX,2) + Math.pow(centreY - touchedY,2));

            if(distance > maxTouchRadius) { // Correction for touches outside the allowed max radius
                float scale = (maxTouchRadius/Math.max(distX, distY));
                distX*=scale;
                distY*=scale;
            }
            // TODO: This may not be correct, but need to run with valid implementationg to check first
            float accelerationX = maxAccelerationX * distX/maxTouchRadius;
            float accelerationY = maxAccelerationY * distY/maxTouchRadius;



        }

        touches = null; // Remove when consumed ( synchronize?)
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(ResourceLoader.getInstance().getImageList().get(id), getX(), getY(), new Paint());
    }

    public void setCollidesWith(Collidable c) {
        collidesWith = c;
    }

    public void setTouches(ArrayList<ArrayList<Float>> touches) {
        this.touches = touches;
    }
}
