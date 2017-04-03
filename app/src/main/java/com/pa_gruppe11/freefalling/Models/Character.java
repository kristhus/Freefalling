package com.pa_gruppe11.freefalling.Models;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Collidable;
import com.pa_gruppe11.freefalling.Drawable;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by kjetilvaagen on 31/03/17.
 */

public class Character extends Collidable{



    private int dt;



/*
    public Character(int dt, ImageView image){
        super();
        this.dt = dt;
    }
*/

    public Character(int id){

        super(ResourceLoader.getInstance().getImage(id).getWidth(), ResourceLoader.getInstance().getImage(id).getHeight());
        this.id = id;

        transformationMatrix = new Matrix();
        transformationMatrix.setTranslate(0, 0);

    }

    public void update(long dt){



    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(ResourceLoader.getInstance().getImageList().get(id), getX(), getY(), new Paint());
    }
}
