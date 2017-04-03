package com.pa_gruppe11.freefalling.gameControllers;

import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;

import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;

/**
 * Created by skars on 31.03.2017.
 */

public class MainMenu extends GameMenu{

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.mainmenu);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        DataHandler.getInstance().screenWidth = size.x;
        DataHandler.getInstance().screenHeight = size.y;
    }


    public void goToAboutMenu(View view){
        super.goTo(AboutMenu.class);
    }

    public void goToFriendsMenu(View view){

    }

    public void goToSettings(View view){

    }

    public void goToLobby(View view){

    }

    public void startGame(View view) {
        //TODO: remove load
        ResourceLoader.getInstance().loadImage(R.drawable.bg_sky, this);
        goTo(GameActivity.class);
    }

}

