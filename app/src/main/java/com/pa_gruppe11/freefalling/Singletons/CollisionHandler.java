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

        float nextX1 = collider1.getNextX();
        float nextY1 = collider1.getNextY();


        float nextX2 = collider2.getNextX();
        float nextY2 = collider2.getNextY();


        RectF nextRect1 = collider1.getNextRect(nextX1, nextY1);
        RectF nextRect2 = collider2.getNextRect(nextX2, nextY2);


        if (nextRect1.intersect(nextRect2)){
            collider1.setCollided(true);
            collider2.setCollided(true);

            if (nextRect1.bottom > nextRect2.top){
                collider1.setBottomCollision(true);
                collider2.setTopCollision(true);

                Log.w("Character", collider1.toString() + " bottom: " + collider1.getBoundingBox().bottom + collider2 + " top: " +
               collider2.getBoundingBox().top);

                if (nextRect1.left > nextRect2.right){
                    collider1.setLeftCollision(true);
                    collider2.setRightCollision(true);
                }else if (nextRect1.right < nextRect2.left){
                    collider1.setRightCollision(true);
                    collider2.setLeftCollision(true);
                }

            } else {

                Log.w("CollisionHandler", "Blir det her kjørt??");

                collider1.setTopCollision(true);
                collider2.setBottomCollision(true);

                if (nextRect1.left > nextRect2.right){
                    collider1.setLeftCollision(true);
                    collider2.setRightCollision(true);
                }else if (nextRect1.left < nextRect2.right){
                    collider1.setRightCollision(true);
                    collider2.setLeftCollision(true);
                }

            }

          /*  if (nextRect1.top < nextRect2.bottom){
                collider1.setTopCollision(true);
                collider2.setBottomCollision(true);

                //   Log.w("Character", collider1.toString() + " bottom: " + collider1.getBoundingBox().bottom + collider2 + " top: " +
                //   collider2.getBoundingBox().top);

                if (nextRect1.left > nextRect2.right){
                    collider1.setLeftCollision(true);
                    collider2.setRightCollision(true);
                }else if (nextRect1.left < nextRect2.right){
                    collider1.setRightCollision(true);
                    collider2.setLeftCollision(true);
                }

            }
            */

            return true;
        }

        // SETS COLLIDED
        collider1.setCollided(false);
        collider2.setCollided(false);

        // SETS TOP/BOTTOM/LEFT/RIGHT COLLISION
        collider1.setTopCollision(false);
        collider1.setBottomCollision(false);
        collider1.setLeftCollision(false);
        collider1.setRightCollision(false);

        collider2.setTopCollision(false);
        collider2.setBottomCollision(false);
        collider2.setLeftCollision(false);
        collider2.setRightCollision(false);



        return false;
    }

    public void handleCollision(Collidable collider1, Collidable collider2){


      if (collider1.isBottomCollision() && collider2.isPinned() ){

          Log.w("CollisionHandler", collider1.toString() + ": Bottom-collision");

          collider1.setDy(collider2.getDy());


          Log.w("CollisionHandler", collider1.toString() + " dy: " + collider1.getDy() + "      " + collider2.toString()
          + " dy: " + collider2.getDy());

          if (collider1.isLeftCollision() || collider1.isRightCollision()){
              collider1.setDx(collider2.getDx());


              Log.w("CollisionHandler", collider1.toString() + " dx: " + collider1.getDx() + "      " + collider2.toString()
                      + " dx: " + collider2.getDx());

          }
      }

      if (collider1.isTopCollision() && collider2.isPinned()){


          Log.w("CollisionHandler", collider1.toString() + ": Top-collision");

          collider1.setDy(collider2.getDy());

          Log.w("CollisionHandler", collider1.toString() + " dy: " + collider1.getDy() + "      " + collider2.toString()
                  + " dy: " + collider2.getDy());

          if (collider1.isLeftCollision() || collider1.isRightCollision()){
              collider1.setDx(collider2.getDx());


              Log.w("CollisionHandler", collider1.toString() + " dx: " + collider1.getDx() + "      " + collider2.toString()
                      + " dx: " + collider2.getDx());

          }

      }



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
