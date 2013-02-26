package edu.ucdavis.mcsg.components;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	final static String TAG = "ComponentTester";
	boolean runningTest = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button b = (Button) findViewById(R.id.cpuButton);
		b.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		Thread t;

		switch(id){
		
		case R.id.cpuButton:
			Log.d(TAG, "Running CPU Test...");
			t = new Thread(new CPUTest());
			t.run();
			break;
		}
	}

}
