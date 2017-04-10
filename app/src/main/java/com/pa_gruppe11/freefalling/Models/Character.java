package com.pa_gruppe11.freefalling.Models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Collidable;
import com.pa_gruppe11.freefalling.Drawable;
import com.pa_gruppe11.freefalling.Singletons.CollisionHandler;
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


    private float accModifier = 40;

    private ArrayList<ArrayList<Float>> touches;

    private Collidable collidesWith;

    private String displayName = "default";

    public Character(int id, int width, int height){

        super(id, width, height);


        transformationMatrix = new Matrix();
        transformationMatrix.setTranslate(0, 0);

        maxTouchRadius = DataHandler.getInstance().getScreenHeight()*(pMaxTouch/100);
        Log.w("Character", "maxTouchRadius: " + maxTouchRadius);

    }

    @Override
    public void update(long dt){
        respondToTouch(); // we do this here, to assure the order is done correctly
        super.update(dt);
/*
        if ( isBottomCollision() && collidesWith.isPinned()){


           // Log.w("Character", "Her skal det kolliderast!");

            setDy(collidesWith.getDy());




           // Log.w("Character", "Collideswith according to character is: " + collidesWith.toString());
           // Log.w("Character", "Character collides with something! This happens in Character.update()");

        }
*/


        /*
        setDx((dx + accelerationX * (float)dt/1000));
        setDy((dy + accelerationY * (float)dt/1000));

       // Log.w("CharacterPRE", "dt in seconds: " + accelerationY * (float)dt/1000);

        setX(x + dx * (float)dt/1000);
        setY(y + dy * (float)dt/1000);
        */
     //   Log.w("CharacterPOST", "X: " + x  +   "    Y: " + y);



    }

    public void respondToTouch() {
        float centreX = x + width/2;
        float centreY = y + height/2;

        if(touches != null && touches.size() > 0) {
            // TODO: handle only 1 touch to begin with, find out if loop through list would be wurt
            int i = 0;  // replace this with loop if necessary or wanted
            float touchedX = touches.get(i).get(0);
            float touchedY = touches.get(i).get(1);

            float distX = touchedX - centreX;
            float distY = touchedY - centreY;

           // Log.w("Character", "distX: " + distX + "  distY: " + distY);

            float distance = (float) Math.sqrt(Math.pow(centreX-touchedX,2) + Math.pow(centreY - touchedY,2));

           // Log.w("Character", "distance: " + distance);

            if(distance > maxTouchRadius) { // Correction for touches outside the allowed max radius
                //float scale = (maxTouchRadius/Math.max(distX, distY));
                float scale = Math.abs(distX) > Math.abs(distY) ? Math.abs(maxTouchRadius/distX) : Math.abs(maxTouchRadius/distY);

                distX*=scale;
                distY*=scale;
             //   Log.w("Character", "scale: " + scale);
            }
            // TODO: This may not be correct, but need to run with valid implementationg to check first
            accelerationX = accModifier * maxAccelerationX * distX/maxTouchRadius;
            accelerationY = accModifier * maxAccelerationY * distY/maxTouchRadius;

           // Log.w("Character", "accelerationX: " + accelerationX);
           // Log.w("Character", "accelerationY: " + accelerationY);

        }

         touches = null; // Remove when consumed ( synchronize?)
    }

    @Override
    public void draw(Canvas canvas) {
//        canvas.drawBitmap(ResourceLoader.getInstance().getImageList().get(id), getX(), getY(), new Paint());
        super.draw(canvas);

        // Draw displayName atop of charactermodel
        Paint paint = new Paint();
        paint.setTextSize(32);
        paint.setColor(Color.RED);
        canvas.drawText(displayName, x-30 + (width/2), y-30, paint);
    }

    @Override
    public void setCollidesWith(Collidable collidesWith) {
        this.collidesWith = collidesWith;
    }

    @Override
    public String toString(){
        return "Character";
    }

    public void setTouches(ArrayList<ArrayList<Float>> touches) {
        this.touches = touches;
    }

    public void setValues(GameMessage gameMessage) {
        x = gameMessage.getX();
        y = gameMessage.getY();
        dx = gameMessage.getDx();
        dy = gameMessage.getDy();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

}
