package com.pa_gruppe11.freefalling.gameControllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;

import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.pa_gruppe11.freefalling.Models.GameMessage;
import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.Singletons.Config;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.framework.GameServiceListener;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;
import com.pa_gruppe11.freefalling.framework.GridViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by skars on 31.03.2017.
 */

public class MainMenu extends GameMenu implements View.OnClickListener{

    // Character selection
    private View selector;
    private GridView characterGridView;
    private GridViewAdapter characterAdapter;
    private int selectedCharacterId = -1;
    private HashMap<String, Integer> opponentSelection = new HashMap<>(); // participantId and the characterId

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Config.getInstance().readFile(this);
        setContentView(R.layout.mainmenu);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        DataHandler.getInstance().setScreenWidth(size.x);
        DataHandler.getInstance().setScreenHeight(size.y);

        ResourceLoader.getInstance().setContext(this);

        findViewById(R.id.sign_in_button).setOnClickListener(this);

    }

    /**
     * Set the activities view to lobby
     * @param view
     */
    public void inflateLobby(View view) {
        ResourceLoader.getInstance().getCharacters();   // loads characters to be used in lobby
        setContentView(R.layout.lobby);
    }
    /**
     * Set the activities view to mainmenu
     * @param view
     */
    public void inflateMenu(View view) {
        setContentView(R.layout.mainmenu);
    }

    /**
     * Start a an automated search for other players with similar criterias
     * @param view
     */
    public void startQuickGame(View view) {
        Log.w("MainMenu", "StartQuickGame");
        Bundle am = RoomConfig.createAutoMatchCriteria(1, 3, 0);    // minimum 1 opponent, maximum 3, no role dependency
        RoomConfig.Builder roomConfigBuilder = createDefaultRoom();
        roomConfigBuilder.setAutoMatchCriteria(am);
        RoomConfig roomConfig = roomConfigBuilder.build();

        Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        showLoading();

    }

    /**
     * Open activity for player selection
     * @param view
     */
    public void onInvitePlayersClicked(View view) {
        Log.w("MainMenu", "invite players");
        Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 3);
        Log.w("MainMenu", "Intent serial: " + intent);
        Log.w("MainMenu", "Before activity: " + intent.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, -1));
        startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    /**
     * Create a default room with GameServiceListener
     * @return
     */
    public RoomConfig.Builder createDefaultRoom() {
        return RoomConfig.builder(serviceListener)
                .setMessageReceivedListener(serviceListener)
                .setRoomStatusUpdateListener(serviceListener);
    }


    public void goToAboutMenu(View view){
        goTo(AboutMenu.class);
    }

    public void goToSettings(View view){
        goTo(Settings.class);
    }

    public void startGame(View view) {
        //TODO: remove load

        //ResourceLoader.getInstance().loadImage(R.drawable.bg_sky, this);
        //ResourceLoader.getInstance().loadImage(R.drawable.stickman, this);
        //ResourceLoader.getInstance().loadImage(R.drawable.bg_sky, this);
        if(serviceListener != null)
            serviceListener.remove(this);
        else
            Log.w("MainMenu", "BUG?!?!?!?!?");
        if(!mGoogleApiClient.isConnected()) {
            goTo(GameActivity.class);
            return;
        }

        ResourceLoader.getInstance().recycleMenuResources();    // Recycle resources used in menus
        Intent intent = new Intent(this, GameActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(Multiplayer.EXTRA_ROOM, room);
        intent.putExtra("role", selectedCharacterId);
        intent.putExtra("CURRENT_PLAYER_ID", Games.Players.getCurrentPlayer(mGoogleApiClient));
        intent.putExtra("opponentSelections", opponentSelection);
        startActivity(intent);
        finish();

      //  goTo(GameActivity.class);
    }

    @Override
    public void connected() {
        super.connected();
        Log.w("MainMenu", "Connected successfully");
       // Toast.makeText(this, "Connected to Game Services",Toast.LENGTH_SHORT).show();
        findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
    }

    @Override
    public void connectionSuspended(int i) {
        Log.w("MainMenu", "Connection suspended!");
        Toast.makeText(this, "Connection suspended",
                Toast.LENGTH_SHORT).show();
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
    }

    @Override
    public void connectionFailed(@NonNull ConnectionResult connectionResult) {
        super.connectionFailed(connectionResult);
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
    }

    @Override
    public void invitationReceived(Invitation invitation) {
        Log.w("MainMenu", "Invitation received");

        if(invitation != null) {
            RoomConfig.Builder roomConfigBuilder = createDefaultRoom();
            roomConfigBuilder.setInvitationIdToAccept(invitation.getInvitationId());
            Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void invitationRemoved(String s) {
        Log.w("MainMenu", "Removed invitation");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.w("MainMenu", "onActivityResult");
        switch(requestCode) {
            case RC_SELECT_PLAYERS: // Create room based on invites
                if(resultCode != Activity.RESULT_OK) {
                    Log.w("MainMenu", "Canceled by user");
                    return;
                }

                final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
                for(String s : invitees)
                    Log.w("MainMenu", "Invite " + s);
                //get the criterias for the match
                Bundle criteria = null;
                int minPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, -1);
                int maxPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, -1);

                Log.w("MainMenu", "minPlayers: " + minPlayers);
                if(minPlayers > 0) {
                    criteria = RoomConfig.createAutoMatchCriteria(1, 3, 0);
                }
                RoomConfig.Builder roomConfigBuilder = createDefaultRoom();
                roomConfigBuilder.addPlayersToInvite(invitees);
                if(criteria != null)
                    roomConfigBuilder.setAutoMatchCriteria(criteria);
                RoomConfig roomConfig = roomConfigBuilder.build();

                Log.w("MainMenu", "Created new room: " + (criteria == null ? "null" : "something"));

                Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                showLoading();
                break;
            case RC_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(result != null && result.isSuccess()) {
                    Log.w("MainMenu", "Success");
                    mGoogleApiClient.connect();
                }
                else
                    Log.w("MainMenu", "RC_SIGN_IN failed with status " + (result!=null ? result.getStatus() : "result null"));
                break;
            case RC_WAITING_ROOM:
                Log.w("MainMenu", "RC_WAITING_ROOM");
                if(resultCode == Activity.RESULT_OK) {
                    //waitForPlayers();
                    startGame(null);
                } else if(resultCode == Activity.RESULT_CANCELED) {
                    // TODO: pressed back button or similar. leave room? run in background?
                }
                else if(resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    //TODO: leave room
                    Games.RealTimeMultiplayer.leave(mGoogleApiClient, serviceListener, room.getRoomId());
                }
                break;
        }
    }

    public void waitForPlayers() {
        try {
            showLoading();
            Thread.sleep(10000);
            dismissLoading();
        } catch (InterruptedException e) {}
    }


    @Override
    public void onClick(View v) {
        Log.w("MainMenu", "opening signin");
        mSignInClicked = true;
        mGoogleApiClient.connect();
        /*
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        */
    }

    @Override
    public void messageReceived(RealTimeMessage realTimeMessage) {
        Log.w("MainMenu", "Message received");
        byte[] bytes = realTimeMessage.getMessageData();
        GameMessage messageReceived = GameMessage.fromBytes(bytes);

        if(messageReceived != null) {
            switch(messageReceived.getType()) {
                case GameMessage.CHARACTER_SELECTED:
                    opponentSelection.put(realTimeMessage.getSenderParticipantId(), messageReceived.getCharacterSelected());
                    break;
            }
        }
    }

    @Override
    public void roomCreated(int i, Room room) {
        this.room = room;
        Log.w("MainMenu", "Room created");
        Intent intent = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, Integer.MAX_VALUE);
        startActivityForResult(intent, RC_WAITING_ROOM);
        dismissLoading();
        //Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 3);
        //startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    @Override
    public void joinedRoom(int statusCode, Room room) {
        this.room = room;
        Log.w("MainMenu", "Joined room");
        if(statusCode != GamesStatusCodes.STATUS_OK) {
            Log.w("MainMenu", "Error");
            return;
        }
        if(room.getParticipants().size() > 1) { // If someone in the room, tell them of your selection
            GameMessage selectionMessage = new GameMessage(GameMessage.CHARACTER_SELECTED);
            selectionMessage.setCharacterSelected(selectedCharacterId);
            for(Participant p : room.getParticipants()) {
                Games.RealTimeMultiplayer.sendUnreliableMessage(mGoogleApiClient, selectionMessage.toBytes(), room.getRoomId(), p.getParticipantId());

            }
        }
        Intent intent = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, Integer.MAX_VALUE);
        startActivityForResult(intent, RC_WAITING_ROOM);
    }

    @Override
    public void leftRoom(int i, String s) {
        Log.w("MainMenu", "Left room");
    }

    @Override
    public void roomConnected(int i, Room room) {
        super.roomConnected(i, room);
        Log.w("MainMenu", "onRoomConnected: #participants: " + room.getParticipants().size());
    }

    @Override
    public void roomConnecting(Room room) {
        Log.w("MainMenu", "onRoomConnecting");
    }

    @Override
    public void peerJoined(Room room, List<String> participantIds) {
        Log.w("MainMenu", "peerJoined");
        if(participantIds.size() != 0) {
            Log.w("MainMenu", "sending message about character selection");
            GameMessage selectionMessage = new GameMessage(GameMessage.CHARACTER_SELECTED);
            selectionMessage.setCharacterSelected(selectedCharacterId);
            Games.RealTimeMultiplayer.sendUnreliableMessageToOthers(mGoogleApiClient, selectionMessage.toBytes(), room.getRoomId());
        }
    }


    public void showLoading() {
        findViewById(R.id.loadLayout).setVisibility(View.VISIBLE);
        /*
        if(loader == null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DataHandler.screenWidth, DataHandler.screenHeight);
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.lobbyLayout);
            loader = getLayoutInflater().inflate(R.layout.loader, null);
            layout.addView(loader);
        } loader.setVisibility(View.VISIBLE);
        */

    }
    public void dismissLoading() {
        findViewById(R.id.loadLayout).setVisibility(View.GONE);
        /*
        if(loader != null) {
            loader.setVisibility(View.GONE);
        }
        */
    }

    /**
     * Inflates an xml-layout with custom ArrayAdapter with characters as defined in strings.xml strings-array
     *
     */
    public void openCharacterSelection(View view) {
        Log.w("MainMenu", "openCharacterSelection()");
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DataHandler.getInstance().getScreenWidth(), DataHandler.getInstance().getScreenHeight());
        selector = getLayoutInflater().inflate(R.layout.character_selection, null);

        characterGridView = (GridView) selector.findViewById(R.id.gridView);
        if(characterGridView == null)
            Log.w("MainMenu", "characterGridView == null");
        characterAdapter = new GridViewAdapter(this,
                R.layout.grid_item_layout,
                ResourceLoader.getInstance().getCharacters());
        characterGridView.setAdapter(characterAdapter);
        characterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int identifier = characterAdapter.getImageItem(position).getIdentifier();
                String name = characterAdapter.getImageItem(position).getTitle();
                Toast.makeText(getApplicationContext(),
                        "Item Clicked: " + name + " " + identifier, Toast.LENGTH_SHORT).show();
                selectedCharacterId = identifier;
                closeSelector(null);
            }
        });


        addContentView(selector, params);

    }

    public void closeSelector(View view) {
        ((ViewGroup) selector.getParent()).removeView(selector);
    }


}

