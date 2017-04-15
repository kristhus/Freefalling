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
import java.util.List;

/**
 * Created by Kristian on 31/03/2017.
 */
public class GameActivity extends GameMenu {

    // MODELS
    private HashMap<String, Player> opponents;
    private GameMap gameMap; //
    private Player thisPlayer;

    private ArrayList<Participant> participants;

    // TODO: REMOVE AFTER TESTING

    // Controllers
    private PlayerController controller;

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

        if(!getIntent().hasExtra(Multiplayer.EXTRA_ROOM))
            initiate();

    }

    public void initiate() {
        thisPlayer = new Player();
        thisPlayer.setCharacter(new Hanz(R.drawable.stickman, 65, 112));
        thisPlayer.getCharacter().setThisCharacter(true);


        // change back to calculated x-value
        thisPlayer.getCharacter().setDrawY(
                DataHandler.getInstance().getScreenHeight()/2 - thisPlayer.getCharacter().getHeight()/2);
        if(room != null) {
            participants = room.getParticipants();
            int i = 0;
            if(participants != null) {
                opponents = new HashMap<>();
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

                        otherPlayer.getCharacter().setDrawY(
                                thisPlayer.getCharacter().getDrawY() +
                                (otherPlayer.getCharacter().getY() - thisPlayer.getCharacter().getY())
                        );

                        opponents.put(p.getParticipantId(), otherPlayer);  //-1 is thisPlayer
//                        opponents[i] = otherPlayer;
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
            for (String key : opponents.keySet()) {
                Player opponent = opponents.get(key);
                opponent.getCharacter().setDrawY(
                        thisPlayer.getCharacter().getDrawY() +
                                (opponent.getCharacter().getY() - thisPlayer.getCharacter().getY())
                );
                HashMap<String, Object> mtvList =
                        opponent.getCharacter().collidesMTV(            // TODO: possible memory leak
                                opponent.getCharacter().getBounds(),
                                thisPlayer.getCharacter().getBounds());
                if((boolean) mtvList.get("boolean")) {
                    VectorSAT mtv = (VectorSAT) mtvList.get("VectorSAT");
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
                        (DataHandler.getInstance().getScreenHeight()/2 -
                        thisPlayer.getCharacter().getHeight()/2));  // TODO: move somewhere else?
                /*
                o.setDrawY(
                        thisPlayer.getCharacter().getDrawY() +
                        (o.getY() - thisPlayer.getCharacter().getY()));
                 */
                if(!GameMap.outsideScreen(o)) {// don't check collision if guaranteed to not collide
                    VectorSAT mtv = o.SATcollide(o, thisPlayer.getCharacter(), dt);       // TODO: Massive memory strain
                    if (mtv != null) {
                        if (o.isLethal()) {
                            // TODO: death-animation?
                            float res = gameMap.getClosestRespawnPoint(thisPlayer.getCharacter().getY());
                            Log.w("GameActivity", "res: " + res);
                            thisPlayer.getCharacter().setY(res);
                        }
                        thisPlayer.getCharacter().setX(thisPlayer.getCharacter().getX() + mtv.x);
                        thisPlayer.getCharacter().setY(thisPlayer.getCharacter().getY() + mtv.y);

                        // Log.w("GameActivity", "Collision");
                    } else {
                        // thisPlayer.getCharacter().setDebugString("(" + thisPlayer.getCharacter().getX() + ", " + thisPlayer.getCharacter().getY());
                    }
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

    public HashMap<String, Player> getOpponents() {
        return opponents;
    }


    public void notifyReady() {
        gameInProgress = true;
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
        gameInProgress = false;
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
            for(String participantId : opponents.keySet()) {
                Player p = opponents.get(participantId);
                if (p.getParticipantId().equals(senderId)) {
                    p.getCharacter().setValues(messageReceived);
                    break;
               }
            }
        }
        else
            Log.w("GameActivity","Got a message, but data corrupted");
    }

    public void peerLeft(Room room, List<String> participantIds) {
        for(String pId : participantIds) {

        }
    }

}
