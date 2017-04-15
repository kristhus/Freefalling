package com.pa_gruppe11.freefalling.Singletons;

import android.graphics.Color;

import com.pa_gruppe11.freefalling.Collidable;

/**
 * Created by skars on 15.04.2017.
 */

public final class AnimationHandler {

    private static final AnimationHandler INSTANCE =  new AnimationHandler();

    private static final int moveRightAnimation = 0x0001;

    private static final int moveLeft = 0x0002;

    private static final int rotateClockwise = 0x0003;

    private static final int rotateCounterClockwise = 0x0004;



    public AnimationHandler getInstance(){return INSTANCE;}

    public void moveRightAnimation(Collidable collidable){






    }


    public void animate(){




    }

}
