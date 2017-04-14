package com.pa_gruppe11.freefalling;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;
import com.pa_gruppe11.freefalling.framework.RectSAT;
import com.pa_gruppe11.freefalling.framework.VectorSAT;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Collidable implements Drawable {

    protected int id;
    protected Matrix transformationMatrix;

    protected float x = 0.0f;
    protected float y = 0.0f;
    protected float nextX = 0.0f;
    protected float nextY = 0.0f;

    protected float drawX = 0.0f; // unused, as vertical falling
    protected float drawY = 0.0f;


    protected float dx = 0.0f;
    protected float dy = 0.0f;
    private float maxDx = 100.0f; // max velocity    - not necessarily final (powerup?)
    private float maxDy = 100.0f; // TODO: MAKE THIS 5% OF THE SCREEN HEIGHT

    private float angularVelocity; // rads per second, pi/2 seems legit
    private float angle;
    private boolean rotate;

    protected int height;
    protected int width;
    protected float accelerationX = 0.0f;
    protected float accelerationY = 0.0f;
    protected float maxAccelerationX = 30; // max acceleration
    protected float maxAccelerationY = 30; // would take 5 seconds to fully change 180degrees with 10 acc, and 25 vel.

    private float scale;
    private float scaledHeight;
    private float scaledWidth;

    private float importedWidth;
    private float importedtHeigt;

    private boolean pinned = false; // Set to true if the collidable obeject does not move after handleCollision.
    private boolean collided = false;
    private boolean collisionNext = false;

    private int rightBounds = DataHandler.getInstance().getScreenWidth();
    private int leftBounds = 0;

    private RectF boundingBox;
    private RectF nextRect;
    private Collidable collidesWith;


    private boolean topCollision = false;
    private boolean bottomCollision = false;
    private boolean leftCollision = false;
    private boolean rightCollision = false;

    private Bitmap bitmap;


    private String debugString = "";

    public Collidable(int id, int width, int height){

        this.id = id;

        this.width = width;
        this.height = height;


        boundingBox = new RectF(x, y, x + width, y + height);

        bitmap = ResourceLoader.getInstance().getImageList().get(id);

        Log.w("Collidable", "Width: " + width + "   height: " + height);

        bitmap = ResourceLoader.getInstance().getResizedBitmap(bitmap, width, height);

        transformationMatrix = new Matrix();
    }

    /**
     * Creates a new RectF according to the x and y given.
     * @param x
     * @param y
     * @return
     */
    public RectF calculateNextRect(float x, float y){
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

        // SETTING THE SPEED

        setDx((dx + accelerationX * (float) dt / 1000));
        setDy((dy + accelerationY * (float) dt / 1000));

        // SETTING THE POSITION
        setX(x + dx * (float) dt / 1000);
        setY(y + dy * (float) dt / 1000);


        // SETTING THE ANGULAR VELOCITY
        if(true) {
            if(angle > 2*Math.PI)   // keep angle in the interval [-2PI, 2PI]
                angle -= 2*Math.PI;
            else if(angle < -2*Math.PI)
                angle += 2*Math.PI;
            angle += angularVelocity * (float) dt / 1000;
            //DELETE THIS AFTER TESTING (edge-rotation)
            setRotationPoints(getCorners(this, dt));
        }



    }


    public RectSAT getScreenBounds() {
        RectSAT bounds = new RectSAT();
        bounds.topLeft = new PointF(0,0);
        bounds.topRight = new PointF(DataHandler.getInstance().getScreenWidth(), 0);
        bounds.bottomLeft = new PointF(0, DataHandler.getInstance().getScreenHeight());
        bounds.bottomRight = new PointF(DataHandler.getInstance().getScreenWidth()
                , DataHandler.getInstance().getScreenHeight());
        return bounds;
    }

    public RectSAT getBounds() {
        RectSAT bounds = new RectSAT();
        bounds.topLeft = new PointF(x, y);
        bounds.topRight = new PointF(x + width, y);
        bounds.bottomLeft = new PointF(x, y + height);
        bounds.bottomRight = new PointF(x + width, y + height);

        bounds.left = x;
        bounds.top = y;
        bounds.right = x + width;
        bounds.bottom = y + height;

        return bounds;
    }

    public static boolean collides(RectSAT b1, RectSAT b2) {
        return (b1.left < b2.right && b1.right > b2.left) &&
                (b1.top < b2.bottom && b1.bottom > b2.top);
    }

    /**
     * Minimum translation vector
     * Updates positions and notifies of a collision
     */
    public static HashMap<String, Object> collidesMTV(RectSAT b1, RectSAT b2) {
        boolean collides = collides(b1, b2);
        HashMap retVal = new HashMap<>();
        retVal.put("boolean", collides);
        VectorSAT mtv = new VectorSAT(0,0);
        if(collides) {
            ArrayList<VectorSAT> mtvList = new ArrayList<>();
            mtvList.add(new VectorSAT(b1.left - (b2.right),0));
            mtvList.add(new VectorSAT(b1.right-b2.left, 0));
            mtvList.add(new VectorSAT(0, b1.top - b2.bottom));
            mtvList.add(new VectorSAT(0, b1.bottom - b2.top));

            Collections.sort(mtvList);
            mtv = mtvList.get(0);
        }
        retVal.put("VectorSAT", mtv);
        return retVal;
    }

    /**
     * Get the centre position of the collidable object
     * @param c
     * @return
     */
    public static PointF getCentre(Collidable c) {
        return new PointF(c.getX() + c.getWidth()/2, c.getY() + c.getHeight()/2);
    }

    /**
     * Get corners of a collidable object to be rotated. Needs frame time, to calculate
     * angular velocity for this frame
     * @param c
     * @param dt
     * @return
     */
    public static ArrayList<VectorSAT> getCorners(Collidable c, long dt) { // dt needed to get angular velocity for this frame
        ArrayList<VectorSAT> corners = new ArrayList<>();
        PointF centre = getCentre(c);

        float rot = c.getAngle();   // Angular velocity for current frame

        ArrayList<VectorSAT> world_cords = new ArrayList<>();
        world_cords.add(new VectorSAT(c.getX(), c.getY()));                                 // top left
        world_cords.add(new VectorSAT(c.getX() + c.getWidth(), c.getY()));                  // top right
        world_cords.add(new VectorSAT(c.getX(), c.getY() + c.getHeight()));                 // bottom left
        world_cords.add(new VectorSAT(c.getX() + c.getWidth(), c.getY() + c.getHeight()));  // bottom right


        for(VectorSAT v : world_cords) {
            float x = (float) (centre.x + (v.x - centre.x) * Math.cos(rot) - (v.y - centre.y) * Math.sin(rot));
            float y = (float) (centre.y + (v.x - centre.x) * Math.sin(rot) + (v.y - centre.y) * Math.cos(rot));
            corners.add(new VectorSAT(x,y));
        }
        return corners;
    }

    public static ArrayList<VectorSAT> getAxis(ArrayList<VectorSAT> c1, ArrayList<VectorSAT> c2) {
        ArrayList<VectorSAT> axis = new ArrayList<>();
        axis.add(VectorSAT.getUnitVector(new VectorSAT(c1.get(1).x -  c1.get(0).x, c1.get(1).y - c1.get(0).y))); // topright - top left
        axis.add(VectorSAT.getUnitVector(new VectorSAT(c1.get(2).x -  c1.get(0).x, c1.get(2).y - c1.get(0).y))); // bottom right - top right
        axis.add(VectorSAT.getUnitVector(new VectorSAT(c2.get(1).x -  c2.get(0).x, c2.get(1).y - c2.get(0).y)));    // ok?
        axis.add(VectorSAT.getUnitVector(new VectorSAT(c2.get(2).x -  c2.get(0).x, c2.get(2).y - c2.get(0).y)));
        return axis;
    }

    public static float dotProduct(VectorSAT v1, VectorSAT v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }

    public static VectorSAT SATcollide(Collidable collidable1, Collidable collidable2, long dt) {
        ArrayList<VectorSAT> c1 = getCorners(collidable1, dt);  // The rotated corners of collidable1
        ArrayList<VectorSAT> c2 = getCorners(collidable2, dt);
        ArrayList<VectorSAT> axis = getAxis(c1, c2);

        VectorSAT mtv = new VectorSAT(999999.0f, 999999.0f ); //TODO: lul

        // For hver akse (4 akser), finn for hver shape, nærmeste og borteste punkt fra aksen
        for(VectorSAT v1 : axis) {  // For hver akse
            ArrayList<Float> scalarsc1v = new ArrayList<>();
            ArrayList<Float> scalarsc2v = new ArrayList<>();
            for(int i = 0; i < c1.size(); i++) {    // for hvert punkt i hjørnet (c1length == c2length)
                scalarsc1v.add(dotProduct(c1.get(i), v1));
                scalarsc2v.add(dotProduct(c2.get(i), v1));
            }

            Collections.sort(scalarsc1v);   // sort largest to shortest
            Collections.sort(scalarsc2v);

            float s1min = scalarsc1v.get(0);
            float s1max = scalarsc1v.get(scalarsc1v.size()-1);

            float s2min = scalarsc2v.get(0);
            float s2max = scalarsc2v.get(scalarsc2v.size()-1);

            if (s2min > s1max || s2max < s1min) {
                return null;
            }

            float overlap = (s1max > s2max) ? -(s2max - s1min) : (s1max-s2min);
            if( Math.abs(overlap) < mtv.getLength()) {
                mtv = new VectorSAT(v1.x * overlap, v1.y * overlap);
            }

        }
        return mtv;
    }


        /*
        temp, delete
         */
    private ArrayList<VectorSAT> rotationPoints;
    private ArrayList<VectorSAT> getRotationPoints() {return rotationPoints;}
    private void setRotationPoints(ArrayList<VectorSAT> r){rotationPoints=r;}



    /**
     * Draws the collidable object onto a canvas. The BitMap is loaded through the Singleton
     * Resourceloader.
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {

        Paint paint = new Paint();


        if(rotate) {
            Matrix rotationMatrix = new Matrix();
            rotationMatrix.setTranslate(x, drawY);
            rotationMatrix.postRotate((float)Math.toDegrees(angle), getCentre(this).x, drawY+ height/2);
            canvas.drawBitmap(bitmap, rotationMatrix, paint);

        } else {
            canvas.drawBitmap(bitmap, x, drawY, paint);
        }

      // Paints the corners of a rotating object
        if(rotationPoints != null) {
            paint.setColor(Color.GREEN);
            for(VectorSAT v : rotationPoints) {
                canvas.drawRect(v.x, v.y, v.x+10, v.y+10, paint);
            }
        }


        if(!debugString.equals("")) {
            paint.setTextSize(64);
            paint.setColor(Color.BLACK);
            canvas.drawText(debugString, 50, 100, paint);
        }

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

        if ((x + width) < rightBounds && x > leftBounds ) {
            this.x = x;
        }
        else {
           // Log.w("Collidable", "Width when OOB collided: " + width);
           // Log.w("Collidable", "The object cannot go out of bounds in x-direction");
        }
    }

    /**
     * Sets the y. Checks collided conditions first.
     * @param y
     */
    public void setY(float y)
    {
        if (true) {      // true is placeholder for something in the future
            this.y = y;
        }
        //else
           // Log.w("Collidable", "The object collides with something in y-direction.");
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


        //Log.w("Collidable", "dx = " + this.dx);

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

//        Log.w("Collidable", "dy = " + this.dy);
     }

    public void setWidth(int width){this.width = width;}

    public void setHeight(int height){this.height = height;}

    public void setId(int id) {this.id = id;}

    public void setPinned(boolean pinned){this.pinned = pinned;}

    public void setCollided(boolean collided){this.collided = collided;}

    public void setCollisionNext(boolean collisionNext) {
        this.collisionNext = collisionNext;
    }

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

    public float getX() {return x;}

    public float getY() {return y;}

    public float getNextX() {return nextX;}

    public float getNextY() {return nextY;}

    public RectF getNextRect(){return nextRect;}

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

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

    public boolean isCollisionNext() {return collisionNext;}

    public void setDebugString(String debugString) {
        this.debugString = debugString;
    }

    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }
    public float getAngularVelocity() {
        return angularVelocity;
    }
    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }
    public float getAngle() {
        return angle;
    }
    public float getDrawY() {
        return drawY;
    }
    public void setDrawY(float drawY) {
        this.drawY = drawY;
    }

}
