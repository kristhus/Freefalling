package com.pa_gruppe11.freefalling.framework;


import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.*;

import com.pa_gruppe11.freefalling.Singletons.DataHandler;

/**
 * Created by Kristian on 21/04/2017.
 */

public class AudioFreefalling implements OnCompletionListener, OnSeekCompleteListener, OnPreparedListener {


    private MediaPlayer mediaPlayer;
    private boolean prepared;           // Indicates if mediaplayer has finished loading the audiofile
    private final int maxSteps = 100;   // To match the Settings sliders

    // Audio type - Possible to create other types, to prepare more settings for the user
    // Easy to prepare more in depth types of audio, e.g. collision sound, death sound
    // This again makes it easier for the user to mute audio that can become obnoxious in the long run
    public static final int BGM = 0;    // Necessary for Settings to be able to differentiate the two
    public static final int SFX = 1;    //

    public int AUDIO_TYPE;

    /**
     * Create a mediaplayer from a resource id, and specify the type
     * @param c     Context, e.g. an activity of type GameMenu
     * @param id    R.raw.<name>
     * @param AUDIO_TYPE Mediaplayer.BGM or Mediaplayer.SFX
     */
    public AudioFreefalling(Context c, int id, int AUDIO_TYPE) {
        this.AUDIO_TYPE = AUDIO_TYPE;
        mediaPlayer = MediaPlayer.create(c, id);
        prepared = true;
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnPreparedListener(this);
        if(AUDIO_TYPE == BGM) {
            if (DataHandler.getInstance().isBgmMuted())
                setVolume(0);
            else
                setVolume(DataHandler.getInstance().getBgmLevel());
        }
        else if(AUDIO_TYPE == SFX) {
            if(DataHandler.getInstance().isSfxMuted())
                setVolume(0);
            else
                setVolume(DataHandler.getInstance().getSfxLevel());
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        prepared = false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        prepared = true;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {    // Unused

    }

    /**
     * Play the audio as defined by the resource initialized with
     */
    public void play() {
        if (mediaPlayer.isPlaying()) // No sense in playing a playing song. Go through the right channels, and stop it before playing
            return;
        try {
            mediaPlayer.start();
        }catch(IllegalStateException e) {}
    }

    /**
     * Stop the currently playing mediaPlayer
     */
    public void stop() {
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }

    /**
     * Stop and release the resources connected to the mediaplayer
     */
    public void dispose() {
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.release();
    }

    /**
     * Convert a value between 0 and 100 logaritmically to a volume mediaplayer can use.
     * @param db    A value between 0 and 100
     */
    public void setVolume(float db) {
        db = Math.min(db, 100);
        float log=(float)(Math.log(maxSteps-db)/Math.log(maxSteps));
        mediaPlayer.setVolume(1-log, 1-log);
    }

    /**
     * Tells mediaplayer to loop the queued audiofile
     * @param loop Weather or not the mediaplayer should replay the audiofile
     */
    public void loopBGM(boolean loop) {
        mediaPlayer.setLooping(loop);
    }

    /**
     * Set volume to 0, or reload values from DataHandler
     * @param mute
     */
    public void mute(boolean mute) {
        if(mute)
            setVolume(0);
        else {
            if(AUDIO_TYPE == BGM)
                setVolume(DataHandler.getInstance().getBgmLevel());
            else if(AUDIO_TYPE == SFX) {
                setVolume(DataHandler.getInstance().getSfxLevel());
            }
        }
    }


    public int getAudioType() {
        return AUDIO_TYPE;
    }
}
