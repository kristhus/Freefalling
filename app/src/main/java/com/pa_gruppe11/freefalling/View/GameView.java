package com.pa_gruppe11.freefalling.View;

import android.graphics.Canvas;
import android.view.SurfaceView;

import com.pa_gruppe11.freefalling.Models.Player;
import com.pa_gruppe11.freefalling.gameControllers.GameActivity;

/**
 * Created by Kristian on 03/04/2017.
 */

public class GameView extends SurfaceView{

    private GameActivity gameContext;

    public GameView(GameActivity context) {
        super(context);
        gameContext = context;
    }

    // The order in which the different objects get drawn are critical for
    // displaying which object is "on top" of the others.
    public void draw(Canvas canvas) {
        super.draw(canvas);                     // remove previous artefacts
        gameContext.getGameMap().draw(canvas);  // draw current frame
        gameContext.getPlayer().getCharacter().draw(canvas);
        if(gameContext.getOpponents() != null) {
            for (String participantIds : gameContext.getOpponents().keySet()) {
                Player p = gameContext.getOpponents().get(participantIds);
                p.getCharacter().draw(canvas);
            }
        }
    }


}
