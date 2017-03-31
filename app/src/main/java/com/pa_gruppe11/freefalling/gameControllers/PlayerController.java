package com.pa_gruppe11.freefalling.gameControllers;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by Kristian on 31/03/2017.
 */
public class PlayerController extends SurfaceView implements SurfaceHolder.Callback {

    private ArrayList<ArrayList<Float>> touchs;

    public PlayerController(Context context) {
        super(context);
        touchs = new ArrayList<ArrayList<Float>>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.w("PlayerController", "OOOH, you touched my talala");
        touchs = new ArrayList<ArrayList<Float>>();
        Point p = new Point();
        for(int i = 0; i < e.getPointerCount(); i++) { // multi touch
            float x = e.getX(i);  // PRECISION !!!!!
            float y = e.getY(i);
            ArrayList<Float> tmp = new ArrayList<Float>();
            tmp.add(x);tmp.add(y);
            touchs.add(tmp);
        }
        return true;
    }




    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.w("Task3View", "Surface created; starting thread");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public ArrayList<ArrayList<Float>> getTouchs() {
        return touchs;
    }

}
