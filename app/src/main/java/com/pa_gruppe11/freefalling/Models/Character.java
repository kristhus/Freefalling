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
import com.pa_gruppe11.freefalling.framework.VectorSAT;

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


    private float accModifier = 0;

    private ArrayList<ArrayList<Float>> touches;

    private Collidable collidesWith;

    private String displayName = "default";

    private VectorSAT previousPosition;

    private boolean thisCharacter = false;  // If this is the current client's user

    private float centreX;

    private float centreY;

    private float touchedX = 0.0f;

    private float touchedY = 0.0f;


    private VectorSAT oldVector;
    private VectorSAT oldUnitVector;

    public Character(int id, int width, int height){

        super(id, width, height);

        previousPosition = new VectorSAT(0,0);

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





    public void respondToTouch(){

        oldVector = getVector();
        oldUnitVector = getUnitVector();

        centreX = x + width/2;
        centreY = drawY + height/2;


        if(touches != null && touches.size() > 0){

            int i = 0;  // replace this with loop if necessary or wanted
            touchedX = touches.get(i).get(0); // x
            touchedY = touches.get(i).get(1); // y

            vector = new VectorSAT(touchedX - centreX, touchedY - centreY);

            Log.w("Character", "dx: " + vector.x + "    dy: " + vector.y);

           // vector = new VectorSAT(centreX - touchedX, touchedY - centreY);

            vector.setMagnitude(vector.calculateMagnitude(vector.x, vector.y));

            // Setting the unit vector. This is the general direction the character should be heading after a touch.
            unitVector = new VectorSAT(vector.x / vector.magnitude, vector.y / vector.magnitude);

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


            unitVector.setMagnitude(unitVector.calculateMagnitude(unitVector.x, unitVector.y));

            Log.w("Character", "vector.x: " + vector.x + "  oldVector.x: " + oldVector.x);
            Log.w("Character", "vector.x: " + vector.y + "  oldVector.x: " + oldVector.y);



            if (Math.abs(vector.x) > Math.abs(oldVector.x)){

                Log.w("Character", "Blir det her kjørt?");
                accelerationX = unitVector.x * (screenHeight * pAx / 100);
            }else{
                accelerationX = 0; // Should be negative??
            }

            if (Math.abs(vector.y) > Math.abs(oldVector.y)){
                accelerationY = unitVector.y * (screenHeight * pAy / 100);
            }else{
                accelerationY = 0; // Should be negative??
            }





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
        canvas.drawText(displayName, x-30 + (width/2), drawY-30, paint);

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

    public VectorSAT getPreviousPosition() {
        return previousPosition;
    }
    public void setPreviousPosition(VectorSAT previousPosition) {
        this.previousPosition = previousPosition;
    }

    public boolean isThisCharacter() {
        return thisCharacter;
    }

    public void setThisCharacter(boolean thisCharacter) {
        this.thisCharacter = thisCharacter;
    }

}
