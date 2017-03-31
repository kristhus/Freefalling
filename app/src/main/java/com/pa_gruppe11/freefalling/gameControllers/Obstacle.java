package com.pa_gruppe11.freefalling.gameControllers;

import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pa_gruppe11.freefalling.R;

public class Obstacle {
    RelativeLayout layout;
    float x, y, currentX, currentY, dy;
    int width, height, velX = 20, velY=20;
    Rect rect = new Rect(0, 0, 0, 0);

    public Obstacle(RelativeLayout lv) {
        layout = lv;
    }

    Rect getRect() {
        layout.getHitRect(rect);
        return rect;
    }
}
