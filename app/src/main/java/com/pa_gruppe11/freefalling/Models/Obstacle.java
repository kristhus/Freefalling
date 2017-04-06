package com.pa_gruppe11.freefalling.Models;

import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Collidable;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Obstacle extends Collidable{


    public Obstacle(int type, ImageView image, int height, int width){
        super(height, width);
  //      this.type = type;
//        this.image = image;
    }

    public Obstacle(int id){
        super(ResourceLoader.getInstance().getImage(id).getHeight(), ResourceLoader.getInstance().getImage(id).getWidth());
        this.id = id;
    }

    @Override
    public void update(long dt){
        super.update(dt);
    }

}
