package com.pa_gruppe11.freefalling.tmp;

import android.graphics.Canvas;
import android.view.SurfaceView;

import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;
import com.pa_gruppe11.freefalling.gameControllers.GameActivity;

/**
 * Created by Kristian on 03/04/2017.
 */

public class TmpView extends SurfaceView{

    private GameActivity gameContext;

    public TmpView(GameActivity context) {
        super(context);
        gameContext = context;
    }

    // The order in which the different objects get drawn are critical for
    // displaying which object is "on top" of the others.
    public void draw(Canvas canvas) {
        super.draw(canvas);                     // remove previous artefacts
        gameContext.getGameMap().draw(canvas);  // draw current frame
        gameContext.getObstacle().draw(canvas);
        gameContext.getPlayer().getCharacter().draw(canvas);
    }


}
