package edu.ucdavis.mcsg.components;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	TextView tv;
	final static String TAG = "ComponentTester";
	boolean runningTest = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv = (TextView) findViewById(R.id.details);
		
		Button b = (Button) findViewById(R.id.cpuButton);
		b.setOnClickListener(this);
		
		b = (Button) findViewById(R.id.displayButton);
		b.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Thread t;
		String testName = "";
		
		CPUTest.setMaxFrequency();

		switch(id){
		
		case R.id.cpuButton:
			testName = "CPU Test";
			Log.d(TAG, "Running CPU Test...");
			t = new Thread(new CPUTest(this));
			t.start();
			break;
			
		case R.id.displayButton:
			testName = "Display Test";
			Log.d(TAG, "Running Display Test...");
			t = new Thread(new DisplayTest(this));
			t.start();
			break;
		}
		tv.setText(testName);
		
	}
	
	public void setText(String s){
		tv.setText(s);
	}

}
