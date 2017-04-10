package com.pa_gruppe11.freefalling.Models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Kristian on 10/04/2017.
 */

public class GameMessage implements Serializable{

    public static final int CHAT_MESSAGE = 100;
    public static final int GAME_POSITION = 200;
    public static final int POWERUP = 201;

    private int type = GAME_POSITION;   // Default value

    private String message;

    private float x;
    private float y;
    private float dx;
    private float dy;
    private float ax;
    private float ay;


    public GameMessage(int type) {
        this.type = type;
    }

    public byte[] toBytes() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] bytes = {};
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(this);              // The object to be serialized
            out.flush();
            bytes = bos.toByteArray();
        }
        catch (IOException ex) {}
        finally {
            try {
                bos.close();
            } catch (IOException ex) {}
        }
        return bytes;
    }

    public static GameMessage fromBytes(byte[] bytes) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        GameMessage gameMessage = null;
        try {
            in = new ObjectInputStream(bis);
            gameMessage = (GameMessage) in.readObject();
        }catch(IOException | ClassNotFoundException e) {}
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return gameMessage;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setCharacterValues(Character c) {
        x = c.getX();
        y = c.getY();
        dx = c.getDx();
        dy = c.getDy();
        //ax = c.getAccelerationX();    // TODO: Missing in Collidable
        //ay = c.getAccelerationY();
    }


    public String toString() {
        String string = "";
        switch(type) {
            case CHAT_MESSAGE:
                string += "Type: CHAT_MESSAGE ";
                string += "- message: " + (message == "" ? "warning:message not set" : message);
                break;
            case GAME_POSITION:
                string += "Type: GAME_POSITION ";
                string += "- x: " + x + " y: " + y + " dx: " + dx + " dy: " + dy;
                break;
            case POWERUP:
                string += "Type: POWERUP ";
                break;
        }
        return string;
    }


}
