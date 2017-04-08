package com.pa_gruppe11.freefalling.implementations.models;

import android.util.Log;

import com.pa_gruppe11.freefalling.Models.Obstacle;

/**
 * Created by skars on 06.04.2017.
 */

public class Block extends Obstacle{

    public Block(int id, int width, int height) {
        super(id, width, height);
        Log.w("Block", "Constructed a new Block");
        setPinned(true);
  //      setWidth(106);
//        setHeight(61);
        setX(500);
        setY(1200);
        setDx(0);
        setDy(-20);
    }

    @Override
    public void update(long dt){
        super.update(dt);

        // TODO: Possibly implement some Block specifics here.
        if (isPinned()){

        }

    }


}
