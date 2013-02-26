package edu.ucdavis.mcsg.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class CPUTest implements Runnable{
	static final String GOVERNOR = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
	static final String CAT = "/system/bin/cat";
	
	
	public void run(){
		try {
			String originalGov = getStringFromFile(GOVERNOR);
			Log.d(MainActivity.TAG, "Original Governor: "+originalGov);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private static String getStringFromFile (String filePath) throws Exception {
	    File fl = new File(filePath);
	    FileInputStream fin = new FileInputStream(fl);
	    return convertStreamToString(fin);
	    //Make sure you close all streams.
	}
	
	private static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line).append("\n");
	    }
	    is.close();
	    return sb.toString();
	}
}
