package com.pa_gruppe11.freefalling.implementations.models;

import com.pa_gruppe11.freefalling.Models.Character;
import com.pa_gruppe11.freefalling.R;

/**
 * Created by Kristian on 08/04/2017.
 */

public class Gruber extends Character {


    public Gruber(int id) {
        super(id, 196, 196);
        setDx(11);
        setDy(11);
        setId(R.drawable.stickman);
    }


}