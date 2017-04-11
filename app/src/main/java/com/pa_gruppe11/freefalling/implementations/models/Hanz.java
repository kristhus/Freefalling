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
