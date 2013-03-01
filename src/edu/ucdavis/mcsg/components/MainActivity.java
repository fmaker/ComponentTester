package edu.ucdavis.mcsg.components;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	TextView tv;
	final static String TAG = "ComponentTester";
	boolean runningTest = false;
	View blackView;
	
	int[] buttonIds = {
			R.id.cpuButton, R.id.displayButton, R.id.cellButton,R.id.wifiButton, R.id.gpsButton};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv = (TextView) findViewById(R.id.details);
		
		for(int id : buttonIds){
			Button b = (Button) findViewById(id);
			b.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Thread t;
		String testName = "";
		
		prepareForTest();

		switch(id){
		
		case R.id.cpuButton:
			testName = "CPU Test";
			Log.d(TAG, "Running "+testName+"...");
			t = new Thread(new CPUTest(this));
			t.start();
			break;
		case R.id.displayButton:
			testName = "Display Test";
			Log.d(TAG, "Running "+testName+"...");
			t = new Thread(new DisplayTest(this));
			t.start();
			break;
		case R.id.cellButton:
			testName = "Cell Test";
			Log.d(TAG, "Running "+testName+"...");
			break;
		case R.id.wifiButton:
			testName = "Wifi Test";
			Log.d(TAG, "Running "+testName+"...");
			t = new Thread(new WifiTest(this));
			t.start();
			break;
		case R.id.gpsButton:
			testName = "GPS Test";
			Log.d(TAG, "Running "+testName+"...");
			break;
		default:
			Log.d(TAG, "Unknown button pressed.");
		}
		tv.setText(testName);
		
	}
	
	private void prepareForTest() {
		CPUTest.setMaxFrequency();
		DisplayTest.setBrightness(0);
	}

	public void setText(String s){
		tv.setText(s);
	}

}
