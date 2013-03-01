package edu.ucdavis.mcsg.components;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

public class CPUTest extends Test{
	static final String GOVERNOR = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor";
	static final String READ_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";
	static final String WRITE_FREQ = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_setspeed";
	static final String FREQS = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_available_frequencies";
	static final String CAT = "/system/bin/cat";	

	static final int FREQ_SETTLE = Constants.RATE_MS; // 250 ms
	
	boolean debug = true;
	boolean running = false;
	
	MainActivity mainActivity;
	
	public CPUTest(MainActivity activity) {
		super(activity);
	}

	public void run(){
		try {
			printSettings();
			
			// Change to userspace governor
			runRootCommand("echo userspace > "+GOVERNOR);
			String[] freqs =  getAvailiableFreqs();
			for(final String freq : freqs){
				updateText(freq);
				runRootCommand("echo "+freq+" > "+WRITE_FREQ);
				printSettings();
				Thread.sleep(FREQ_SETTLE);
				busyWait(Constants.TEST_TIME);
			}
			
			updateText("DONE!");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void setMaxFrequency(){
		ArrayList<String> freqs =  new ArrayList<String>(Arrays.asList(getAvailiableFreqs()));
		Collections.sort(freqs);
		String maxFreq = freqs.get(freqs.size()-1);
		
		Log.d(MainActivity.TAG, "Setting Max Freq: "+maxFreq);
		runRootCommand("echo userspace > "+GOVERNOR);
		runRootCommand("echo "+maxFreq+" > "+WRITE_FREQ);

		try {
			Thread.sleep(FREQ_SETTLE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void busyWait(long duration){
		int i=0;
		TimerTask task = new TimerTask(){
			@Override
			public void run() {
				running = false;
			}
		};
		Timer t = new Timer();

		// Schedule task to end test and run dummy operation
		running = true;
		t.schedule(task, duration);
		while(running){
			i++;
		}
		if(debug)
			Log.d(MainActivity.TAG, "Test: "+i);
	}
	
	private void printSettings(){
		try {
			String gov = getStringFromFile(GOVERNOR);
			String freq = getStringFromFile(READ_FREQ);

			if(debug){
				Log.d(MainActivity.TAG, "Governor: "+gov+" Freq: "+freq);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String[] getAvailiableFreqs(){
		String allFreqs = null;
		try {
			allFreqs = getStringFromFile(FREQS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allFreqs != null ? allFreqs.split(" ") : null;
		
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
