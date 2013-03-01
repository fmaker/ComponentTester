package edu.ucdavis.mcsg.components;

import java.io.DataOutputStream;


public abstract class Test implements Runnable{
	
	MainActivity mainActivity;
	
	public Test(MainActivity activity) {
		this.mainActivity = activity;
	}
	
	protected void updateText(final String s){
		mainActivity.runOnUiThread(new Runnable(){
			@Override
			public void run() {
				mainActivity.setText(s);
			}
		});
	}

	
	public static boolean runRootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}
}
