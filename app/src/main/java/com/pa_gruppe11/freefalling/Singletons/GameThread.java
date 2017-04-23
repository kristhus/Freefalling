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
 * If the thread lags behind, it will update, and skip redrawing within that period.    (while-loop disabled for now)
 * If the thread is ajour or better, it will sleep to maintain FPS. 
 * The FPS can be changed on a need-be-basis 
 */
public class GameThread extends Thread { //

    private GameActivity activity;									// Activity to be updated
    private SurfaceView view;										// View to be redrawn
    private int MAX_FPS = DataHandler.getInstance().getFPS();       // We need to move objects a fixed percentage compared to a constant variable, e.g. pixels/second, not per frame, to accomodate players that can run in 60fps
    private boolean running;										// boolean in while loop
    private final int MAX_SKIPS = 5;	 							// Max amount of skipped draws, before drawing will be performed regardless of performance delay.
    private int PERIOD_LENGTH = 1000/MAX_FPS;	                    // milliseconds per frame

    private boolean started;										// Thread has been initialized

    private boolean suspended = false;

    private long elapsedTime = 0;									// Time in millis since execution of thread.

	private static GameThread gameThread = new GameThread();


    // Debugging fields
    private String performanceString = "";
    private float dxs = 0.0f;
    private float dys = 0.0f;



    private GameThread(){
		// Wait for config to finish loading, then get relevant settings
    }

    @Override
    public void run(){

        elapsedTime = 0;
        started = true;
        long beginTime;                   // Time when the cycle begins
        long dt = 0;                      // Time it took for the cycle to execute
        int sleepTime = PERIOD_LENGTH;    // ms to sleep (<0 if behind)
        int framesSkipped = 0;            // Number of frames being skipped

        int fpsCounter = 0;
        while (running) {
            while(suspended)              // suspend run-method, as advised by Oracle
                try {
                    synchronized (this) {
                        Log.w("GT", "PINA.DE");
                        wait();           // Must notify(), to resume
                        Log.w("GT", "PINA.DE ferdig far");
                        suspended = false;
                    }
                    // Reinitialize settings
                    elapsedTime = 0;
                    started = true;
                    dt = 0;
                    sleepTime = PERIOD_LENGTH;
                    framesSkipped = 0;
                    elapsedTime = 0;
                    //
                }catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.w("GameThread", "Thread got interrupted, Fatal error, terminating GameThread. Restart application");
                    running = false;
                    break;
                }


            Canvas canvas = null;
            //Log.w("GT", "RUNNING");
            try {
                canvas = view.getHolder().lockCanvas();            //
                synchronized (view.getHolder()) {                        // Make sure that only this thread gets the view, and no other classes interfers while drawing
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0;
                    activity.update(sleepTime + dt);                                        // Update the activity
                    if (canvas != null)                                          // Null before fully initialized, ignore untill creation
                        view.draw(canvas);                                        // Draw the view
                    dt = System.currentTimeMillis() - beginTime;
                    sleepTime = (int) (PERIOD_LENGTH - dt);    // The time necessary to sleep to maintain the FPS selected
                    if (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);                            // Sleep for the calculated amount of time required
                        } catch (InterruptedException e) {
                        }
                    }/*
                    while (sleepTime < 0 && framesSkipped < MAX_SKIPS) {        // If not possible to maintain FPS, update controller to catch up
                        beginTime = System.currentTimeMillis();
                        activity.update(dt);
                        sleepTime += PERIOD_LENGTH;
                        framesSkipped++;
                        dt = System.currentTimeMillis() - beginTime;
                    }
                    */
                /*    if(framesSkipped > 0) {
                        Log.w("GameThread", "Skipped " + framesSkipped + " frames");
                    }*/
                    //dt = System.currentTimeMillis() - beginTime;	// Time elapsed in current loop, to be used in controller's update
                    elapsedTime+=dt;
                    dxs += activity.getPlayer().getCharacter().getDx();
                    dys += activity.getPlayer().getCharacter().getDy();
                    fpsCounter++;
                    if(elapsedTime > 1000) {
                        elapsedTime -= 1000;
                        performanceString = "FPS: " + fpsCounter;
                        activity.getPlayer().getCharacter().setDebugString(performanceString);
                        dxs = 0.0f;
                        dys = 0.0f;
                        fpsCounter = 0;
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
            }finally {
                if (canvas != null) {
                    view.getHolder().unlockCanvasAndPost(canvas);    // Release the view, for others to use
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


    /**
     * Terminates the thread. Should not be called, unless quitting the application
     * @param running
     */
    public void setRunning(boolean running) {	//
        this.running = running;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }
    public boolean isSuspended() {
        return suspended;
    }


    public boolean isStarted() {
        return started;
    }

	
	public static GameThread getInstance() {
		return gameThread;
	}

	public void reloadValues() {
        Log.w("GameThread", "reloadValues   old values: " + MAX_FPS + ", " + PERIOD_LENGTH + "   new: " + DataHandler.getInstance().getFPS() + ",  " + 1000/DataHandler.getInstance().getFPS());
        MAX_FPS = DataHandler.getInstance().getFPS();
        PERIOD_LENGTH = 1000/DataHandler.getInstance().getFPS();
    }

    // Suspending the thread did not work as intended, so this method was required
    public void stop_gameThread() {
        gameThread = new GameThread();
    }


    public long getElapsedTime() {
        return elapsedTime;
    }
}
