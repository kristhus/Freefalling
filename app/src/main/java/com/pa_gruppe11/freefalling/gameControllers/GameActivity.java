package com.pa_gruppe11.freefalling.gameControllers;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.pa_gruppe11.freefalling.Models.GameMap;
import com.pa_gruppe11.freefalling.Models.Character;
import com.pa_gruppe11.freefalling.R;
import android.view.ViewGroup;

import com.pa_gruppe11.freefalling.Models.GameMap;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.GameThread;
import com.pa_gruppe11.freefalling.implementations.models.SkyStage;
import com.pa_gruppe11.freefalling.tmp.TmpPlayer;
import com.pa_gruppe11.freefalling.tmp.TmpView;
import com.pa_gruppe11.freefalling.Models.Player;




/**
 * Created by Kristian on 31/03/2017.
 */
public class GameActivity extends GameMenu
        implements GoogleApiClient.ConnectionCallbacks,
                GoogleApiClient.OnConnectionFailedListener,
                OnInvitationReceivedListener {

    private Player[] players;
    private GameMap gameMap; //
    private Player thisPlayer; // REMOVE AFTER TESTING

    private PlayerController controller;

    private GoogleApiClient mGoogleApiClient;

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
        initiate();
    }

    public void initiate() {
        GameThread.getInstance().setActivity(this);
        //TODO: TESTING ONLY, REMOVE
        gameMap = new SkyStage();
        //player = new Player(R.drawable.stickman);
        thisPlayer = new Player(R.drawable.stickman);

        TmpView tmpView = new TmpView(this);
        setContentView(tmpView);
        GameThread.getInstance().setView(tmpView);

        controller = new PlayerController(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DataHandler.screenWidth, DataHandler.screenHeight);
        addContentView(controller, params);

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


    // TODO: Remove after testing.
    public Player getPlayer(){return thisPlayer;}

    public PlayerController getController() {
        return controller;
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
}
