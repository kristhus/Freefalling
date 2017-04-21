package com.pa_gruppe11.freefalling.Singletons;

import android.content.Context;
import android.util.Log;

import com.pa_gruppe11.freefalling.gameControllers.GameMenu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/** Created by Kristian on 24/03/2017
  * 
  *
*/

public class Config {
	
	//private GameMenu context;		// May not be necessary, remove if no errors occur in current implementation
	
	private static Config config = new Config();
	private static final String filename = "/Freefalling.conf";
	
	private Config() {
		//createFile();
	}

	public void createFile() {
		try {
			File file = new File(filename);
			file.createNewFile(); // if file already exists will do nothing
			FileOutputStream oFile = new FileOutputStream(file, false);
			oFile.close();
		}catch(IOException e) {
			e.printStackTrace();
			Log.w("Config", "Error while creating file");
		}
	}
		
	public void readFile(GameMenu context) {
		try {
			FileInputStream fis = new FileInputStream(context.getFilesDir() + filename);
			ObjectInputStream is = new ObjectInputStream(fis);
			is.readObject();
			//Log.w("Config", "FPS: " + datahandler.getInstance());
		//	DataHandler.getInstance().setData(datahandler);
			Log.w("Config", "Success reading file");
			is.close();
			fis.close();
		} catch(Exception e) {
			e.printStackTrace();
			Log.w("Config", "Could not find file to read");
		}
	}
	
	public boolean saveFile(GameMenu context) {
		boolean success = false;
		try {
			FileOutputStream fos = new FileOutputStream(context.getFilesDir() + filename);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(DataHandler.getInstance());
			os.close();
			fos.close();
			success = true;
			GameThread.getInstance().reloadValues(); // Update values such as MAX_FPS
			ResourceLoader.getInstance().reloadAudioVolume();
			Log.w("Config", "Successfully saved file");
		} catch(IOException e) {
			e.printStackTrace();
			Log.w("Config", "Could not find file to save");
		}
		return success;
	}
	
	
	public static Config getInstance() {
		return config;
	}

	//public void setContext(GameMenu context) {
		//this.context = context;
	//}


}