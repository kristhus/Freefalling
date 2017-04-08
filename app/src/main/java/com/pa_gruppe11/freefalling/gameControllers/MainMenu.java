package com.pa_gruppe11.freefalling.gameControllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;

import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.Singletons.Config;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.ResourceLoader;
import com.pa_gruppe11.freefalling.framework.GridViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skars on 31.03.2017.
 */

public class MainMenu extends GameMenu
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnInvitationReceivedListener,
        RealTimeMessageReceivedListener,
        RoomStatusUpdateListener,
        RoomUpdateListener,
        View.OnClickListener{

    private GoogleApiClient mGoogleApiClient;

    //Status codes ( only requirement is for them to be unique )
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_SELECT_PLAYERS = 10000;
    private static final int RC_WAITING_ROOM = 10002;

    private int roomId = -1;

    private View selector;
    private GridView characterGridView;
    private GridViewAdapter characterAdapter;


    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Config.getInstance().readFile(this);
        setContentView(R.layout.mainmenu);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // TODO: REMOVE AFTER TESTING
        DataHandler.getInstance().setScreenWidth(size.x);
        DataHandler.getInstance().setScreenHeight(size.y);

        ResourceLoader.getInstance().setContext(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
 //               .requestEmail()
  //              .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
//                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
            .build();

        mGoogleApiClient.connect();

        findViewById(R.id.sign_in_button).setOnClickListener(this);

    }

    public void startQuickGame(View view) {
        ResourceLoader.getInstance().getCharacters();
        setContentView(R.layout.lobby);

        /*
        Bundle am = RoomConfig.createAutoMatchCriteria(1, 1, 0);
        RoomConfig.Builder roomConfigBuilder = createDefaultRoom();
        roomConfigBuilder.setAutoMatchCriteria(am);
        RoomConfig roomConfig = roomConfigBuilder.build();

        Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        */
        //go to game screen
    }

    public void onInvitePlayersClicked(View view) {
        Log.w("MainMenu", "invite players");
        Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 3);
        startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    public RoomConfig.Builder createDefaultRoom() {
        return RoomConfig.builder(this)
                .setMessageReceivedListener(this)
                .setRoomStatusUpdateListener(this);
    }


    public void goToAboutMenu(View view){
        goTo(AboutMenu.class);
    }

    public void goToFriendsMenu(View view){
        goTo(FriendsMenu.class);
    }

    public void goToSettings(View view){
        goTo(Settings.class);
    }

    public void goToLobby(View view){
        goTo(Lobby.class);
    }

    public void startGame(View view) {
        //TODO: remove load

        //ResourceLoader.getInstance().loadImage(R.drawable.bg_sky, this);
        //ResourceLoader.getInstance().loadImage(R.drawable.stickman, this);
        //ResourceLoader.getInstance().loadImage(R.drawable.bg_sky, this);
        ResourceLoader.getInstance().manualLoad(this);
        goTo(GameActivity.class);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.w("MainMenu", "Connected successfully");
        Toast.makeText(this, "Connected to Game Services",
                Toast.LENGTH_SHORT).show();
        findViewById(R.id.sign_in_button).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w("MainMenu", "Connection suspended!");
        Toast.makeText(this, "Connection suspended",
                Toast.LENGTH_SHORT).show();
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w("MainMenu", "Failed to connect");
        Toast.makeText(this, "Failed connecting to Game Services",
                Toast.LENGTH_SHORT).show();
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
    }

    @Override
    public void onInvitationReceived(Invitation invitation) {
        Log.w("MainMenu", "Invitation received");

        if(invitation != null) {
            RoomConfig.Builder roomConfigBuilder = createDefaultRoom();
            roomConfigBuilder.setInvitationIdToAccept(invitation.getInvitationId());
            Games.RealTimeMultiplayer.join(mGoogleApiClient, roomConfigBuilder.build());

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onInvitationRemoved(String s) {
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
                Bundle extras = data.getExtras();
                final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);
                for(String s : invitees)
                    Log.w("MainMenu", "Invite " + s);
                //get the criterias for the match
                Bundle criteria = null;
                int minPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
                int maxPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

                Log.w("MainMenu", "minPlayers: " + minPlayers);
                if(minPlayers > 0) {
                    criteria = RoomConfig.createAutoMatchCriteria(minPlayers, maxPlayers, 0);
                }
                RoomConfig.Builder roomConfigBuilder = createDefaultRoom();
                roomConfigBuilder.addPlayersToInvite(invitees);
                if(criteria != null)
                    roomConfigBuilder.setAutoMatchCriteria(criteria);
                RoomConfig roomConfig = roomConfigBuilder.build();

                Log.w("MainMenu", "Created new room: " + (criteria == null ? "null" : "something"));

                Games.RealTimeMultiplayer.create(mGoogleApiClient, roomConfig);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

                // TODO: disable the view, and show loading, until room is created
                showLoading();
                break;
            case RC_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(result.isSuccess()) {
                    Log.w("MainMenu", "Success");
                    mGoogleApiClient.connect();
                }
                else
                    Log.w("MainMenu", "Phail" + result.getStatus());
                break;
            case RC_WAITING_ROOM:
                Log.w("MainMenu", "RC_WAITING_ROOM");
                if(resultCode == Activity.RESULT_OK)
                    startGame(null);
                else if(resultCode == Activity.RESULT_CANCELED) {
                    // TODO: pressed back button or similar. leave room? run in background?
                }
                else if(resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                    //TODO: leave room

                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Log.w("MainMenu", "opening signin");
        mGoogleApiClient.connect();
        /*
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        */
    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
        Log.w("MainMenu", "Received message from " + realTimeMessage.getSenderParticipantId() + " : " + realTimeMessage.getMessageData());
    }

    @Override
    public void onRoomCreated(int i, Room room) {
        Log.w("MainMenu", "Room created");
        Intent intent = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, Integer.MAX_VALUE);
        startActivityForResult(intent, RC_WAITING_ROOM);
        dismissLoading();
        //Intent intent = Games.RealTimeMultiplayer.getSelectOpponentsIntent(mGoogleApiClient, 1, 3);
        //startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        Log.w("MainMenu", "Joined room");
        if(statusCode != GamesStatusCodes.STATUS_OK) {
            Log.w("MainMenu", "Error");
            return;
        }
        Intent intent = Games.RealTimeMultiplayer.getWaitingRoomIntent(mGoogleApiClient, room, Integer.MAX_VALUE);
        startActivityForResult(intent, RC_WAITING_ROOM);
    }

    @Override
    public void onLeftRoom(int i, String s) {
        Log.w("MainMenu", "Left room");
    }

    @Override
    public void onRoomConnected(int i, Room room) {
        Log.w("MainMenu", "onRoomConnected");
    }

    @Override
    public void onRoomConnecting(Room room) {
        Log.w("MainMenu", "onRoomConnecting");
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        Log.w("MainMenu", "onRoomAutoMatching");
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {
        Log.w("MainMenu", "onPeerInvitedToRoom");
    }

    @Override
    public void onPeerDeclined(Room room, List<String> list) {
        Log.w("MainMenu", "onPeerDeclined");
    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {
        Log.w("MainMenu", "onPeerJoined");
    }

    @Override
    public void onPeerLeft(Room room, List<String> list) {
        Log.w("MainMenu", "onPeerLeft");
    }

    @Override
    public void onConnectedToRoom(Room room) {
        Log.w("MainMenu", "onConnectedToRoom");
    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        Log.w("MainMenu", "onDisconnectedFromRoom");
    }

    @Override
    public void onPeersConnected(Room room, List<String> list) {
        Log.w("MainMenu", "onPeersConnected");
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> list) {
        Log.w("MainMenu", "onPeersDisconnected");
    }

    @Override
    public void onP2PConnected(String s) {
        Log.w("MainMenu", "onP2PConnected");
    }

    @Override
    public void onP2PDisconnected(String s) {
        Log.w("MainMenu", "onP2PDisconnected");
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

    public void openCharacterSelection(View view) {

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(DataHandler.getInstance().getScreenWidth(), DataHandler.getInstance().getScreenHeight());
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.lobbyLayout);
        selector = getLayoutInflater().inflate(R.layout.character_selection, null);

        //addContentView(selector, params);

        characterGridView = (GridView) selector.findViewById(R.id.gridView);
        if(characterGridView == null)
            Log.w("MainMenu", "characterGridView == null");
        characterAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, ResourceLoader.getInstance().getCharacters());
        characterGridView.setAdapter(characterAdapter);


        addContentView(selector, params);

        //layout.addView(selector);
    }

    public void selectedCharacter(View view) {
        Log.w("MainMenu", "Selected some character");
        // Close, then recycle
    }

    public void closeSelector(View view) {
        ((ViewGroup) selector.getParent()).removeView(selector);
    }

}

