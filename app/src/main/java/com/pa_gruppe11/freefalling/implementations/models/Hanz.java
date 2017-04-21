package com.pa_gruppe11.freefalling.implementations.models;

import android.util.Log;

import com.pa_gruppe11.freefalling.Models.Character;
import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;

/**
 * Created by Thomas on 03.04.2017.
 */

public class Hanz extends Character {


    public Hanz(int id, int width, int height) {
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
        pAx = 50;
        pAy = 50;

        setMaxDx(2*screenHeight * pDx/100);
        setMaxDy(2*screenHeight * pDy/100);

        accelerationX = 0;
        accelerationY = 0;


     //   setDx(10);
     //   setDy(10);
      //  setId(id);
        Log.w("Hanz", "Constructed a new Hanz");
        Log.w("Hanz", "Width: " + width + "     height: " + height);

    }


    @Override
    public void update(long dt){
        super.update(dt);

        // TODO: Possibly implement some Hanz specifics here
    }

}
