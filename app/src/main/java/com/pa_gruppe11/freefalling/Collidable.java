package com.pa_gruppe11.freefalling;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Collidable implements Drawable {

    protected int id;
    protected Matrix transformationMatrix;

    protected int x;
    protected int y;
    protected float dx;
    protected float dy;
    protected int height;
    protected int width;


    public Collidable(int height, int width){
        this.height = height;
        this.width = width;
    }


    public boolean collides(Collidable collider){
        //some condition

        return getBounds().intersect(collider.getBounds());
    }

    public Rect getNextRect(){
        Rect rect = new Rect(x, y, x + width, y + height);
        return rect;
    }

    public void update(long dt){

       // Movement

        int rightBounds = DataHandler.getInstance().screenWidth;
        int leftBounds = 0;
        int topBounds = 0;
        int bottomBounds = DataHandler.getInstance().screenHeight;

        // Checking out of bounds before movement
        if (x + dx*dt > rightBounds || x + dx*dt < leftBounds){

            x += dx*dt;

        }else if (y + dy*dt > bottomBounds || y + dy*dt < topBounds){

            y += dy*dt;

        }




    }


    public void Drawable(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(ResourceLoader.getInstance().getImageList().get(id), x, y, new Paint());
    }

    // SETTERS

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setDx(int dx){
        this.dx = dx;
    }

    public void setDy(int dy){
        this.dy = dy;
    }

    public void setId(int id) {this.id = id;}

    public void setTransformationMatrix(Matrix matrix){
        this.transformationMatrix = matrix;
    }

    // GETTERS

    public int getX() {
        return x;
    }

    public int getY() {
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

    public Rect getBounds(){return new Rect(x, y, x + width, y + height);}


}
