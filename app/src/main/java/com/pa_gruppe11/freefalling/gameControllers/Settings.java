package com.pa_gruppe11.freefalling.gameControllers;

import android.os.Bundle;
import android.view.View;

import com.pa_gruppe11.freefalling.R;

/**
 * Created by skars on 31.03.2017.
 */

public class Settings extends GameMenu {

    private int sfx;
    private int bgm;


    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.Settings);
    }


    public void save(View view){

    }

    public void loadValuesToView(){

    }



}
