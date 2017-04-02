package com.pa_gruppe11.freefalling.implementations.models;

import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Models.GameMap;
import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by Kristian on 02/04/2017.
 */

public class SkyStage extends GameMap {

    public SkyStage() {
        //super(id, image, powerups, obstacles);
        id = R.drawable.bg_sky;
        image = ResourceLoader.getInstance().getImage(id);
    }



    @Override
    public void update(long dt) {

    }

}
