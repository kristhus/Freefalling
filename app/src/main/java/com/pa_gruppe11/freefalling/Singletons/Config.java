package com.pa_gruppe11.freefalling.Singletons;

import android.content.Context;
import android.util.Log;

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
	private static final String filename = "Freefalling.conf";
	
	private Config() {
	}
		
	public void readFile(GameMenu context) {
		try {
			FileInputStream fis = context.openFileOutput(filename, Context.MODE_PRIVATE);
			ObjectInputStream is = new ObjectInputStream(fis);
			DataHandler datahandler = (DataHandler) is.readObject();
			is.close();
			fis.close();
		} catch(IOException e) {
			Log.w("Config", "Could not find file"));
		}
	}
	
	public void saveFile() {
		try {
			FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(DataHandler);
			os.close();
			fos.close();
		} catch(IOException e) {
			Log.w("Config", "Could not find file"));
		}
	}
	
	
	public Config getInstance() {
		return config;
	}

	//public void setContext(GameMenu context) {
		this.context = context;
	}


}