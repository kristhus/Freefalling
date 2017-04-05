package com.pa_gruppe11.freefalling.gameControllers;

import android.os.Bundle;

import com.pa_gruppe11.freefalling.Models.GameMap;
import com.pa_gruppe11.freefalling.Models.Hanz;
import com.pa_gruppe11.freefalling.Models.Obstacle;
import com.pa_gruppe11.freefalling.Models.PowerUp;
import com.pa_gruppe11.freefalling.R;
import android.view.ViewGroup;

import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.GameThread;
import com.pa_gruppe11.freefalling.implementations.models.SkyStage;
import com.pa_gruppe11.freefalling.tmp.TmpView;
import com.pa_gruppe11.freefalling.Models.Player;

/**
 * Created by Kristian on 31/03/2017.
 */
public class GameActivity extends GameMenu {

    private Player[] opponents;
    private GameMap gameMap; //
    private Player thisPlayer; // REMOVE AFTER TESTING

    private PlayerController controller;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        initiate();
    }

    public void initiate() {
        GameThread.getInstance().setActivity(this);

        //TODO: TESTING ONLY, REMOVE
        gameMap = new SkyStage();
        thisPlayer = new Player();
        thisPlayer.setCharacter(new Hanz(R.drawable.stickman));

        TmpView tmpView = new TmpView(this);
        setContentView(tmpView);
        GameThread.getInstance().setView(tmpView);

        controller = new PlayerController(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DataHandler.screenWidth, DataHandler.screenHeight);
        addContentView(controller, params);

        notifyReady();
    }

    public void update(long dt) {
        if(opponents != null) {
            for (Player opponent : opponents) {
                if(thisPlayer.getCharacter().collides(opponent.getCharacter())) {
                    thisPlayer.getCharacter().setCollidesWith(opponent.getCharacter());
                }
                opponent.getCharacter().update(dt);
            }
        }
        if(gameMap != null)
            gameMap.update(dt);     // Also updates the corresponding powerups and obstacles of the stage

        // Update powerups and obstacles
        Obstacle[] obstacles = gameMap.getObstacles();
        if(obstacles != null) {
            for(Obstacle o : obstacles) {
                if(thisPlayer.getCharacter().collides(o))
                    thisPlayer.getCharacter().setCollidesWith(o);
            }
        }

        PowerUp[] powerUps = gameMap.getPowerups();
        if(powerUps != null) {
            for(PowerUp p : powerUps) {
                if(thisPlayer.getCharacter().collides(p))
                    thisPlayer.getCharacter().setCollidesWith(p);
            }
        }

        thisPlayer.getCharacter().update(dt);           // Update this player
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


    // TODO: Remove after testing.
    public Player getPlayer(){return thisPlayer;}

    public PlayerController getController() {
        return controller;
    }

}
