package com.pa_gruppe11.freefalling.gameControllers;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.pa_gruppe11.freefalling.Models.GameMap;
import com.pa_gruppe11.freefalling.Models.GameMessage;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;
import com.pa_gruppe11.freefalling.framework.VectorSAT;
import com.pa_gruppe11.freefalling.implementations.models.Goat;
import com.pa_gruppe11.freefalling.implementations.models.Gruber;
import com.pa_gruppe11.freefalling.implementations.models.Hanz;
import com.pa_gruppe11.freefalling.Models.Obstacle;
import com.pa_gruppe11.freefalling.R;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.GameThread;
import com.pa_gruppe11.freefalling.implementations.models.SkyStage;
import com.pa_gruppe11.freefalling.View.GameView;
import com.pa_gruppe11.freefalling.Models.Player;
import com.pa_gruppe11.freefalling.Models.Character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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

    private boolean thisPlayerReachedGoal = false;
    private boolean otherPlayerReachedGoal = false;
    private ArrayList<Player> finishing_placements;

    // Statistics
    private long startTime;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        if(getIntent().hasExtra(Multiplayer.EXTRA_ROOM))
            room = (Room) getIntent().getExtras().get(Multiplayer.EXTRA_ROOM);

        if(!getIntent().hasExtra(Multiplayer.EXTRA_ROOM))
            initiate();
    }

    public void initiate() {
        ResourceLoader.getInstance().manualLoad(this);
        finishing_placements = new ArrayList<Player>();
        thisPlayer = new Player();

        if(room != null) {  // if multiplayer
            participants = room.getParticipants();
            int selectedCharacterId = (Integer) getIntent().getExtras().get("role");
            GameMessage selectionMessage = new GameMessage(GameMessage.CHARACTER_SELECTED);
            selectionMessage.setCharacterSelected(selectedCharacterId);
            for (Participant p : participants) {
                if (!p.getParticipantId().equals(Games.Players.getCurrentPlayerId(mGoogleApiClient))) {
                    Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, selectionMessage.toBytes(), room.getRoomId(), p.getParticipantId());   // sendUnreliableMessageToOthers
                }
            }
            int i = 0;
            if(participants != null) {
                opponents = new HashMap<>();
                int index = 0;
                HashMap<String, Integer> opponentSelections =
                        (HashMap<String, Integer>) getIntent().getExtras().get("opponentSelections");
                for(Participant p : participants) {
                    //TODO: replace - debug purposes
                    if(p.getPlayer() == null ||
                            !p.getPlayer().getPlayerId().equals(Games.Players.getCurrentPlayerId(mGoogleApiClient))) {
                        int opponentId =  (opponentSelections.keySet().size() > 0 ? opponentSelections.get(p.getParticipantId()) : -1);
                        Character c = createSelectedCharacter(opponentId);
                        c.setX(200*(index+1));
                        c.setY(200);
                        Player otherPlayer = new Player(p.getParticipantId(), i, null, c);
                        String displayName = p.getDisplayName();
                        otherPlayer.setDisplayName(displayName);


                        opponents.put(p.getParticipantId(), otherPlayer);  //-1 is thisPlayer
                        i++;
                    }else {
                        thisPlayer.setCharacter(createSelectedCharacter(selectedCharacterId));
                        thisPlayer.getCharacter().setX(200*(index+1));
                        thisPlayer.getCharacter().setY(200);
                        thisPlayer.setParticipantId(p.getParticipantId());
                        thisPlayer.setDisplayName(p.getDisplayName());
                        // change back to calculated x-value
                        thisPlayer.getCharacter().setDrawY(
                                DataHandler.getInstance().getScreenHeight()/2 - thisPlayer.getCharacter().getHeight()/2);
                    }
                }
                for(String s : opponents.keySet()) {
                    opponents.get(s).getCharacter().setDrawY(
                            thisPlayer.getCharacter().getDrawY() +
                                    (opponents.get(s).getCharacter().getY() - thisPlayer.getCharacter().getY())
                    );
                }
            }
        }else { // If room == null, then it is a practice game, and no opponents
            thisPlayer.setCharacter(new Hanz(R.drawable.roger, 65, 112));
            // change back to calculated x-value
            thisPlayer.getCharacter().setDrawY(
                    DataHandler.getInstance().getScreenHeight()/2 - thisPlayer.getCharacter().getHeight()/2);
            thisPlayer.getCharacter().setThisCharacter(true);
        }


        GameThread.getInstance().setActivity(this);

        gameMessage = new GameMessage(GameMessage.GAME_POSITION);

        //TODO: TESTING ONLY, REMOVE AND PLACE IN SkyStage
        gameMap = new SkyStage();

        gameMap.setThisCharacter(thisPlayer.getCharacter());

        GameView gameView = new GameView(this);
        setContentView(gameView);
        GameThread.getInstance().setView(gameView);

        controller = new PlayerController(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                DataHandler.getInstance().getScreenWidth(),
                DataHandler.getInstance().getScreenHeight());
        addContentView(controller, params);

        notifyReady();
    }

    private Character createSelectedCharacter(int selectedCharacterId) {
        switch(selectedCharacterId) {
            case R.drawable.hanz:
                return new Hanz(R.drawable.stickman, 65, 112);
            case R.drawable.gruber:
                return new Gruber(R.drawable.grubermann, 65, 112);
            case R.drawable.goat:
                return new Goat(R.drawable.goat, 125, 133);
        }
        return new Hanz(R.drawable.roger, 65, 112);  // Defaults to Hanz
    }

    public void update(long dt) {

        // Check if player has reached the goal, and run finishGame()
        if(thisPlayer.getCharacter().getY() >= gameMap.getEndY()) {
            finishGame();
        }
    //    if(otherPlayerReachedGoal)
    //        openPostMatch();

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
                            thisPlayer.incrementDeaths();

                            if (thisPlayer.getCharacter() instanceof Goat)
                                ResourceLoader.getInstance().getAudioList().get(R.raw.goat).play();

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

        /*
        ArrayList<PowerUp> powerUps = gameMap.getPowerups();    // Powerups not implemented
        if (powerUps != null) {
            for (PowerUp p : powerUps) {

            }
        }
        */


        // SEND GameMessage
        messageTiming+=dt;
        if(messageTiming > messageInterval || updateBasedCommunication) {   // TODO: remove, sending only once a second to not overflow monitor
            messageTiming -= messageInterval;
            if(mGoogleApiClient.isConnected() && participants != null) {
                gameMessage.setCharacterValues(thisPlayer.getCharacter());  // Prepare new values
//                byte[] message = ("Other person = (" + thisPlayer.getCharacter().getX() + ", " + thisPlayer.getCharacter().getY() + ")").getBytes();
                for (Participant p : participants) {
                    if (!p.getParticipantId().equals(Games.Players.getCurrentPlayerId(mGoogleApiClient))) {
                        Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, gameMessage.toBytes(), room.getRoomId(), p.getParticipantId());   // sendUnreliableMessageToOthers
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
        startTime = System.currentTimeMillis();
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


    /**
     * Called when thisPlayer has reached the goal line
     */
    public void finishGame() {
        //Switch View to postMatchView
        gameInProgress = false;
        thisPlayerReachedGoal = true;
        thisPlayer.setElapsedTime(System.currentTimeMillis() - startTime);
        finishing_placements.add(thisPlayer);
        if(mGoogleApiClient.isConnected() && participants != null) {
            GameMessage notifyFinish = new GameMessage(GameMessage.FINISHED);
            notifyFinish.setDeathCounter(thisPlayer.getDeathCounter());
            notifyFinish.setElapsedTime(System.currentTimeMillis() - startTime);
            for (Participant p : participants) {
                if (!p.getParticipantId().equals(Games.Players.getCurrentPlayerId(mGoogleApiClient))) {
                    Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, notifyFinish.toBytes(), room.getRoomId(), p.getParticipantId());
                }
            }
        }
        // Might not want to do this
        // GO to postmatchview
        openPostMatch();
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
            Player sender = null;
            String senderId = realTimeMessage.getSenderParticipantId();
            for (String participantId : opponents.keySet()) {
                Player p = opponents.get(participantId);
                if (p.getParticipantId().equals(senderId)) {
                    sender = p;
                    break;
                }
            }
            switch(messageReceived.getType()) {
                case GameMessage.GAME_POSITION:
                    sender.getCharacter().setValues(messageReceived);
                    break;
                case GameMessage.FINISHED:
                    if(!finishing_placements.contains(sender)) {
                        finishing_placements.add(sender);
                        sender.setDeathCounter(messageReceived.getDeathCounter());
                        sender.setElapsedTime(messageReceived.getElapsedTime());
                    }
                    openPostMatch();
                    break;
                case GameMessage.CHARACTER_SELECTED:
                    Log.w("GameActivity", "character_selected");
                    Character c =createSelectedCharacter(messageReceived.getCharacterSelected());
                    c.setX(sender.getCharacter().getX());
                    c.setY(sender.getCharacter().getY());
                    sender.setCharacter(c);
                    break;
            }
        }
        else
            Log.w("GameActivity","Got a message, but data corrupted");
    }

    /**
     * Inflate a postmatchview on top of the current view and suspend the gamethread
     */
    public void openPostMatch() {
        GameThread.getInstance().setSuspended(true);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DataHandler.getInstance().getScreenWidth(), DataHandler.getInstance().getScreenHeight());
                addContentView(getLayoutInflater().inflate(R.layout.post_match_view, null),  params);
                TableLayout tableLayout = (TableLayout) findViewById(R.id.ranking_table);

                for(int i = 1; i < finishing_placements.size()+1; i++) {
                    Player p = finishing_placements.get(i-1);
                    TableRow row = (TableRow) getLayoutInflater().inflate(R.layout.table_item, null);
                    ((TextView) row.findViewById(R.id.placement)).setText("" + i);
                    ((TextView) row.findViewById(R.id.playername)).setText(p.getDisplayName());
                    ((TextView) row.findViewById(R.id.deathcounter)).setText("" + p.getDeathCounter());
                    ((TextView) row.findViewById(R.id.timeused)).setText("" + milliToTime(p.getElapsedTime()));
                    tableLayout.addView(row);
                }

            }
        });
    }

    public String milliToTime(long elapsedTime) {
        String retVal = "";
        retVal+= TimeUnit.MILLISECONDS.toMinutes(elapsedTime)+":";
        retVal+= TimeUnit.MILLISECONDS.toSeconds(elapsedTime)%60;
        Log.w("GameActivity", "" + elapsedTime);
        Log.w("GameActivity", "Elapsed time: " + retVal);
        return retVal;

    }


}
