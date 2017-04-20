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

        endY = 8000.0f;
        respawnPoints = new ArrayList<>();

        //TODO: temporary respawnpoints
        for(int i = 0; i < endY; i++) {
            respawnPoints.add(i*1000.0f);
        }

        DataHandler data = DataHandler.getInstance();


        // BLOCKS

        Block block1 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block1.setAngularVelocity((float) -Math.PI/2);
        block1.setX(data.getRelativeX(0));
        block1.setY(600);
        block1.setRotate(true);

        Block block2 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block2.setAngularVelocity((float) Math.PI/2);
        block2.setRotate(true);
        block2.setX(data.getRelativeX(59));
        block2.setY(800);

        Block block3 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block3.setAngularVelocity((float) Math.PI/2);
        block3.setRotate(true);
        block3.setX(data.getRelativeX(40));
        block3.setY(1200);

        Block block4 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block4.setAngularVelocity((float) Math.PI/6);
        block4.setRotate(true);
        block4.setX(data.getRelativeX(60));
        block4.setY(2000);

        Block block5 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block5.setAngularVelocity((float) Math.PI/6);
        block5.setRotate(true);
        block5.setX(data.getRelativeX(60));
        block5.setY(3200);

        Block block6 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block6.setAngularVelocity((float) -Math.PI/2);
        block6.setX(data.getRelativeX(20));
        block6.setY(3450);
        block6.setRotate(true);

        Block block7 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block7.setAngularVelocity((float) Math.PI/2);
        block7.setRotate(true);
        block7.setX(data.getRelativeX(80));
        block7.setY(4000);

        Block block8 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block8.setAngularVelocity((float) Math.PI/2);
        block8.setRotate(true);
        block8.setX(data.getRelativeX(300));
        block8.setY(4200);

        Block block9 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block9.setAngularVelocity((float) Math.PI/6);
        block9.setRotate(true);
        block9.setX(data.getRelativeX(70));
        block9.setY(4670);

        Block block10 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block10.setAngularVelocity((float) Math.PI/6);
        block10.setRotate(true);
        block10.setX(data.getRelativeX(200));
        block10.setY(5400);

        // SPINNINGKNIVES



        SpinningKnife knife1 = new SpinningKnife(R.drawable.knife, data.getRelativeWidth(20), data.getRelativeHeight(1.5f));
        knife1.setAngularVelocity((float) Math.PI/2);
        knife1.setRotate(true);
        knife1.setX(data.getRelativeX(78));
        knife1.setY(800);


        SpinningKnife knife2 = new SpinningKnife(R.drawable.knife, data.getRelativeWidth(20), data.getRelativeHeight(1.5f));
        knife2.setAngularVelocity((float) Math.PI/2);
        knife2.setRotate(true);
        knife2.setX(data.getRelativeX(50));
        knife2.setY(2100);


        SpinningKnife knife3 = new SpinningKnife(R.drawable.knife, data.getRelativeWidth(20), data.getRelativeHeight(1.5f));
        knife3.setAngularVelocity((float) Math.PI/2);
        knife3.setRotate(true);
        knife3.setX(data.getRelativeX(20));
        knife3.setY(3500);


        // SAWBLADES

        Sawblade sawblade1 = new Sawblade(data.getRelativeWidth(10), data.getRelativeWidth(10));
        sawblade1.setX(data.getRelativeX(5));
        sawblade1.setY(1400);

        Sawblade sawblade2 = new Sawblade(data.getRelativeWidth(10), data.getRelativeWidth(10));
        sawblade2.setX(data.getRelativeX(85));
        sawblade2.setY(1400);

        Sawblade sawblade3 = new Sawblade(data.getRelativeWidth(20), data.getRelativeWidth(20));
        sawblade3.setX(data.getRelativeX(40));
        sawblade3.setY(1400);
        sawblade3.setPeriodic(true, 80.0f, 0.0f);

        Sawblade sawblade4 = new Sawblade(data.getRelativeWidth(20), data.getRelativeWidth(20));
        sawblade4.setX(data.getRelativeX(40));
        sawblade4.setY(2400);
        sawblade4.setPeriodic(true, 80.0f, 0.0f);

        Sawblade sawblade5 = new Sawblade(data.getRelativeWidth(20), data.getRelativeWidth(20));
        sawblade5.setX(data.getRelativeX(40));
        sawblade5.setY(2700);
        sawblade5.setPeriodic(true, 140.0f, 0.0f);

        Sawblade sawblade6 = new Sawblade(data.getRelativeWidth(20), data.getRelativeWidth(20));
        sawblade6.setX(data.getRelativeX(40));
        sawblade6.setY(5600);
        sawblade6.setPeriodic(true, 140.0f, 0.0f);

        Sawblade sawblade7 = new Sawblade(data.getRelativeWidth(100), data.getRelativeWidth(20));
        sawblade7.setX(data.getRelativeX(300));
        sawblade7.setY(5600);
        sawblade7.setPeriodic(true, 140.0f, 0.0f);



        // PUSH BLOCKS
        Block pushBlock = new Block(R.drawable.block, data.getRelativeWidth(3), data.getRelativeHeight(20));
        pushBlock.setX(data.getRelativeX(10.0f));
        pushBlock.setY(1800.0f);
        pushBlock.setMaxDx(1000.0f);
        pushBlock.setPeriodic(true, 1000.0f, 0.0f);
        pushBlock.setPeriodicMaxTime(8000);

        Block pushBlock2 = new Block(R.drawable.block, data.getRelativeWidth(3), data.getRelativeHeight(20));
        pushBlock.setX(data.getRelativeX(10.0f));
        pushBlock.setY(3000.0f);
        pushBlock.setMaxDx(600.0f);
        pushBlock.setPeriodic(true, 1000.0f, 0.0f);
        pushBlock.setPeriodicMaxTime(8000);


        addObstacle(block1);
        addObstacle(block2);
        addObstacle(block3);
        addObstacle(block4);
        addObstacle(block5);
        addObstacle(block6);
        addObstacle(block7);
        addObstacle(block8);
        addObstacle(block9);
        addObstacle(block10);

        addObstacle(knife1);
        addObstacle(knife2);
        addObstacle(knife3);

        addObstacle(sawblade1);
        addObstacle(sawblade2);
        addObstacle(sawblade3);
        addObstacle(sawblade4);
        addObstacle(sawblade5);
        addObstacle(sawblade6);
        addObstacle(pushBlock);
        addObstacle(pushBlock2);


    }


}
