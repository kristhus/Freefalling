package com.pa_gruppe11.freefalling.Models;

import android.media.Image;
import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Collidable;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class PowerUp extends Collidable{

    private int type;
    ImageView image;

    public PowerUp(int id, ImageView image, int width, int height) {
        super(id, width, height);
    }

    public void update(long dt){

    }


}
