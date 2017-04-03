package com.pa_gruppe11.freefalling;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Collidable implements Drawable {

    protected int id;
    protected Matrix transformationMatrix;

    private int x;
    private int y;
    private float dx;
    private float dy;
    private int height;
    private int width;


    public Collidable(int height, int width){
        this.height = height;
        this.width = width;
    }


    public boolean collides(){
        //some condition
        return true;
    }

    public Rect getNextRect(){
        Rect rect = new Rect(x, y, x + width, y + height);
        return rect;
    }

    public void update(long dt){
        x += dx;
        y += dy;
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
}
