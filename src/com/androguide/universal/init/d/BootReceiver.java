package com.androguide.universal.init.d;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private SharedPreferences prefs;
	private Boolean execute, doTest;
	private String[] scripts;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		// Get our ON/OFF Switch state
		prefs = context.getSharedPreferences("INITD_PREFS", 0);

		// Check if testing the kernel's init.d support is needed
		doNeedTest();

		execute = prefs.getBoolean("IS_ON", false);

		// If the Switch is ON, execute all scripts
		if (execute == true) {

			Log.v("INITD", "The Switch is ON, execute scripts");

			// Get the list of init.d scripts
			File dir = new File("/system/etc/init.d");
			File[] filelist = dir.listFiles();
			scripts = new String[filelist.length];
			for (int i = 0; i < scripts.length; i++) {
				scripts[i] = filelist[i].getName();

				// Execute each script one after the other in the loop
				CMDProcessor cmd = new CMDProcessor();
				cmd.su.runWaitFor("sh /system/etc/init.d/" + scripts[i]);
			}

		} else
			Log.v("INITD", "The Switch is OFF, DO NOTHING");
	}

	// Our method to check if the user asked for a test
	private void doNeedTest() {

		doTest = prefs.getBoolean("DO_TEST", false);

		if (doTest == true)
			runTest();
	}

	// The actual test
	private void runTest() {

		String sdcard = Environment.getExternalStorageDirectory().toString();
		File file = new File(sdcard + "/initdsupport.txt");
		if (file.exists()) {

			Editor e = prefs.edit();
			e.putInt("TEST_RESULT", 1);
			e.commit();

		} else {

			Editor e = prefs.edit();
			e.putInt("TEST_RESULT", 2);
			e.commit();
		}
	}
}
