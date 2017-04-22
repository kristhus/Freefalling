package com.pa_gruppe11.freefalling.Singletons;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.pa_gruppe11.freefalling.Collidable;

import java.util.ArrayList;

/**
 * Created by skars on 06.04.2017.
 */

public final class CollisionHandler {


    private static final CollisionHandler INSTANCE =  new CollisionHandler();

    Collidable collider1;
    Collidable collider2;


    public void setCollider1(Collidable collider1) {
        this.collider1 = collider1;
    }

    public void setCollider2(Collidable collider2){
        this.collider2 = collider2;
    }

    public Collidable getCollider1() {
        return collider1;
    }

    public Collidable getCollider2(){
        return collider2;
    }

    public static CollisionHandler getInstance(){

        return INSTANCE;
    }


}
