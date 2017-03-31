package com.pa_gruppe11.freefalling.gameControllers;

import com.pa_gruppe11.freefalling.Models.GameMap;
import com.pa_gruppe11.freefalling.Models.Character;
import com.pa_gruppe11.freefalling.Singletons.GameThread;

/**
 * Created by Kristian on 31/03/2017.
 */
public class GameActivity extends GameMenu {

    private Player[] players;
    private GameMap gameMap;



    public GameActivity() {
        GameThread.getInstance().setActivity(this);

    }

    public void update(long dt) {
        for(Player player : players) {
            player.getCharacter().update(dt);
        }
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


}
