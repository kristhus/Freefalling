package com.pa_gruppe11.freefalling.implementations.models;

import android.util.Log;

import com.pa_gruppe11.freefalling.Models.Character;
import com.pa_gruppe11.freefalling.R;

/**
 * Created by Kristian on 08/04/2017.
 */

public class Gruber extends Character {

    protected final int identifier = R.drawable.gruber;

    public Gruber(int id, int width, int height) {
        super(id, width, height);
        setX(420);
        setY(0);


        //   setDx(10);
        //setMaxDx(250);
        //setMaxDy(250);

        vector.x = 0;
        vector.y = 0;

        dx = 0;
        dy = 0;

        pDx = 20;
        pDy = 10;
        pAx = 70;
        pAy = 40;

        setMaxDx(screenHeight * pDx/100);
        setMaxDy(screenHeight * pDy/100);

        accelerationX = 0;
        accelerationY = 0;


        //   setDx(10);
        //   setDy(10);
        //  setId(id);
        Log.w("Gruber", "Constructed a new Gruber");
        Log.w("Gruber", "Width: " + width + "     height: " + height);

    }


    @Override
    public void update(long dt){
        super.update(dt);

        // TODO: Possibly implement some Hanz specifics here
    }

    public int getIdentifier() {
        return identifier;
    }


}