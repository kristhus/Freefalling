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

    public boolean detectCollision(Collidable collider1, Collidable collider2){

        float nextX1 = collider1.calculateNextX(collider1.getDx(), collider1.getDt());
        float nextY1 = collider1.calculateNextY(collider1.getDy(), collider1.getDt());


        float nextX2 = collider2.calculateNextX(collider2.getDx(), collider2.getDt());
        float nextY2 = collider2.calculateNextY(collider2.getDy(), collider2.getDt());


        RectF nextRect1 = collider1.getNextRect(nextX1, nextY1);
        RectF nextRect2 = collider2.getNextRect(nextX2, nextY2);


        if (nextRect1.intersect(nextRect2)){
            collider1.setCollision(true);
            collider2.setCollision(true);

          //  Log.w("CollisionHandler", "Collision detected!");

            return true;
        }

        collider1.setCollision(false);
        collider2.setCollision(false);
        return false;
    }

    public void handleCollision(Collidable collider1, Collidable collider2){


       // Log.w("CollisionHandler", "Handles some collision");

        // Case when player collides with an obstacle
       /* if (!collider1.isPinned() && collider2.isPinned() && detectCollision(collider1, collider2)){
            collider1.setDx(collider2.getDx());
            collider1.setDy(collider2.getDy());
        }else if (!collider1.isPinned() && collider2.isPinned() && detectCollision(collider1, collider2)){
            collider2.setDx(collider1.getDx());
            collider2.setDy(collider1.getDy());
        }
*/


        //collider1.setY(100);

    }


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

    public static CollisionHandler getInstance(){

        return INSTANCE;
    }


}
