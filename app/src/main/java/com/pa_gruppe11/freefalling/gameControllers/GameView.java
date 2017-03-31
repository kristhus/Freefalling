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

public class GameView extends SurfaceView implements Runnable {
    private SurfaceHolder holder;
    private Drawable[] drawList;
    private Boolean isRunning;
    Canvas c;

    public GameView(Context context, Drawable[] drawableList) {
        super(context);
        drawList=drawableList;
        holder = getHolder();
        resume();
    }

    @Override
    public void run() {
        while(isRunning) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            c = holder.lockCanvas();
            for(int i=0; i<drawList.length; i++) {
                drawList[i].draw(c);
            }
            holder.unlockCanvasAndPost(c);
        }
    }
    public void pause() {
        isRunning=false;
    }
    public void resume() {
        isRunning=true;
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
