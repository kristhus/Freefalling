package com.pa_gruppe11.freefalling.gameControllers;

import android.os.Bundle;

import com.pa_gruppe11.freefalling.Models.GameMap;
import com.pa_gruppe11.freefalling.Singletons.GameThread;
import com.pa_gruppe11.freefalling.implementations.models.SkyStage;
import com.pa_gruppe11.freefalling.tmp.TmpView;

/**
 * Created by Kristian on 31/03/2017.
 */
public class GameActivity extends GameMenu {

    private Player[] players;
    private GameMap gameMap; //

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        initiate();
    }

    public void initiate() {
        GameThread.getInstance().setActivity(this);

        //TODO: TESTING ONLY, REMOVE
        gameMap = new SkyStage();

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
        if(!GameThread.getInstance().isStarted()) {
            GameThread.getInstance().setRunning(true);
            GameThread.getInstance().start();
        }
        else {  // First start after init app.
            synchronized (GameThread.getInstance()) {
                GameThread.getInstance().notify();
            }
        }
    }


    public void finishGame() {
        //Switch View to postMatchView
        GameThread.getInstance().setSuspended(true);
    }

    public GameMap getGameMap() {
        return gameMap;
    }

}
