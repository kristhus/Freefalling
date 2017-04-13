package com.pa_gruppe11.freefalling.Singletons;


import com.pa_gruppe11.freefalling.framework.GameServiceListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/** Created by Kristian on 24/03/2017
  * This data is a value holder for the various settings.
  * Is to be serialized and saved entirely to a file. 
  *	Fill with values as needed. Newly added fields will be saved as wanted, but removals will cause errors.
 *
 * Serialization means that fields can not be static, for proper deserialization (With instance as the exception)
 */
public final class DataHandler implements Serializable{
	
	private int FPS = 30;
	private int sfxLevel = 50;
	private int bgmLevel = 50;
	private boolean sfxMuted = false;
	private boolean bgmMuted = false;

	private boolean hideMinimap = false;

	private int screenWidth;
	private int screenHeight;

	private static DataHandler INSTANCE ;


	private static GameServiceListener messageListener = null; // The static part makes sure that this value will not get serialized

	
	private DataHandler() {}	// No need to initiate this file by other classes.

	public static DataHandler getInstance() {
		if(INSTANCE == null) {
			synchronized (DataHandler.class) {		// in case shit hits the fan and everyone wants a piece of data at the same time
				if(INSTANCE == null)
					INSTANCE = new DataHandler();
			}
		}
		return INSTANCE;
	}


	/*
	Accidentally broke the system, and made two objects through serialization. This fixes the issue
	 */
	private Object readResolve() throws ObjectStreamException{
		return getInstance();
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		INSTANCE = this;
	}

	public int getFPS() {
		return FPS;
	}
	public void setFPS(int FPS){
		this.FPS = FPS;
	}

	public boolean isSfxMuted() {
		return sfxMuted;
	}
	public void setSfxMuted(boolean sfxMuted) {
		this.sfxMuted = sfxMuted;
	}

	public int getSfxLevel() {
		return sfxLevel;
	}

	public void setSfxLevel(int sfxLevel) {
		this.sfxLevel = sfxLevel;
	}

	public int getBgmLevel() {
		return bgmLevel;
	}

	public void setBgmLevel(int bgmLevel) {
		this.bgmLevel = bgmLevel;
	}

	public boolean isBgmMuted() {
		return bgmMuted;
	}

	public void setBgmMuted(boolean bgmMuted) {
		this.bgmMuted = bgmMuted;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public boolean isHideMinimap() {
		return hideMinimap;
	}
	public void setHideMinimap(boolean hideMinimap) {
		this.hideMinimap = hideMinimap;
	}


	/**
	 * Returns messagelistener, or a new instance if it is not instantiated yet
	 * @return
	 */
	public static GameServiceListener getMessageListener() {
		return messageListener == null ? messageListener = new GameServiceListener() : messageListener;
	}
	public static void setMessageListener(GameServiceListener ml) {
		messageListener = ml;
	}

	/**
	 *
	 * @param width percentage of the screenwidth's position
	 */
	public int getRelativeWidth(float width) {
		return (int) (screenWidth * (width/100));
	}

	/**
	 *
	 * @param height percentage of the screenHeight's position
	 */
	public int getRelativeHeight(float height) {
		return (int) (screenHeight * (height/100));
	}

	public int getRelativeX(float x) {
		return (int) (screenWidth * (x/100));
	}


}