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
import com.pa_gruppe11.freefalling.Collidable;
import com.pa_gruppe11.freefalling.Models.GameMap;
import com.pa_gruppe11.freefalling.Models.GameMessage;
import com.pa_gruppe11.freefalling.Singletons.CollisionHandler;
import com.pa_gruppe11.freefalling.framework.GameServiceListener;
import com.pa_gruppe11.freefalling.framework.VectorSAT;
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
import java.util.HashMap;

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
        if(getIntent().hasExtra(Multiplayer.EXTRA_ROOM))
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

        if(!getIntent().hasExtra(Multiplayer.EXTRA_ROOM))
            initiate();

    }

    public void initiate() {
        thisPlayer = new Player();
        thisPlayer.setCharacter(new Hanz(R.drawable.stickman, 65, 112));

        // change back to calculated x-value
        thisPlayer.getCharacter().setDrawY(
                DataHandler.getInstance().getScreenHeight()/2 - thisPlayer.getCharacter().getHeight()/2);
        if(room != null) {
            participants = room.getParticipants();
            int i = 0;
            if(participants != null) {
                opponents = new Player[participants.size()-1];  //-1 is thisPlayer
                int index = 0;
                for(Participant p : participants) {
                    //TODO: replace - debug purposes
                    if(p.getPlayer() == null ||
                            !p.getPlayer().getPlayerId().equals(Games.Players.getCurrentPlayerId(mGoogleApiClient))) {
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

        //TODO: TESTING ONLY, REMOVE AND PLACE IN SkyStage
        gameMap = new SkyStage();
        testblock = new Block(R.drawable.block, 500, 50);
        testblock.setAngularVelocity((float) Math.PI/2);
        testblock.setX(1200);
        testblock.setY(800);
        testblock.setRotate(true);

        Block testblock2 = new Block(R.drawable.block, 500, 50);
        testblock2.setAngularVelocity((float) -Math.PI/2);
        testblock2.setRotate(true);
        testblock2.setY(400);
        testblock2.setX(200);

        Block testblock3 = new Block(R.drawable.block, 500, 50);
        testblock3.setAngularVelocity((float) Math.PI/2);
        testblock3.setRotate(true);
        testblock3.setY(2500);
        testblock3.setX(200);

        //
        ArrayList<VectorSAT> c1 = Collidable.getCorners(thisPlayer.getCharacter(), 20);  // The rotated corners of collidable1
        ArrayList<VectorSAT> c2 = Collidable.getCorners(testblock, 20);
        ArrayList<VectorSAT> axis = Collidable.getAxis(c1, c2);

        gameMap.addObstacle(testblock);
        gameMap.addObstacle(testblock2);
        gameMap.addObstacle(testblock3);

        gameMap.setThisCharacter(thisPlayer.getCharacter());

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

        thisPlayer.getCharacter().getPreviousPosition().x = thisPlayer.getCharacter().getX();
        thisPlayer.getCharacter().getPreviousPosition().y = thisPlayer.getCharacter().getY();

        if (opponents != null) {
            for (Player opponent : opponents) {
                HashMap<String, Object> mtvList =
                        Collidable.collidesMTV(
                                opponent.getCharacter().getBounds(),
                                thisPlayer.getCharacter().getBounds());
                if((boolean) mtvList.get("boolean")) {
                    VectorSAT mtv = (VectorSAT) mtvList.get("VectorSAT");
                    thisPlayer.getCharacter().setDebugString("Kållesjcøn");
                    thisPlayer.getCharacter().setX(thisPlayer.getCharacter().getX() + mtv.x);
                    thisPlayer.getCharacter().setY(thisPlayer.getCharacter().getY() + mtv.y);
                }
                //if(!updateBasedCommunication)   // refreshes on each message retreived, which is each frame when true
                  //  opponent.getCharacter().update(dt);
            }
        }


        // Update powerups and obstacles
        ArrayList<Obstacle> obstacles = gameMap.getObstacles();
        if (obstacles != null) {
            for (Obstacle o : obstacles) {
                o.setDrawY(o.getY() -
                        gameMap.getY() +
                        DataHandler.getInstance().getScreenHeight()/2 -
                        thisPlayer.getCharacter().getHeight()/2);  // TODO: move somewhere else?
                thisPlayer.getCharacter().setDebugString("" + gameMap.getY());
                HashMap<String, Object> mtvList = Collidable.SATcollide(o, thisPlayer.getCharacter(), dt);
                if ((boolean) mtvList.get("boolean")) {
                    VectorSAT mtv = (VectorSAT) mtvList.get("VectorSAT");
                    thisPlayer.getCharacter().setX(thisPlayer.getCharacter().getX() + mtv.x);
                    thisPlayer.getCharacter().setY(thisPlayer.getCharacter().getY() + mtv.y);

                    // Log.w("GameActivity", "Collision");
                } else {
                    // thisPlayer.getCharacter().setDebugString("(" + thisPlayer.getCharacter().getX() + ", " + thisPlayer.getCharacter().getY());
                }
            }
        }

        ArrayList<PowerUp> powerUps = gameMap.getPowerups();
        if (powerUps != null) {
            for (PowerUp p : powerUps) {
                //if(thisPlayer.getCharacter().collides(p))
                //  thisPlayer.getCharacter().setCollidesWith(p);
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

        thisPlayer.getCharacter().update(dt);           // Update this player
        if (gameMap != null)
            gameMap.update(dt);     // Also updates the corresponding powerups and obstacles of the stage
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
