package com.pa_gruppe11.freefalling.gameControllers;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Martin Kostveit on 31.03.2017.
 *
 */

public class PostMatchView extends SurfaceView {
    SurfaceHolder holder;
    public PostMatchView(Context context) {
        super(context);
        holder=getHolder();
    }

    /*
    public draw(Canvas c) {
        //super();
        c = holder.lockCanvas();
        //c.drawBitmap(image, 10, 10, null);
        holder.unlockCanvasAndPost(c);
    }
    */
}
