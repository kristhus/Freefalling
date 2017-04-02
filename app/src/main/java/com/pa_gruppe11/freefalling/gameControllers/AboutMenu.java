package com.pa_gruppe11.freefalling.gameControllers;

import android.os.Bundle;
import android.view.View;

import com.pa_gruppe11.freefalling.R;

/**
 * Created by skars on 31.03.2017.
 */

public class AboutMenu extends GameMenu {

    private String info;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.aboutmenu);
    }


}
