package com.pa_gruppe11.freefalling.implementations.models;

import android.util.Log;

import com.pa_gruppe11.freefalling.Models.Obstacle;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;

/**
 * Created by skars on 06.04.2017.
 */

public class Block extends Obstacle{


    /**
     * Basic obstacle that obstructs the player's path
     * @param id resourceId
     * @param width percentage of mapWidth
     * @param height percentage of mapHeight
     */
    public Block(int id, int width, int height) {
        super(id, width, height);
        Log.w("Block", "Constructed a new Block");
        setPinned(true);
        setLethal(false);

  //      setWidth(106);
//        setHeight(61);

        dx = 0;
        dy = 0;
        pDx = 5;
        pDy = 5;
        pAx = 0;
        pAy = 0;
        vector.x = 0;
        vector.y = 0;

        setMaxDx(screenWidth * pDx/100);
        setMaxDy(screenHeight * pDy/100);

        accelerationX = screenWidth * pAx/100;
        accelerationY = screenHeight * pAy/100;


    }




    @Override
    public void update(long dt){
        super.update(dt);
        // TODO: Possibly implement some Block specifics here.
        if (isPinned()){

        }

    }


}
