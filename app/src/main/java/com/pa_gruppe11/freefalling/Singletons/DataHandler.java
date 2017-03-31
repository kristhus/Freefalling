package com.pa_gruppe11.freefalling.Singletons;


/** Created by Kristian on 24/03/2017
  * This data is a value holder for the various settings.
  * Is to be serialized and saved entirely to a file. 
  *	Fill with values as needed. Newly added fields will be saved as wanted, but removals will cause errors. 
 */
public final class DataHandler {
	
	public static int FPS = 30;
	public static int sfxLevel = 50;
	public static int bgmLevel = 50;
	public static boolean sfxMuted = false;
	public static boolean bgmMuted = false;
	
	private static DataHandler = new DataHandler();
	private DataHandler() {}	// No need to initiate this file by other classes.
	
	
}