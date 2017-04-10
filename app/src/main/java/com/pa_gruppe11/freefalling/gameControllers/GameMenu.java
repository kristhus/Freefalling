package com.pa_gruppe11.freefalling.gameControllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.pa_gruppe11.freefalling.R;
import com.pa_gruppe11.freefalling.Singletons.DataHandler;
import com.pa_gruppe11.freefalling.Singletons.GameThread;
import com.pa_gruppe11.freefalling.framework.BaseGameUtils;

import java.util.List;

/**
 * Created by skars on 31.03.2017.
 */

public class GameMenu extends Activity {


    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }


    public void goTo(Class javaClass) {
        Intent intent = new Intent(this, javaClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        Log.w("GameMenu", "goTo " + javaClass.getCanonicalName());
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        Class thisClass = this.getClass();
        if(thisClass == MainMenu.class) {
            // Quit ?Bitmap.createBitmap
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else if(thisClass == GameActivity.class) {
            // set up prompt that you are leaving the game
            alertPrompt();
        } else {
            goTo(MainMenu.class);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(this.getClass() == GameActivity.class)
            GameThread.getInstance().setRunning(false);
        Log.w("GameMenu", "Paused");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.getClass() == GameActivity.class)
            GameThread.getInstance().setRunning(true);
        Log.w("GameMenu", "Resumed");
    }

    private void alertPrompt() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Quit game in progress?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        GameThread.getInstance().stop_gameThread();
                        Games.RealTimeMultiplayer.leave(mGoogleApiClient, DataHandler.getMessageListener(), room.getRoomId());
                        //Log.w("GM", "Suspend: " + GameThread.getInstance().isSuspended());
                        goTo(MainMenu.class);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void goToMainMenu(View view) {
        if(getClass() != MainMenu.class)
            goTo(MainMenu.class);
    }

    /**
     * Possible for views' onclick  to override drawable components under.
     * Examples of usage: loader-layout.
     *
     * @param view
     */
    public void dummyMethod(View view) {

    }

    /*
    The following methods are used to handle GameService Events
    Documentation of methods as retrieved from Google APIs for Android, References

    Status codes:
        STATUS_OK if data was successfully loaded and is up-to-date.
        STATUS_CLIENT_RECONNECT_REQUIRED if the client needs to reconnect to the service to access this data.
        STATUS_REAL_TIME_CONNECTION_FAILED if the client failed to connect to the network
        STATUS_MULTIPLAYER_DISABLED if the game does not support multiplayer.
        STATUS_INTERNAL_ERROR if an unexpected error occurred in the service.
     */


    // Google Game Services -related fields
    protected Room room;
    protected boolean mConnected = false;
    protected boolean mIsOwner = false;
    protected boolean mDisonnected = false;
    protected boolean mSignInClicked = false;
    protected boolean mResolvingConnectionFailure = false;

    protected boolean mFinalizeLogin = false;
    protected boolean initLogin = true;
    protected boolean gamesAuthorized = false;

    protected GoogleApiClient mGoogleApiClient;

    //Status codes ( only requirement is for them to be unique )
    protected static final int RC_SIGN_IN = 9001;
    protected static final int RC_SELECT_PLAYERS = 10000;
    protected static final int RC_WAITING_ROOM = 10002;

    /**
     * Called to notify the client that a reliable or unreliable message was received for a room.
     * @param realTimeMessage The message that was received.
     */
    public void messageReceived(RealTimeMessage realTimeMessage) {

    }

    /**
     * After calling connect(), this method will be invoked asynchronously when the connect request has successfully completed.
     */
    public void connected() {
        mConnected = true;
        mDisonnected = false;
    }

    /**
     * Called when the client is temporarily in a disconnected state.
     *
     * int CAUSE_NETWORK_LOST A suspension cause informing you that a peer device connection was lost.
     * int CAUSE_SERVICE_DISCONNECTED A suspension cause informing that the service has been killed.
     *
     * @param cause
     */
    public void connectionSuspended(int cause) {
        mConnected = false;
        mDisonnected = true;
    }

    /**
     * Called when the client can not connect to Game Services.
     * @param connectionResult
     */
    public void connectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // Solving error through BaseGameUtils
            return;
        }
        if(mSignInClicked) {
            mResolvingConnectionFailure = true;
            mSignInClicked = false;
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getString(R.string.signin_other_error)))  {
                mResolvingConnectionFailure = false;
            }

        }
        mConnected = false;
/*
        Log.w("GameMenu", "Error: " + connectionResult.getErrorCode());
            Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
            Log.w("GameMenu", "Connection failed: " + connectionResult.getErrorMessage());
            mConnected = false;
            */
    }

    /**
     * Callback invoked when a new invitation is received.
     * This allows an app to respond to the invitation as appropriate.
     * If the app receives this callback,
     *      the system will not display a notification for this invitation.
     * @param invitation The invitation that was received
     */
    public void invitationReceived(Invitation invitation) {

    }

    /**
     * Callback invoked when a previously received invitation has been removed from the local device.
     * For example, this might occur if the inviting player leaves the match.
     * @param invitationId The ID of the invitation that was removed.
     */
    public void invitationRemoved(String invitationId) {
    }

    /**
     * Called when one or more participants have joined the room and have started the process of establishing peer connections.
     * @param room
     */
    public void roomConnecting(Room room) {
    }

    /**
     * Called when the server has started the process of auto-matching.
     * @param room
     */
    public void roomAutoMatching(Room room) {
    }

    /**
     * Called when one or more peers are invited to a room.
     * @param room
     * @param participantIds
     */
    public void peerInvitedToRoom(Room room, List<String> participantIds) {
    }

    /**
     * Called when one or more peers decline the invitation to a room.
     * @param room
     * @param participantIds
     */
    public void peerDeclined(Room room, List<String> participantIds) {
    }

    /**
     * Called when one or more peer participants join a room.
     * @param room
     * @param participantIds
     */
    public void peerJoined(Room room, List<String> participantIds) {
    }

    /**
     * Called when one or more peer participant leave a room.
     * @param room
     * @param participantIds
     */
    public void peerLeft(Room room, List<String> participantIds) {
    }

    /**
     * Called when the client is connected to the connected set in a room.
     * @param room
     */
    public void connectedToRoom(Room room) {
        mIsOwner = false;
    }

    /**
     * Called when the client is disconnected from the connected set in a room.
     * @param room
     */
    public void disconnectedFromRoom(Room room) {
    }

    /**
     * Called when one or more peer participants are connected to a room.
     * @param room
     * @param participantId
     */
    public void peersConnected(Room room, List<String> participantId) {
    }

    /**
     * Called when one or more peer participants are disconnected from a room.
     * @param room
     * @param participantId
     */
    public void peersDisconnected(Room room, List<String> participantId) {
    }

    /**
     * Called when the client is successfully connected to a peer participant.
     * @param participantId
     */
    public void P2PConnected(String participantId) {
    }

    /**
     * Called when client gets disconnected from a peer participant.
     * @param participantId
     */
    public void P2PDisonnected(String participantId) {
    }

    /**
     * Called when the client attempts to create a real-time room.
     * @param statusCode
     * @param room
     */
    public void roomCreated(int statusCode, Room room) {
        mIsOwner = true;
    }

    /**
     * Called when the client attempts to join a real-time room.
     * @param statusCode
     * @param room
     */
    public void joinedRoom(int statusCode, Room room) {
        mIsOwner = false;
    }

    /**
     * Called when the client attempts to leaves the real-time room.
     * @param statusCode
     * @param roomId
     */
    public void leftRoom(int statusCode, String roomId) {
        mIsOwner = false;
        room = null;
    }

    /**
     * Called when all the participants in a real-time room are fully connected.
     * @param statusCode
     * @param room
     */
    public void roomConnected(int statusCode, Room room) {
        this.room = room;
        // Start game
    }
}
