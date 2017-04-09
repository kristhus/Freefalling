package com.pa_gruppe11.freefalling;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
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

    private float scale;
    private float scaledHeight;
    private float scaledWidth;

    private float importedWidth;
    private float importedtHeigt;

    private boolean pinned = false; // Set to true if the collidable obeject does not move after handleCollision.
    private boolean collided = false;
    private int rightBounds = DataHandler.getInstance().getScreenWidth();
    private int leftBounds = 0;

    private RectF boundingBox;
    private Collidable collider;
    private Collidable collidesWith;


    private boolean topCollision = false;
    private boolean bottomCollision = false;
    private boolean leftCollision = false;
    private boolean rightCollision = false;

    private Bitmap bitmap;


    public Collidable(int id, int width, int height){

//        float screenWidth = DataHandler.getInstance().screenWidth;
  ///      float screenHeight = DataHandler.getInstance().screenHeight;

        this.id = id;

        this.width = width;
        this.height = height;

        bitmap = ResourceLoader.getInstance().getImageList().get(id);

        Log.w("Collidable", "Width: " + width + "   height: " + height);

        bitmap = ResourceLoader.getInstance().getResizedBitmap(bitmap, width, height);

        //scale = width / ResourceLoader.getInstance().getImage(id).getWidth();
      //  scaledHeight = ResourceLoader.getInstance().getImage(id).getHeight()*scale;
    //    scaledWidth = ResourceLoader.getInstance().getImage(id).getWidth()*scale;

        transformationMatrix = new Matrix();
//        transformationMatrix.setTranslate(0.0f, 0.0f);
  //      transformationMatrix.postScale(scale, scale);
    }

    /**
     * Creates a new RectF according to the x and y given.
     * @param x
     * @param y
     * @return
     */
    public RectF getNextRect(float x, float y){
        RectF rect = new RectF(x, y, x + width, y + height);
        return rect;
    }

    /**
     * Calculate the next x position by multiplying the speed with the time and adding it with the
     * current x position.
     * @param dx
     * @param dt
     * @return
     */
    public float calculateNextX(float dx, long dt){
        return x + dx * (float) dt / 1000;
    }

    /**
     * Calculate the next y position by multiplying the speed with the time and adding it with the
     * current y position.
     * @param dy
     * @param dt
     * @return
     */
    public float calculateNextY(float dy, long dt){
        return y + dy * (float) dt / 1000;
    }

    /**
     * Updates everything that has to do with the Collidable object.
     * @param dt
     */
    public void update(long dt){

        setDt(dt);

        // Controllable objects

        /*if (!pinned) {

            // SETTING THE SPEED
            setDx((dx + accelerationX * (float) dt / 1000));
            setDy((dy + accelerationY * (float) dt / 1000));

            // SETTING THE POSITION
            setX(x + dx * (float) dt / 1000);
            setY(y + dy * (float) dt / 1000);

        }

        else{ // Update of object that can only move i one axis, primarily Obstales.

            setX(x + dx * (float) dt / 1000);
            setY(y + dy * (float) dt / 1000);


        }
        */

        // SETTING THE SPEED
        setDx((dx + accelerationX * (float) dt / 1000));
        setDy((dy + accelerationY * (float) dt / 1000));

        // SETTING THE POSITION
        setX(x + dx * (float) dt / 1000);
        setY(y + dy * (float) dt / 1000);


    }

    /**
     * Draws the collidable object onto a canvas. The BitMap is loaded through the Singleton
     * Resourceloader.
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {

        canvas.drawBitmap(bitmap, x, y, new Paint());

        //canvas.drawBitmap(ResourceLoader.getInstance().getImageList().get(id), x, y, new Paint());

        //Paint paint = new Paint();
        //canvas.drawBitmap(ResourceLoader.getInstance().getImage(id), transformationMatrix, paint);

    }

    // SETTERS


    public String toString(){
        return "";
    }

    /**
     * Sets the x. Checks out of bounds and collided conditions first.
     * @param x
     */
    public void setX(float x){

        // || isPinned() because objects like obstacles should move anyway

        if ((x + width) < rightBounds && x > leftBounds && !collided || isPinned()) {
            this.x = x;
        }
        else {
            Log.w("Collidable", "Width when OOB collided: " + width);
            Log.w("Collidable", "The object cannot go out of bounds in x-direction");
        }
    }

    /**
     * Sets the y. Checks collided conditions first.
     * @param y
     */
    public void setY(float y)
    {
        if (!collided || isPinned())       // || isPinned() because objects like obstacles should move anyway
            this.y = y;
        else
            Log.w("Collidable", "The object collides with something in y-direction.");
    }

    /**
     * Sets the dx (speed in x-direction). Checks if the speed if the speed is higher than the
     * allowed max speed.
     * @param dx
     */
    public void setDx(float dx){


        if (Math.abs(dx) <= maxDx){     // The absolute value of dx is lower than the allowed max speed.
            this.dx = dx;
        }else if (dx < 0){              // The speed given is negative.
            this.dx = -maxDx;
        }


        Log.w("Collidable", "dx = " + this.dx);

    }

    /**
     *
     * Sets the dy (speed in y-direction). Checks if the speed if the speed is higher than the
     * allowed max speed.
     * @param dy
     */
    public void setDy(float dy){

        if (Math.abs(dy) <= maxDy){     // The absolute value of dx is lower than the allowed max speed.
            this.dy = dy;
        }else if (dy < 0){              // The speed given is negative.
            this.dy = -maxDy;
        }

        Log.w("Collidable", "dy = " + this.dy);
    }

    public void setWidth(int width){this.width = width;}

    public void setHeight(int height){this.height = height;}

    public void setId(int id) {this.id = id;}

    public void setPinned(boolean pinned){this.pinned = pinned;}

    public void setCollider(Collidable collider){this.collider = collider;}

    public void setCollided(boolean collided){this.collided = collided;}

    public void setDt(long dt){this.dt = dt;}

    public void setCollidesWith(Collidable collidesWith){this.collidesWith = collidesWith;}

    public void setTransformationMatrix(Matrix matrix){
        this.transformationMatrix = matrix;
    }

    public void setTopCollision(boolean topCollision) {
        this.topCollision = topCollision;
    }

    public void setBottomCollision(boolean bottomCollision) {
        this.bottomCollision = bottomCollision;
    }

    public void setLeftCollision(boolean leftCollision) {
        this.leftCollision = leftCollision;
    }

    public void setRightCollision(boolean rightCollision) {
        this.rightCollision = rightCollision;
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

    public int getWidth(){return width;}

    public int getHeight(){return height;}

    public boolean isPinned(){return pinned;}

    public Matrix getTransformationMatrix() {
        return transformationMatrix;
    }

    public RectF getBoundingBox(){return boundingBox;}

    public boolean isTopCollision() {
        return topCollision;
    }

    public boolean isBottomCollision() {
        return bottomCollision;
    }

    public boolean isLeftCollision() {
        return leftCollision;
    }

    public boolean isRightCollision() {
        return rightCollision;
    }

    public boolean isCollided(){return collided;}
}
