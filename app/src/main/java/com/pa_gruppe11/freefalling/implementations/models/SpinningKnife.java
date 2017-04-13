package com.pa_gruppe11.freefalling.implementations.models;

import com.pa_gruppe11.freefalling.Models.Obstacle;

/**
 * Created by Kristian on 13/04/2017.
 */

public class SpinningKnife extends Obstacle {

    public SpinningKnife(int id, int width, int height) {
        super(id, width, height);
        setPinned(true);
        setLethal(true);
    }
}
