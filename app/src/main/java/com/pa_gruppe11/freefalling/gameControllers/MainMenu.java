package com.pa_gruppe11.freefalling.gameControllers;

import android.os.Bundle;
import android.view.View;

import com.pa_gruppe11.freefalling.R;

/**
 * Created by skars on 31.03.2017.
 */

public class MainMenu extends GameMenu{

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.mainmenu);
    }


    public void goToAboutMenu(View view){
        goTo(AboutMenu.class);
    }

    public void goToFriendsMenu(View view){
        goTo(FriendsMenu.class);
    }

    public void goToSettings(View view){
        goTo(Settings.class);
    }

    public void goToLobby(View view){
        goTo(Lobby.class);
    }

}

