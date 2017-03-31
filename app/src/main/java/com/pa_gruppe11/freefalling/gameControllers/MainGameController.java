package com.pa_gruppe11.freefalling.gameControllers;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.pa_gruppe11.freefalling.R;
import android.widget.ImageView;

/**
 * Created by Martin Kostveit on 31.03.2017.
 *
 */

public class MainGameController extends AppCompatActivity {
    int delay=50, width=120, height=52;
    int obstacleVelX = 20;
    Player playerA, playerB;
    Rect rec1, rec2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameView);

        ImageView player1 = (ImageView) findViewById(R.id.player1);
        //ImageView player2 = (ImageView) findViewById(R.id.player2);

        playerA = new Player(player1);
        //playerB = new Player(player2);
        th.start();
    }
    boolean rectCollision() {
        rec1 = playerA.getRect();
        rec2 = playerB.getRect();
        if(rec1.intersect(rec2)) {
            return true;
        }
        return false;
    }

    Handler handler = new Handler();
    Thread th = new Thread() {
        public void run() {
            if(rectCollision()) {

            }
            handler.postDelayed(this, delay);
        }
    };
}
