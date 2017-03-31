package com.pa_gruppe11.freefalling.gameControllers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Martin Kostveit on 31.03.2017.
 *
 */

public class GameView extends SurfaceView implements Runnable {
    private SurfaceHolder holder;
    private Drawable[] drawList;
    private Boolean isRunning;

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
            Canvas c = holder.lockCanvas();
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
}
