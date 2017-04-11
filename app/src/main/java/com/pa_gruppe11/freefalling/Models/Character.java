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
    // For now, we say this radius is 10% of the screen height, not the width
    private final float pMaxTouch = 25.0f;

    private float maxTouchRadius;


    private float accModifier = 10;

    private ArrayList<ArrayList<Float>> touches;

    private Collidable collidesWith;

    private String displayName = "default";

    public Character(int id, int width, int height){

        super(id, width, height);


        transformationMatrix = new Matrix();
        transformationMatrix.setTranslate(0, 0);

        // maxTouchRadius set to 10% of the screenheight.
        maxTouchRadius = DataHandler.getInstance().getScreenHeight()*(pMaxTouch/100);
        Log.w("Character", "maxTouchRadius: " + maxTouchRadius);

    }

    @Override
    public void update(long dt){
        respondToTouch(); // we do this here, to assure the order is done correctly
        super.update(dt);
    }

    // Correct centreX and centreY? - check
    // Correct touchedX and tocuehdY? - check
    // Correct vector between character and touchedX and touchedY - check
    // Correct vector magnitude (distance)? - check
    // Good scaling?

/*
    public void respondToTouch() {
        float centreX = x + width/2;        // centre of the character
        float centreY = y + height/2;

        Log.w("Character", "centreX : " + centreX + "   centreY : " + centreY);

        if(touches != null && touches.size() > 0) {
            // TODO: handle only 1 touch to begin with, find out if loop through list would be wurt
            int i = 0;  // replace this with loop if necessary or wanted
            float touchedX = touches.get(i).get(0); // x
            float touchedY = touches.get(i).get(1); // y

            Log.w("Character", "TouchedX : " + touchedX + "  touchedY : " + touchedY);

            float distX = touchedX - centreX;
            float distY = touchedY - centreY;

            Log.w("Character", "distX: " + distX + "  distY: " + distY);

            float distance = (float) Math.sqrt(Math.pow(centreX-touchedX,2) + Math.pow(centreY - touchedY,2));

            Log.w("Character", "distance: " + distance);

            if(distance > maxTouchRadius) { // Correction for touches outside the allowed max radius

               // float scale = (maxTouchRadius/Math.max(distX, distY));
                float scale = Math.abs(distX) > Math.abs(distY) ? Math.abs(maxTouchRadius/distX) : Math.abs(maxTouchRadius/distY);

                //distX*=scale;
                //distY*=scale;

                   Log.w("Character", "scale: " + scale);
            }
            // TODO: This may not be correct, but need to run with valid implementation to check first

            accelerationX = accModifier * maxAccelerationX * distX/maxTouchRadius;
            accelerationY = accModifier * maxAccelerationY * distY/maxTouchRadius;






           // Log.w("Character", "accelerationX: " + accelerationX);
           // Log.w("Character", "accelerationY: " + accelerationY);

        }

         touches = null; // Remove when consumed ( synchronize?)
    }

    */

    public void respondToTouch(){

        float centreX = x + width/2;
        float centreY = y + height/2;

        if(touches != null && touches.size() > 0){

            int i = 0;  // replace this with loop if necessary or wanted
            float touchedX = touches.get(i).get(0); // x
            float touchedY = touches.get(i).get(1); // y

            vector.x = touchedX - centreX;
            vector.y = touchedY - centreY;

            vector.setMagnitude(vector.calculateMagnitude(vector.x, vector.y));

            // Setting the unit vector. This is the general direction the character should be heading after a touch.
            unitVector.x = vector.x / vector.magnitude;
            unitVector.y = vector.y / vector.magnitude;

            unitVector.setMagnitude(unitVector.calculateMagnitude(unitVector.x, unitVector.y));


            Log.w("Character", "Magnitude of unit vector: " + unitVector.getMagnitude());

            /*

            // TODO: Possibly change this
            if (vector.getMagnitude() > maxTouchRadius){

                float scale = Math.abs(vector.x) > Math.abs(vector.y) ? Math.abs(maxTouchRadius/vector.x) : Math.abs(maxTouchRadius/vector.y);

                vector.x *= scale;
                vector.y *= scale;

                dx = vector.x;
                dy = vector.y;

            }

            */

            dx = unitVector.x;
            dy = unitVector.y;

            unitVector.setMagnitude(unitVector.calculateMagnitude(unitVector.x, unitVector.y));


            accelerationX = dx * (screenHeight * pAx / 100);
            accelerationY = dy * (screenHeight * pAy / 100);

        }

        touches = null;
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
