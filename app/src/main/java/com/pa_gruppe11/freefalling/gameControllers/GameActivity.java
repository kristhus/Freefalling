package com.pa_gruppe11.freefalling.gameControllers;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.pa_gruppe11.freefalling.Models.GameMap;
import com.pa_gruppe11.freefalling.Models.Character;
import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.Singletons.GameThread;
import com.pa_gruppe11.freefalling.implementations.models.SkyStage;
import com.pa_gruppe11.freefalling.tmp.TmpPlayer;
import com.pa_gruppe11.freefalling.tmp.TmpView;

/**
 * Created by Kristian on 31/03/2017.
 */
public class GameActivity extends GameMenu {

    private Player[] players;
    private GameMap gameMap; //
    private TmpPlayer tmpPlayer; // REMOVE AFTER TESTING

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        initiate();
    }

    public void initiate() {
        GameThread.getInstance().setActivity(this);

        //TODO: TESTING ONLY, REMOVE
        gameMap = new SkyStage();
        tmpPlayer = new TmpPlayer();

        TmpView tmpView = new TmpView(this);
        setContentView(tmpView);
        GameThread.getInstance().setView(tmpView);

        notifyReady();
    }

    public void update(long dt) {
        if(players != null) {
            for (Player player : players) {
                player.getCharacter().update(dt);
            }
        }
        if(gameMap != null)
            gameMap.update(dt);
    }


    public void notifyReady() {
        GameThread.getInstance().setRunning(true);
        GameThread.getInstance().start();
    }


    public void finishGame() {
        //Switch View to postMatchView
        GameThread.getInstance().setRunning(false);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    // REMOVE AFTER TESTING
    public TmpPlayer getTmpPlayer(){return tmpPlayer;}

}
