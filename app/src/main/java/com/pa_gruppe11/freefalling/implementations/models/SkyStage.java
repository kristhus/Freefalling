package com.pa_gruppe11.freefalling.implementations.models;

import android.util.Log;
import android.widget.ImageView;

import com.pa_gruppe11.freefalling.Models.GameMap;
import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

import java.util.ArrayList;

/**
 * Created by Kristian on 02/04/2017.
 */

public class SkyStage extends GameMap {

    public SkyStage() {
        super(R.drawable.bg_sky);

        endY = 10000.0f;
        respawnPoints = new ArrayList<>();

        //TODO: temporary respawnpoints
        for(int i = 0; i < endY; i++) {
            respawnPoints.add(i*1000.0f);
        }

        DataHandler data = DataHandler.getInstance();

        Block block1 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block1.setAngularVelocity((float) -Math.PI/2);
        block1.setX(data.getRelativeX(0));
        block1.setY(400);
        block1.setRotate(true);

        Block block2 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block2.setAngularVelocity((float) Math.PI/2);
        block2.setRotate(true);
        block2.setX(data.getRelativeX(59));
        block2.setY(400);

        Block block3 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block3.setAngularVelocity((float) Math.PI/2);
        block3.setRotate(true);
        block3.setX(data.getRelativeX(40));
        block3.setY(800);

        SpinningKnife knife1 = new SpinningKnife(R.drawable.knife, data.getRelativeWidth(20), data.getRelativeHeight(3));
        knife1.setAngularVelocity((float) Math.PI/2);
        knife1.setRotate(true);
        knife1.setX(data.getRelativeX(78));
        knife1.setY(800);

        addObstacle(block1);
        addObstacle(block2);
        addObstacle(block3);
        addObstacle(knife1);



    }


}
