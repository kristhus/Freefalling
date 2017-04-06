package com.pa_gruppe11.freefalling.Singletons;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by skars on 06.04.2017.
 */

public final class Collisionhandler {

    private static final Collisionhandler INSTANCE =  new Collisionhandler();

    private RectF rect1;
    private RectF rect2;


    public ArrayList<PointF> getEdges(RectF rect){
        ArrayList<PointF> edges = new ArrayList<PointF>();
        edges.add(0, new PointF(rect.left, rect.top));
        edges.add(1, new PointF(rect.right, rect.top));
        edges.add(2, new PointF(rect.left, rect.bottom));
        edges.add(3, new PointF(rect.right, rect.bottom));

        return edges;
    }

    public boolean collision(RectF rect1, RectF rect2){
        ArrayList edgesRect1 = getEdges(rect1);
        ArrayList edgesRect2 = getEdges(rect2);

        return false;
    }
}
