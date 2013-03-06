package edu.ucdavis.mcsg.components;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class CellTest extends Test {
	final int MS_PER_SECOND = 1000;
	final String HOST = "192.168.1.8"; // faraday eth0
	final String MSG = "Hello World!";
	final int PORT = 16000;
	
	DatagramSocket mSock;
	DatagramPacket mPacket;
	boolean debug = true;

	public CellTest(MainActivity activity) {
		super(activity);
	}

	boolean running = false;
	@Override
	public void run() {
		ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();

		// Make sure we are on cellular
		if(info != null && info.getType() == ConnectivityManager.TYPE_MOBILE){
		
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
			

			for(int i=0;i<Constants.NUM_LOOPS;i++){

				Timer t = new Timer();
				t.schedule(new TimerTask(){
					@Override
					public void run() {
						running = false;
					}
					
				}, Constants.TEST_TIME);

				running = true;
				while(running){
					try {
						mSock.send(mPacket);
						if(debug)
							Log.d(MainActivity.TAG, "Sent packet");
					} catch (IOException e) {
						e.printStackTrace();
						running = false;
					}
				}

				if(debug)
					Log.d(MainActivity.TAG, "Test "+i+" Done!");

				try {
					Thread.sleep(Constants.RATE_MS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		else{
			Toast.makeText(mainActivity, "No Cellular Data connection!", Toast.LENGTH_LONG).show();
		}

	}

}
