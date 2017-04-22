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

        endY = 6000.0f;
        respawnPoints = new ArrayList<>();

        //TODO: temporary respawnpoints
        for(int i = 0; i < endY; i++) {
            respawnPoints.add(i*1000.0f);
        }

        DataHandler data = DataHandler.getInstance();

        /*

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

        SpinningKnife knife1 = new SpinningKnife(R.drawable.knife, data.getRelativeWidth(20), data.getRelativeHeight(1.5f));
        knife1.setAngularVelocity((float) Math.PI/2);
        knife1.setRotate(true);
        knife1.setX(data.getRelativeX(78));
        knife1.setY(800);

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

        Block pushBlock = new Block(R.drawable.block, data.getRelativeWidth(3), data.getRelativeHeight(20));
        pushBlock.setX(data.getRelativeX(10.0f));
        pushBlock.setY(1800.0f);
        pushBlock.setMaxDx(1000.0f);
        pushBlock.setPeriodic(true, 1000.0f, 0.0f);
        pushBlock.setPeriodicMaxTime(8000);

        addObstacle(block1);
        addObstacle(block2);
        addObstacle(block3);
        addObstacle(knife1);
        addObstacle(sawblade1);
        addObstacle(sawblade2);
        addObstacle(sawblade3);
        addObstacle(pushBlock);

*/
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

        Block block4 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block4.setAngularVelocity((float) Math.PI/2);
        block4.setRotate(true);
        block4.setX(data.getRelativeX(20));
        block4.setY(1200);

        Block block5 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block5.setAngularVelocity((float) -Math.PI/4);
        block5.setRotate(true);
        block5.setX(data.getRelativeX(0));
        block5.setY(1600);

        Block block6 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block6.setAngularVelocity((float) -Math.PI/2);
        block6.setRotate(true);
        block6.setX(data.getRelativeX(40));
        block6.setY(2100);

        Block block7 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block7.setAngularVelocity((float) -Math.PI/4);
        block7.setRotate(true);
        block7.setX(data.getRelativeX(0));
        block7.setY(2100);


        Block block8 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block8.setAngularVelocity((float) -Math.PI/4);
        block8.setRotate(true);
        block8.setX(data.getRelativeX(0));
        block8.setY(3400);

        Block block9 = new Block(R.drawable.block, data.getRelativeWidth(40), data.getRelativeHeight(3));
        block9.setAngularVelocity((float) Math.PI/2);
        block9.setRotate(true);
        block9.setX(data.getRelativeX(50));
        block9.setY(3400);


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

        SpinningKnife knife4 = new SpinningKnife(R.drawable.knife, data.getRelativeWidth(40), data.getRelativeHeight(3.0f));
        knife4.setAngularVelocity((float) Math.PI/2);
        knife4.setRotate(true);
        knife4.setX(data.getRelativeX(40));
        knife4.setY(5800);


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
        sawblade5.setX(data.getRelativeX(40));
        sawblade5.setY(3400);
        sawblade5.setPeriodic(true, 140.0f, 0.0f);

        Sawblade sawblade7 = new Sawblade(data.getRelativeWidth(20), data.getRelativeWidth(20));
        sawblade7.setX(data.getRelativeX(0));
        sawblade7.setY(4200);
        sawblade7.setPeriodic(true, 140.0f, 0.0f);

        Sawblade sawblade8 = new Sawblade(data.getRelativeWidth(20), data.getRelativeWidth(20));
        sawblade5.setX(data.getRelativeX(60));
        sawblade5.setY(4200);
        sawblade5.setPeriodic(true, 140.0f, 0.0f);

        Sawblade sawblade9 = new Sawblade(data.getRelativeWidth(40), data.getRelativeWidth(40));
        sawblade9.setX(data.getRelativeX(40));
        sawblade9.setY(5600);
        sawblade9.setPeriodic(true, 140.0f, 0.0f);

        Sawblade sawblade10 = new Sawblade(data.getRelativeWidth(40), data.getRelativeWidth(40));
        sawblade10.setX(data.getRelativeX(300));
        sawblade10.setY(5600);
        sawblade10.setPeriodic(true, 140.0f, 0.0f);



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

        Block pushBlock3 = new Block(R.drawable.block, data.getRelativeWidth(3), data.getRelativeHeight(20));
        pushBlock.setX(data.getRelativeX(10.0f));
        pushBlock.setY(5800.0f);
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
      /*  addObstacle(block4);
        addObstacle(block5);
        addObstacle(block6);
        addObstacle(block7);
        addObstacle(block8);
        addObstacle(block9);
        addObstacle(block10);
*/
        addObstacle(knife1);
        addObstacle(knife2);
        addObstacle(knife3);

        addObstacle(sawblade1);
        addObstacle(sawblade2);
        addObstacle(sawblade3);
        addObstacle(sawblade4);
        addObstacle(sawblade5);
        addObstacle(sawblade6);
        addObstacle(sawblade7);
        addObstacle(sawblade8);
        addObstacle(sawblade9);
        addObstacle(sawblade10);

        // Push blocks

        addObstacle(pushBlock);
        addObstacle(pushBlock2);
        addObstacle(pushBlock3);

    }


}
