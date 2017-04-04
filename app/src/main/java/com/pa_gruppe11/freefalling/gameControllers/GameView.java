package com.pa_gruppe11.freefalling.gameControllers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pa_gruppe11.freefalling.Models.Player;

/**
 * Created by Martin Kostveit on 31.03.2017.
 *
 */

public class GameView extends SurfaceView {
    private Drawable[] drawList;
    Canvas c;

    public GameView(Context context, Drawable[] drawableList) {
        super(context);
        drawList=drawableList;
    }

    @Override
    public void draw(Canvas c) {
        super.draw(c);
        for(int i=0; i<drawList.length; i++) {
            drawList[i].draw(c);
        }
    }

    public void drawPostMatchView(Player winner) {
        Rect background = new Rect(0, 0, getWidth(), getHeight());
        Paint myPaint = new Paint();
        myPaint.setColor(Color.BLACK);
        myPaint.setAlpha(10);
        myPaint.setStyle(Paint.Style.FILL);
        c.drawRect(background, myPaint);
    }
}
