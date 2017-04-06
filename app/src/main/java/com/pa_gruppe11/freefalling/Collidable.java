package com.pa_gruppe11.freefalling;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Collidable implements Drawable {

    protected int id;
    protected Matrix transformationMatrix;

    protected float x = 00.0f;
    protected float y = 00.0f;
    protected float dx = 0.0f;
    protected float dy = 0.0f;
    private float maxDx = 60.0f; // max velocity    - not necessarily final (powerup?)
    private float maxDy = 60.0f; // 5 percent of screen changed per second

    protected int height;
    protected int width;
    protected float accelerationX = 0.0f;
    protected float accelerationY = 0.0f;
    protected float maxAccelerationX = 30; // max acceleration
    protected float maxAccelerationY = 30; // would take 5 seconds to fully change 180degrees with 10 acc, and 25 vel.


    private boolean pinned; // Set to true if the collidable obeject does not move after collision.
    private int rightBounds = DataHandler.getInstance().screenWidth;
    private int leftBounds = 0;
    private int topBounds = 0;


    public Collidable(int height, int width){
        this.height = height;
        this.width = width;
    }


    public boolean collides(Collidable collider){
        //some condition

        return getBounds().intersect(collider.getBounds());
    }

    public RectF getNextRect(){
        RectF rect = new RectF(x, y, x + width, y + height);
        return rect;
    }

    public void update(long dt){

        setDx((dx + accelerationX * (float)dt/1000));
        setDy((dy + accelerationY * (float)dt/1000));

        // Log.w("CharacterPRE", "dt in seconds: " + accelerationY * (float)dt/1000);

        setX(x + dx * (float)dt/1000);
        setY(y + dy * (float)dt/1000);

        // Out of bounds
        // If OOB Invert dx


        // Acceleration

            // v = m/s,  a = m/s^2

        setDx((dx + accelerationX * (float)dt/1000));
        setDy((dy + accelerationY * (float)dt/1000));


        Log.w("Collidable", "dx : " + dx + "       dy: " + dy);


        // Conditions

        // Velocity
        // Conditions

        // Position


        setX(x + dx * (float)dt/1000);
        setY(y + dy * (float)dt/1000);

        // Conditions






    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(ResourceLoader.getInstance().getImageList().get(id), x, y, new Paint());
    }

    // SETTERS

    public void setX(float x){
        if (x + width < rightBounds && x > leftBounds)
            this.x = x;
        else
            Log.w("Collidable", "The object cannot go out of bounds in x-direction");
    }

    public void setY(float y)
    {
            this.y = y;
    }

    public void setDx(float dx){


        if (Math.abs(dx) <= maxDx){
            this.dx = dx;
        }else if (dx < 0){
            this.dx = -maxDx;
        }

    }

    public void setDy(float dy){

        if (Math.abs(dy) <= maxDy){
            this.dy = dy;
        }else if (dy < 0){
            this.dy = -maxDy;
        }
    }

    public void setId(int id) {this.id = id;}

    public void setTransformationMatrix(Matrix matrix){
        this.transformationMatrix = matrix;
    }

    // GETTERS

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }

    public RectF getBounds(){return new RectF(x, y, x + width, y + height);}


}
