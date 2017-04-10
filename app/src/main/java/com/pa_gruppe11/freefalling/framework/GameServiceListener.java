package com.pa_gruppe11.freefalling.framework;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.pa_gruppe11.freefalling.gameControllers.GameMenu;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Kristian on 09/04/2017.
 * Custom sort of event / listener system, where GameMenu has a set of premade methods which can be overridden
 */

public class GameServiceListener implements RealTimeMessageReceivedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnInvitationReceivedListener,
        RoomStatusUpdateListener,
        RoomUpdateListener{

    private GameMenu notify;

    private HashMap<String, GameMenu> listeners;    // Notify everyone


    public GameServiceListener() {
        listeners = new HashMap<>();
    }

    public void setNotify(GameMenu notify) {
        this.notify = notify;
    }


    public void addListener(GameMenu menu) {
        listeners.put(menu.toString(), menu);
    }
    public void remove(GameMenu menu) {
        listeners.remove(menu.toString());
    }


    // All methods below are of the listener type
    //
    //
    //
    //
    //

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
        Log.w("GameServiceListener", "Got a message, ding ding");
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.messageReceived(realTimeMessage);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.connected();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.connectionSuspended(i);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.connectionFailed(connectionResult);
            }
        }
    }

    @Override
    public void onInvitationReceived(Invitation invitation) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.invitationReceived(invitation);
            }
        }
    }

    @Override
    public void onInvitationRemoved(String s) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.invitationRemoved(s);
            }
        }
    }

    @Override
    public void onRoomConnecting(Room room) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.roomConnecting(room);
            }
        }
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.roomAutoMatching(room);
            }
        }
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.peerInvitedToRoom(room, list);
            }
        }
    }

    @Override
    public void onPeerDeclined(Room room, List<String> list) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.peerDeclined(room, list);
            }
        }
    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.peerJoined(room, list);
            }
        }
    }

    @Override
    public void onPeerLeft(Room room, List<String> list) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.peerLeft(room, list);
            }
        }
    }

    @Override
    public void onConnectedToRoom(Room room) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.connectedToRoom(room);
            }
        }
    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.disconnectedFromRoom(room);
            }
        }
    }

    @Override
    public void onPeersConnected(Room room, List<String> list) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.peersConnected(room, list);
            }
        }
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> list) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.peersDisconnected(room, list);
            }
        }
    }

    @Override
    public void onP2PConnected(String s) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.P2PConnected(s);
            }
        }
    }

    @Override
    public void onP2PDisconnected(String s) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.P2PDisonnected(s);
            }
        }
    }

    @Override
    public void onRoomCreated(int i, Room room) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.roomCreated(i, room);
            }
        }
    }

    @Override
    public void onJoinedRoom(int i, Room room) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.joinedRoom(i, room);
            }
        }
    }

    @Override
    public void onLeftRoom(int i, String s) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.leftRoom(i, s);
            }
        }
    }

    @Override
    public void onRoomConnected(int i, Room room) {
        if(listeners != null && listeners.keySet().size() > 0) {
            for(String key : listeners.keySet()) {
                GameMenu m = listeners.get(key);
                m.roomConnected(i, room);
            }
        }
    }
}
