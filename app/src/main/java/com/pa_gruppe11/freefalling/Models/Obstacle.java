package com.pa_gruppe11.freefalling.Models;

import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Collidable;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Obstacle extends Collidable{


    private boolean lethal = false;

    public Obstacle(int id, int width, int height){
        super(id, width, height);
    }

    @Override
    public void update(long dt){
        super.update(dt);
    }




    @Override
    public String toString(){
        return "Obstacle";
    }

    public void setLethal(boolean lethal) {
        this.lethal = lethal;
    }
    public boolean isLethal() {
        return lethal;
    }

}
