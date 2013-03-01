package edu.ucdavis.mcsg.components;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class WifiTest extends Test implements Runnable {
	final int[] packetRates = 
		{8,10,12,14,18,20,25,30,35,40,45,50,100,125,200,250,500};
	final int MS_PER_SECOND = 1000;
	final String HOST = "192.168.1.8"; // faraday eth0
	final String MSG = "Hello World!";
	final int PORT = 16000;
	
	DatagramSocket mSock;
	DatagramPacket mPacket;
	boolean debug = true;

	public WifiTest(MainActivity activity) {
		super(activity);
	}

	@Override
	public void run() {
		ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();

		// Make sure we are on wifi
		if(info != null && info.getType() == ConnectivityManager.TYPE_WIFI){
		
			try {
				mSock = new DatagramSocket();
				InetAddress addr = InetAddress.getByName(HOST);
				byte[] data = MSG.getBytes("US-ASCII");
				mPacket = new DatagramPacket(data, data.length, addr, PORT);
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			packetRateTest();
		}
		else{
			Toast.makeText(mainActivity, "No Wifi connection!", Toast.LENGTH_LONG).show();
		}

	}

	boolean testRunning;
	private void packetRateTest(){
		long start;

		for(int rate : packetRates){
			final Timer t = new Timer();
			final SendPacket sp = new SendPacket();
		//int rate = packetRates[0];
			if(debug)
				Log.d(MainActivity.TAG, "Packet rate: "+rate);
			
			// Start sending packets at rate/sec
			testRunning = true;
			start = System.currentTimeMillis();
			int period = MS_PER_SECOND/rate;
			t.scheduleAtFixedRate(sp, 0, period);

			// Schedule stop after TEST_TIME
			t.schedule(new TimerTask(){
				@Override
				public void run() {
					sp.cancel();
					testRunning = false;
				}
			}, new Date(start+Constants.TEST_TIME));
			while(testRunning);  // Block until test finished
			t.cancel();
			if(debug)
				Log.d(MainActivity.TAG, "Test done!");
			
		}
	}
	
	private class SendPacket extends TimerTask{
		@Override
		public void run() {
			try {
				mSock.send(mPacket);
				if(debug)
					Log.d(MainActivity.TAG, "Sent packet");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
