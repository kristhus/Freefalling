package com.pa_gruppe11.freefalling.gameControllers;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.pa_gruppe11.freefalling.Models.GameMap;
import com.pa_gruppe11.freefalling.Singletons.CollisionHandler;
import com.pa_gruppe11.freefalling.implementations.models.Hanz;
import com.pa_gruppe11.freefalling.Models.Obstacle;
import com.pa_gruppe11.freefalling.Models.PowerUp;
import com.pa_gruppe11.freefalling.R;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;

import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.GameThread;
import com.pa_gruppe11.freefalling.implementations.models.Block;
import com.pa_gruppe11.freefalling.implementations.models.SkyStage;
import com.pa_gruppe11.freefalling.tmp.TmpView;
import com.pa_gruppe11.freefalling.Models.Player;

import java.util.ArrayList;

/**
 * Created by Kristian on 31/03/2017.
 */
public class GameActivity extends GameMenu
        implements GoogleApiClient.ConnectionCallbacks,
                GoogleApiClient.OnConnectionFailedListener,
                RealTimeMessageReceivedListener,
                OnInvitationReceivedListener {

    // MODELS
    private Player[] opponents;
    private GameMap gameMap; //
    private Player thisPlayer;

    private ArrayList<Participant> participants;

    // TODO: REMOVE AFTER TESTING

    private Block testblock;

    // Controllers
    private PlayerController controller;

    private GoogleApiClient mGoogleApiClient;
    private Room room;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();
        mGoogleApiClient.connect();

        room = (Room) getIntent().getExtras().get(Multiplayer.EXTRA_ROOM);
        if(room != null)
            participants = room.getParticipants();
        initiate();
    }

    public void initiate() {
        GameThread.getInstance().setActivity(this);

        //TODO: TESTING ONLY, REMOVE
        gameMap = new SkyStage();
        thisPlayer = new Player();
        thisPlayer.setCharacter(new Hanz(R.drawable.stickman, 65, 112));
        testblock = new Block(R.drawable.block, 106, 61);

        gameMap.addObstacle(testblock);

        TmpView tmpView = new TmpView(this);
        setContentView(tmpView);
        GameThread.getInstance().setView(tmpView);

        controller = new PlayerController(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                DataHandler.getInstance().getScreenWidth(),
                DataHandler.getInstance().getScreenHeight());
        addContentView(controller, params);

        notifyReady();
    }

    public void update(long dt) {
        if (opponents != null) {
            for (Player opponent : opponents) {
                //if(thisPlayer.getCharacter().collides(opponent.getCharacter())) {
                if (CollisionHandler.getInstance().detectCollision(thisPlayer.getCharacter(), opponent.getCharacter())) {
                    thisPlayer.getCharacter().setCollidesWith(opponent.getCharacter());
                    CollisionHandler.getInstance().handleCollision(thisPlayer.getCharacter(), opponent.getCharacter());
                }
                opponent.getCharacter().update(dt);
            }
        }
        if (gameMap != null)
            gameMap.update(dt);     // Also updates the corresponding powerups and obstacles of the stage

        // Update powerups and obstacles
        ArrayList<Obstacle> obstacles = gameMap.getObstacles();
        if (obstacles != null) {
            for (Obstacle o : obstacles) {
                if (CollisionHandler.getInstance().detectCollision(thisPlayer.getCharacter(), o)) {
                    thisPlayer.getCharacter().setCollidesWith(o);
                    CollisionHandler.getInstance().handleCollision(thisPlayer.getCharacter(), o);
                }
            }
        }

        ArrayList<PowerUp> powerUps = gameMap.getPowerups();
        if (powerUps != null) {
            for (PowerUp p : powerUps) {
                //if(thisPlayer.getCharacter().collides(p))
                //  thisPlayer.getCharacter().setCollidesWith(p);
                if (CollisionHandler.getInstance().detectCollision(thisPlayer.getCharacter(), p)) {
                    thisPlayer.getCharacter().setCollidesWith(p);
                    CollisionHandler.getInstance().handleCollision(thisPlayer.getCharacter(), p);
                }
            }
        }

        // SEND SHIT

        if(mGoogleApiClient.isConnected()) {
            byte[] message = ("Other person = (" + thisPlayer.getCharacter().getX() + ", " + thisPlayer.getCharacter().getY()).getBytes();
            for (Participant p : participants) {
                if (!p.getParticipantId().equals(Games.Players.getCurrentPlayerId(mGoogleApiClient))) {
                    Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, message, "pina");
                }
            }
        }

        //


        thisPlayer.getCharacter().update(dt);           // Update this player
        testblock.update(dt);                           // Update this obstacle
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

    public Obstacle getObstacle(){return testblock;}

    public PlayerController getController() {
        return controller;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w("MainMenu", "onActivityResult");
        switch(requestCode) {

        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.w("GameActivity", "Connected!");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w("GameActivity", "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w("GameActivity", "Failed to connect");
        Log.w("GameActivity", "" + connectionResult.getErrorCode());
        Log.w("GameActivity", connectionResult.getErrorMessage() + "");
    }

    @Override
    public void onInvitationReceived(Invitation invitation) {
        Log.w("GameActivity", "Received an invitation");
    }

    @Override
    public void onInvitationRemoved(String s) {

    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
        byte[] b = realTimeMessage.getMessageData();
        String message = new String(b);
        Log.w("GameActivity", "Got a message: " + message);
    }
}
