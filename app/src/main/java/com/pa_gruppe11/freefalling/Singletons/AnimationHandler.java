package com.pa_gruppe11.freefalling.Singletons;


import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.pa_gruppe11.freefalling.Collidable;

/**
 * Created by skars on 15.04.2017.
 */

public final class AnimationHandler {

    private static final AnimationHandler INSTANCE =  new AnimationHandler();

    public static final int noAnimation = 0x0000;

    public static final int moveRightAnimation = 0x0001;

    public static final int moveLeftAnimation = 0x0002;

    public static final int rotateClockwise = 0x0003;

    public static final int rotateCounterClockwise = 0x0004;

    private Matrix animationMatrix;


    public static AnimationHandler getInstance(){return INSTANCE;}

    public void moveRightAnimation(Collidable collidable, Canvas canvas){

       /* Matrix rotationMatrix = new Matrix();
        rotationMatrix.setTranslate(x, drawY);
        rotationMatrix.postRotate((float)Math.toDegrees(angle), getCentre(this).x, drawY+ height/2);
        canvas.drawBitmap(bitmap, rotationMatrix, paint);
    */

       float moveRightAngle = 20.0f;


        animationMatrix = collidable.getTransformationMatrix();
        animationMatrix.setTranslate(collidable.getX(), collidable.getDrawY());
        //animationMatrix.postRotate((float)Math.toDegrees(moveRightAngle), collidable.getCentre(collidable).x, collidable.getDrawY() + collidable.getHeight()/ 2);
        animationMatrix.postRotate(moveRightAngle, collidable.getCentre(collidable).x, collidable.getDrawY() + collidable.getHeight()/ 2);
        collidable.setTransformationMatrix(animationMatrix);
        canvas.drawBitmap(collidable.getBitmap(), collidable.getTransformationMatrix(), new Paint());



    }

    public void moveLeftAnimation(Collidable collidable, Canvas canvas){

        float moveLeftAngle = 290.0f;

        animationMatrix = collidable.getTransformationMatrix();
        animationMatrix.setTranslate(collidable.getX(), collidable.getDrawY());
        //animationMatrix.postRotate((float)Math.toDegrees(moveLeftAngle), collidable.getCentre(collidable).x, collidable.getDrawY() + collidable.getHeight()/2);
        animationMatrix.postRotate(moveLeftAngle, collidable.getCentre(collidable).x, collidable.getDrawY() + collidable.getHeight()/2);
        collidable.setTransformationMatrix(animationMatrix);
        canvas.drawBitmap(collidable.getBitmap(), collidable.getTransformationMatrix(), new Paint());



    }


    public void animate(){




    }

}
