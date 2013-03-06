package edu.ucdavis.mcsg.components;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class DisplayTest extends Test{
	final static String BRIGHTNESS = "/sys/class/leds/lcd-backlight/brightness";
	final int[] values = {5, 30, 55, 80, 105, 130, 155, 180, 205, 230, 255};
	
	View whiteView;
	
	public DisplayTest(MainActivity activity) {
		super(activity);

	}
	
	public void run(){
		mainActivity.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				RelativeLayout rootLayout = (RelativeLayout) mainActivity.findViewById(R.id.main_layout);
				if(rootLayout != null){
					whiteView = new View(mainActivity);
					whiteView.setBackgroundColor(Color.WHITE);
					whiteView.setLayoutParams(new RelativeLayout.LayoutParams(
							RelativeLayout.LayoutParams.FILL_PARENT,
							RelativeLayout.LayoutParams.FILL_PARENT));
					rootLayout.addView(whiteView);
				}
				else{
					Log.d(MainActivity.TAG, "Layout was null!");
				}
			}
		});
		

		for(final int b : values){
			try {
				Log.d(MainActivity.TAG, "Setting brightness to: "+b);
				setBrightness(b);
				Thread.sleep(Constants.TEST_TIME);
				setBrightness(0);
				Thread.sleep(Constants.RATE_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		updateText("DONE!");

		mainActivity.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				RelativeLayout rootLayout = (RelativeLayout) mainActivity.findViewById(R.id.main_layout);
				if(rootLayout != null){
					rootLayout.removeView(whiteView);
				}
				else{
					Log.d(MainActivity.TAG, "Layout was null!");
				}
			}
		});
		
	}
	
	public static void setBrightness(int b){
		runRootCommand("echo "+b+" > "+BRIGHTNESS);
	}
}
