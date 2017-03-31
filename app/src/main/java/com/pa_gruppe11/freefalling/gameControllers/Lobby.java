package com.pa_gruppe11.freefalling.gameControllers;

import android.os.Bundle;

import com.pa_gruppe11.freefalling.R;

/**
 * Created by skars on 31.03.2017.
 */

public class Lobby extends GameMenu {

    private String message;
    private boolean ready;



    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.lobby);
    }

    public void inviteToGame(String id){

    }

    public void sendMessage(String message){

    }

    public boolean isReady(){
        return true;
    }

    public void invite(String id){

    }



}
