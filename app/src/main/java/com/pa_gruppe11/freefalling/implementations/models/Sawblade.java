package com.pa_gruppe11.freefalling.implementations.models;

import com.pa_gruppe11.freefalling.Models.Obstacle;
import com.pa_gruppe11.freefalling.R;

/**
 * Created by Kristian on 14/04/2017.
 */

public class Sawblade extends Obstacle{

    public Sawblade(int width, int height) {
        super(R.drawable.sawblade, width, height);
        setPinned(false);
        setRotate(true);
        setLethal(true);
        setAngularVelocity(3.0f*(float)Math.PI);    // 2 rotations per second
    }

}
