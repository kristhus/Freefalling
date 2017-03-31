package com.pa_gruppe11.freefalling.gameControllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
/**
 * Created by Martin Kostveit on 31.03.2017.
 *
 */
public class GameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Drawable[] drawables = ...;
        GameView gameView = new GameView(this, drawables);
        setContentView(gameView);
    }
}
