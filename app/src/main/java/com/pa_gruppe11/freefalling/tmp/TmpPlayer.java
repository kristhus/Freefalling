package com.pa_gruppe11.freefalling.tmp;

import com.pa_gruppe11.freefalling.Models.Character;
import com.pa_gruppe11.freefalling.R;

/**
 * Created by Thomas on 03.04.2017.
 */

public class TmpPlayer {

    Character character;

    public TmpPlayer(){
        character = new Character(R.drawable.stickman);
    }

    public Character getCharacter(){
        return character;
    }



}
