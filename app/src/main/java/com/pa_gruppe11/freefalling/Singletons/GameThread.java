package com.pa_gruppe11.freefalling.Singletons;

import android.app.Activity;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.pa_gruppe11.freefalling.gameControllers.GameActivity;

/**
 * Created by Kristian on 21/03/2017.
 * The purpose of this class is to update and redraw the game in a way, such that the update is prioritized to achieve correct movement.
 * If the thread lags behind, it will update, and skip redrawing within that period.
 * If the thread is ajour or better, it will sleep to maintain FPS. 
 * The FPS can be changed on a need-be-basis 
 */
public class GameThread extends Thread { //

    private GameActivity activity;										// Activity to be updated
    private SurfaceView view;												// View to be redrawn
    private final int MAX_FPS = 60;     								// We need to move objects a fixed percentage compared to a constant variable, e.g. pixels/second, not per frame, to accomodate players that can run in 60fps
    private boolean running;												// boolean in while loop
    private final int MAX_SKIPS = 5;	 								// Max amount of skipped draws, before drawing will be performed regardless of performance delay.
    private final int PERIOD_LENGTH = 1000/MAX_FPS;	                    // milliseconds per frame

    private boolean started;												// Thread has been initialized

    private long elapsedTime = 0;										// Time in millis since execution of thread. Currently not in use

	private static GameThread gameThread = new GameThread();

	
    private GameThread(){
		// Wait for config to finish loading, then get relevant settings
    }

    @Override
    public void run(){ 
	    
        started = true;
        long beginTime; 			// Time when the cycle begins
        long dt = MAX_FPS/1000;  					// Time it took for the cycle to execute, init at perfect value
        int sleepTime; 				// ms to sleep (<0 if behind)
        int framesSkipped=0; 	// Number of frames being skipped

        while(running) {
            Canvas canvas = null;
            try {
                canvas = view.getHolder().lockCanvas();			//
                synchronized (view.getHolder()) {						// Make sure that only this thread gets the view, and no other classes interfers while drawing
                    Log.w("GameThread", "dt: " + dt);
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0;
                    activity.update(dt);										// Update the activity
                    if(canvas != null)                                          // Null before fully initialized, ignore untill creation
                        view.draw(canvas);										// Draw the view
                    sleepTime = (int)(PERIOD_LENGTH - dt);	// The time necessary to sleep to maintain the FPS selected

                    if(sleepTime > 0){										
                        try{
                            Thread.sleep(sleepTime);					// Sleep for the calculated amount of time required
                            Log.w("GameThread", "Slept for " + sleepTime + " ms");
                        }catch (InterruptedException e){}
                    }
                    while (sleepTime < 0 && framesSkipped < MAX_SKIPS) {        // If not possible to maintain FPS, update controller to catch up
                        beginTime = System.currentTimeMillis();
                        activity.update(dt);
                        sleepTime += PERIOD_LENGTH;
                        framesSkipped++;
                        dt = System.currentTimeMillis() - beginTime;
                    }
                    dt = System.currentTimeMillis() - beginTime;	// Time elapsed in current loop, to be used in controller's update
                }
            }
            finally{
                if (canvas != null) {
                    view.getHolder().unlockCanvasAndPost(canvas);	// Release the view, for others to use
                }
            }
        }

    }
	
	
	public GameActivity getCurrentActivity() {
		return activity;
	}
	public void setActivity(GameActivity activity) {
		this.activity = activity;
	}
	public SurfaceView getCurrentView() {
		return view;
	}
	public void setView(SurfaceView view) {
		this.view = view;
	}
	

    public void setRunning(boolean running) {	// 
        this.running = running;
    }
    public boolean isStarted() {
        return started;
    }

	
	public static GameThread getInstance() {
		return gameThread;
	}
	
}
