package com.pa_gruppe11.freefalling.gameControllers;

import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.pa_gruppe11.freefalling.Models.Character;



public class Player {
    ImageView currentView;
    float x, y, currentX, currentY, dy;
    int width, height, velX = 20, velY=20;
    Rect rect = new Rect(0, 0, 0, 0);
    private Character character;

    public Player(ImageView imageView) {
        currentView = imageView;
        x=currentView.getX();
        y=currentView.getY();
        currentView.setScaleX(-1);
    }

    Rect getRect() {
        currentView.getHitRect(rect);
        return rect;
    }

    boolean isOutOfBoundsX() {
        currentX=currentView.getX();
        if(currentX<50 || currentX>620) {
            return true;
        }
        return false;
    }
    boolean isOutOfBoundsY() {
        currentY=currentView.getY();
        if(currentY<100 || currentY>1300) {
            return true;
        }
        return false;
    }

  //  @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                x = event.getX();
                y = event.getY();
                dy = y - currentView.getY();
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                if(x>currentView.getX()-200 && x<currentView.getX()+200) {
                    currentView.setY(event.getY() - dy);
                }
                else {
                    break;
                }
            }
            return true;
        }
        return true;
    }


    public void setCharacter(Character character) {
        this.character = character;
    }
    public Character getCharacter() {
        return character;
    }

}
