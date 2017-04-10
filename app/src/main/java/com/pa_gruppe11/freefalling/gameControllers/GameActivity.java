package com.pa_gruppe11.freefalling.gameControllers;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.pa_gruppe11.freefalling.Models.GameMap;
import com.pa_gruppe11.freefalling.Models.GameMessage;
import com.pa_gruppe11.freefalling.Singletons.CollisionHandler;
import com.pa_gruppe11.freefalling.framework.GameServiceListener;
import com.pa_gruppe11.freefalling.implementations.models.Hanz;
import com.pa_gruppe11.freefalling.Models.Obstacle;
import com.pa_gruppe11.freefalling.Models.PowerUp;
import com.pa_gruppe11.freefalling.R;

import android.support.annotation.NonNull;
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
public class GameActivity extends GameMenu {

    // MODELS
    private Player[] opponents;
    private GameMap gameMap; //
    private Player thisPlayer;

    private ArrayList<Participant> participants;

    // TODO: REMOVE AFTER TESTING

    private Block testblock;

    // Controllers
    private PlayerController controller;

    private GameServiceListener serviceListener;

    private GameMessage gameMessage; // GameMessage sent to communicate character actions and movement
    private long messageTiming = 0;
    private long messageInterval = 100;
    private boolean updateBasedCommunication = true;

    private String drawPlayerName;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        room = (Room) getIntent().getExtras().get(Multiplayer.EXTRA_ROOM);
        serviceListener = DataHandler.getInstance().getMessageListener();
        serviceListener.addListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(serviceListener)
                .addOnConnectionFailedListener(serviceListener)
                .addApi(Games.API)
                .addScope(Games.SCOPE_GAMES)
                .build();
        mGoogleApiClient.connect();
    }

    public void initiate() {
        thisPlayer = new Player();
        thisPlayer.setCharacter(new Hanz(R.drawable.stickman, 65, 112));
        if(room != null) {
            participants = room.getParticipants();
            int i = 0;
            if(participants != null) {
                opponents = new Player[participants.size()-1];  //-1 is thisPlayer
                int index = 0;
                for(Participant p : participants) {
                    //TODO: replace - debug purposes
                    if(p.getPlayer() == null || !p.getPlayer().getPlayerId().equals(Games.Players.getCurrentPlayerId(mGoogleApiClient))) {
                        Hanz c = new Hanz(R.drawable.stickman, 65, 112);
                        c.setX(200*(index+1));
                        c.setY(200);
                        Player otherPlayer = new Player(p.getParticipantId(), i, null, c);
                        String displayName = p.getDisplayName();
                        otherPlayer.setDisplayName(displayName);
                        opponents[i] = otherPlayer;
                        i++;
                    }else {
                        thisPlayer.getCharacter().setX(200*(index+1));
                        thisPlayer.getCharacter().setY(200);
                        thisPlayer.setParticipantId(p.getParticipantId());
                        thisPlayer.setDisplayName(p.getDisplayName());
                    }
                }
            }
        }


        GameThread.getInstance().setActivity(this);

        gameMessage = new GameMessage(GameMessage.GAME_POSITION);

        //TODO: TESTING ONLY, REMOVE
        gameMap = new SkyStage();
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
          /*      if (CollisionHandler.getInstance().detectCollision(thisPlayer.getCharacter(), opponent.getCharacter())) {
                    thisPlayer.getCharacter().setCollidesWith(opponent.getCharacter());
                    CollisionHandler.getInstance().handleCollision(thisPlayer.getCharacter(), opponent.getCharacter());
                }
           */
                if(!updateBasedCommunication)
                    opponent.getCharacter().update(dt);
            }
        }


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

        // SEND GameMessage
        messageTiming+=dt;
        if(messageTiming > messageInterval || updateBasedCommunication) {   // TODO: remove, sending only once a second to not overflow monitor
            messageTiming -= messageInterval;
            if(mGoogleApiClient.isConnected() && participants != null) {
                gameMessage.setCharacterValues(thisPlayer.getCharacter());  // Prepare new values
//                byte[] message = ("Other person = (" + thisPlayer.getCharacter().getX() + ", " + thisPlayer.getCharacter().getY() + ")").getBytes();
                for (Participant p : participants) {
                    if (!p.getParticipantId().equals(Games.Players.getCurrentPlayerId(mGoogleApiClient))) {
                        Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, gameMessage.toBytes(), room.getRoomId());
                    }
                }
            }
        }

        //


        thisPlayer.getCharacter().update(dt);           // Update this player
        if (gameMap != null)
            gameMap.update(dt);     // Also updates the corresponding powerups and obstacles of the stage
        //testblock.update(dt);                           // Update this obstacle
    }

    public Player[] getOpponents() {
        return opponents;
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
        Log.w("GameActivity", "onActivityResult");
        switch(requestCode) {

        }
    }



    @Override
    public void connected() {
        super.connected();
        initiate();
        Log.w("GameActivity", "Connected!");
    }


    @Override
    public void connectionSuspended(int i) {
        Log.w("GameActivity", "Connection suspended");
    }

    @Override
    public void connectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w("GameActivity", "Failed to connect");
        Log.w("GameActivity", "" + connectionResult.getErrorCode());
        Log.w("GameActivity", connectionResult.getErrorMessage() + "");
    }

    @Override
    public void messageReceived(RealTimeMessage realTimeMessage) {
        if(!mConnected)
            return;
        byte[] bytes = realTimeMessage.getMessageData();
        GameMessage messageReceived = GameMessage.fromBytes(bytes);

        if(messageReceived != null) {
            String senderId = realTimeMessage.getSenderParticipantId();
            for(Player p : opponents) {
                if (p.getParticipantId().equals(senderId)) {
                    p.getCharacter().setValues(messageReceived);
                    break;
               }
            }
        }
        else
            Log.w("GameActivity","Got a message, but data corrupted");
    }

}
