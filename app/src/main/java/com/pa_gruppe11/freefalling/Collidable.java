package com.pa_gruppe11.freefalling;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
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

    protected float x = 0.0f;
    protected float y = 0.0f;
    protected float dx = 0.0f;
    protected float dy = 0.0f;
    private float maxDx = 200.0f; // max velocity    - not necessarily final (powerup?)
    private float maxDy = 200.0f; // TODO: MAKE THIS 5% OF THE SCREEN HEIGHT

    protected int height;
    protected int width;
    protected float accelerationX = 0.0f;
    protected float accelerationY = 0.0f;
    protected float maxAccelerationX = 30; // max acceleration
    protected float maxAccelerationY = 30; // would take 5 seconds to fully change 180degrees with 10 acc, and 25 vel.

    protected long dt = 0;

    private boolean pinned = false; // Set to true if the collidable obeject does not move after handleCollision.
    private boolean collision = false;
    private int rightBounds = DataHandler.getInstance().screenWidth;
    private int leftBounds = 0;

    private RectF boundingBox;
    private Collidable collider;

    private boolean topCollision = false;
    private boolean bottomCollision = false;
    private boolean leftCollision = false;
    private boolean rightCollision = false;



    public Collidable(int height, int width){
        this.height = height;
        this.width = width;

    }

/*
    public boolean collides(Collidable collider){
        //some condition
        return getBounds().intersect(collider.getBounds());
    }
  */
    /*
    public boolean collides(Collidable collider){
        if (boundingBox.intersect(collider.getBoundingBox())) {
            setCollider(collider);
            return collision = true;
        }
        else {
            Log.w("Collidable", "Kjem du hit??");
            return collision = false;
        }
    }
    */

    public RectF getNextRect(float x, float y){
        RectF rect = new RectF(x, y, x + width, y + height);
        return rect;
    }

    public float calculateNextX(float dx, long dt){
        return x + dx*dt;
    }


    public float calculateNextY(float dy, long dt){
        return y + dy*dt;
    }


    public void update(long dt){

        setDt(dt);

        // Controllable objects
        if (!pinned) {
            Log.w("Collidable", "The Player is not pinned!");
            if (!collision){
                // SETTING THE SPEED
                setDx((dx + accelerationX * (float) dt / 1000));
                setDy((dy + accelerationY * (float) dt / 1000));

                // SETTING THE POSITION
                setX(x + dx * (float) dt / 1000);
                setY(y + dy * (float) dt / 1000);


               // Log.w("Collidable", "dx : " + dx + "       dy: " + dy);

                Log.w("Collidable", "Player does not collide with anything!");

            }else{

                Log.w("Collidable", "Player collides with something!");


//                handleCollision(collider);

            }
        }
            // Update of object that can only move i one axis, primarily Obstales.
        else{

            // Collision detection


                // Movement

         //       setX(x + dx * (float) dt / 100);
         //       setY(y + dy * (float) dt / 100);



            // Log.w("Collidable", "x: "  + x + "      y: " + y);

        }

    }


    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(ResourceLoader.getInstance().getImageList().get(id), x, y, new Paint());
    }

    // SETTERS

    public void setX(float x){

        if (x + width < rightBounds && x > leftBounds && !collision)
            this.x = x;
        else
            Log.w("Collidable", "The object cannot go out of bounds in x-direction");
    }

    public void setY(float y)
    {
        if (!collision)
            this.y = y;
        else
            Log.w("Collidable", "The object collides with something in y-direction.");
    }

    public void setDx(float dx){


        if (Math.abs(dx) <= maxDx){     // The absolute value of dx is lower than the allowed max speed.
            this.dx = dx;
        }else if (dx < 0){              // The speed given is negative.
            this.dx = -maxDx;
        }

    }

    public void setDy(float dy){

        if (Math.abs(dy) <= maxDy){     // The absolute value of dx is lower than the allowed max speed.
            this.dy = dy;
        }else if (dy < 0){              // The speed given is negative.
            this.dy = -maxDy;
        }
    }

    public void setId(int id) {this.id = id;}

    public void setPinned(boolean pinned){this.pinned = pinned;}

    public void setCollider(Collidable collider){this.collider = collider;}

    public void setCollision(boolean collision){this.collision = collision;}

    public void setDt(long dt){this.dt = dt;}

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

    public long getDt(){return dt;}

    public boolean isPinned(){return pinned;}

    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }

    public RectF getBoundingBox(){return new RectF(x, y, x + width, y + height);}

    public void setBoundingBox(RectF boundingBox){this.boundingBox = boundingBox;}

}
