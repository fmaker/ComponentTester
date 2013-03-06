package edu.ucdavis.mcsg.components;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPSTest extends Test implements Runnable {
	final int LOOPS = 5;
	long start;
	LocationManager lm;
	GpsUpdater updater;
	Timer t;
	boolean finished;
	
	int loopCount;

	public GPSTest(MainActivity activity) {
		super(activity);
		lm = (LocationManager) mainActivity.getSystemService(Activity.LOCATION_SERVICE);
		updater = new GpsUpdater();
		loopCount = 0;
	}

	@Override
	public void run() {
		for(int i=0;i<LOOPS;i++){
			t = new Timer();
			finished = false;
			start = System.currentTimeMillis();
			gpsOn();
			t.schedule(new GpsOff(), new Date(start+Constants.TEST_TIME));
			//Log.d(MainActivity.TAG, "Sceduled GPS off for: "+(start+Constants.TEST_TIME));
			while(!finished);
			try {
				Thread.sleep(Constants.RATE_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class GpsOff extends TimerTask{

		@Override
		public void run() {
			lm.removeUpdates(updater);
			Log.d(MainActivity.TAG, "Turning GPS off!");
			cancel();
			finished = true;
		}
	}
	
	private void gpsOn(){
		Log.d(MainActivity.TAG, "Turning GPS ON...");
		mainActivity.runOnUiThread(updater);
		Log.d(MainActivity.TAG, "Turned GPS ON!");
	}

	private class GpsUpdater implements Runnable, LocationListener{

		@Override
		public void run() {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this); 

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.d(MainActivity.TAG, "onStatusChanged");
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.d(MainActivity.TAG, "onProviderEnabled");
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.d(MainActivity.TAG, "onProviderDisabled");
		}

		@Override
		public void onLocationChanged(Location location) {
			Log.d(MainActivity.TAG, "onLocationChanged");
		}
	}


}
