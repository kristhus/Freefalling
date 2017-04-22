package com.pa_gruppe11.freefalling.implementations.models;

import android.util.Log;

import com.pa_gruppe11.freefalling.Models.Character;
import com.pa_gruppe11.freefalling.R;

/**
 * Created by Kristian on 21/04/2017.
 * Ridiculous movement speed, but larger bounds
 * Screams like an idiot upon death
 */

public class Goat extends Character {

    protected final int identifier = R.drawable.hanz;


    public Goat(int id, int width, int height) {
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

        pDx = 10;
        pDy = 10;
        pAx = 100;  // Ridiculous acceleration
        pAy = 100;

        setMaxDx(6*screenHeight * pDx/100);
        setMaxDy(6*screenHeight * pDy/100);

        accelerationX = 0;
        accelerationY = 0;


        //   setDx(10);
        //   setDy(10);
        //  setId(id);
        Log.w("Goat", "Constructed a new Goat");
        Log.w("Goat", "Width: " + width + "     height: " + height);

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

