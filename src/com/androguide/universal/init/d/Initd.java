package com.androguide.universal.init.d;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar.LayoutParams;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import de.ankri.views.Switch;

public class Initd extends SherlockFragment {

	private int pos = 0;
	private Switch toggle;
	private SharedPreferences prefs;
	private Boolean isOn, test;
	private int result;
	private ActionMode mActionMode;
	private StableArrayAdapter adapter;
	private ListView listview;
	private String[] scripts;
	private View v;
	private Button testButton;
	private TextView testValue;
	private ScrollView ll;
	private SherlockFragmentActivity fa;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		fa = super.getSherlockActivity();
		ll = (ScrollView) inflater.inflate(R.layout.initd, container, false);

		prefs = fa.getSharedPreferences("INITD_PREFS", 0);
		toggle = (Switch) ll.findViewById(R.id.toggle);
		testButton = (Button) ll.findViewById(R.id.testButton);
		testButton.setOnClickListener(testListener);
		testValue = (TextView) ll.findViewById(R.id.testValue);

		isOn = prefs.getBoolean("IS_ON", false);

		if (isOn == true) {
			toggle.setChecked(true);
			Log.v("SWITCH", "The switch is" + isOn);
		} else {
			toggle.setChecked(false);
			Log.v("SWITCH", "The switch is" + isOn);
		}

		setTestValue();

		toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

				if (toggle.isChecked()) {

					Editor e = prefs.edit();
					e.putBoolean("IS_ON", true);
					e.commit();

					test = true;
					Log.v("SWITCH", "The switch is" + test);

				} else {

					Editor e = prefs.edit();
					e.putBoolean("IS_ON", false);
					e.commit();

					test = false;
					Log.v("SWITCH", "The switch is" + test);
				}
			}
		});

		File dir = new File("/system/etc/init.d");
		CMDProcessor cmd = new CMDProcessor();

		if (!dir.exists()) {

			cmd.su.runWaitFor("mount -o remount,rw /system");
			cmd.su.runWaitFor("mkdir /system/etc/init.d");
			cmd.su.runWaitFor("chmod 755 /system/etc/init.d");
			cmd.su.runWaitFor("echo \"no scripts found\" >> /system/etc/init.d/no");
			cmd.su.runWaitFor("echo \"no scripts found\" >> /system/etc/init.d/scripts");
			cmd.su.runWaitFor("echo \"no scripts found\" >> /system/etc/init.d/found");
			cmd.su.runWaitFor("chmod 755 /system/etc/init.d/no");
			cmd.su.runWaitFor("chmod 755 /system/etc/init.d/scripts");
			cmd.su.runWaitFor("chmod 755 /system/etc/init.d/found");
			cmd.su.runWaitFor("mount -o remount,ro /system");
			cmd.su.runWaitFor("mount -o remount,ro /system");
		} else if (!dir.canRead()) {

			cmd.su.runWaitFor("mount -o remount,rw /system");
			cmd.su.runWaitFor("chmod 755 /system/etc/init.d");
			cmd.su.runWaitFor("mount -o remount,ro /system");
		}

		File d = new File("/system/etc/init.d");		
		File[] file = d.listFiles();

		if (file.length == 0) {

			cmd.su.runWaitFor("mount -o remount,rw /system");
			cmd.su.runWaitFor("echo \"no scripts found\" >> /system/etc/init.d/no");
			cmd.su.runWaitFor("echo \"no scripts found\" >> /system/etc/init.d/scripts");
			cmd.su.runWaitFor("echo \"no scripts found\" >> /system/etc/init.d/found");
			cmd.su.runWaitFor("chmod 755 /system/etc/init.d/no");
			cmd.su.runWaitFor("chmod 755 /system/etc/init.d/scripts");
			cmd.su.runWaitFor("chmod 755 /system/etc/init.d/found");
			cmd.su.runWaitFor("mount -o remount,ro /system");
		}

		generateList();
		adjustListViewHeight(listview);

		return ll;
	}

	private void generateList() {

		File dir = new File("/system/etc/init.d");
		File[] f = dir.listFiles();

		if (!dir.exists()) {

			CMDProcessor cmd = new CMDProcessor();
			cmd.su.runWaitFor("mount -o remount,rw /system");
			cmd.su.runWaitFor("mkdir /system/etc/init.d");
			cmd.su.runWaitFor("chmod 755 /system/etc/init.d");
			cmd.su.runWaitFor("mount -o remount,ro /system");

		} else if (f.length == 0) {

			CMDProcessor cmd = new CMDProcessor();
			cmd.su.runWaitFor("mount -o remount,rw /system");
			cmd.su.runWaitFor("echo \"no scripts found\" >> /system/etc/init.d/no");
			cmd.su.runWaitFor("echo \"no scripts found\" >> /system/etc/init.d/scripts");
			cmd.su.runWaitFor("echo \"no scripts found\" >> /system/etc/init.d/found");
			cmd.su.runWaitFor("chmod 755 /system/etc/init.d/no");
			cmd.su.runWaitFor("chmod 755 /system/etc/init.d/scripts");
			cmd.su.runWaitFor("chmod 755 /system/etc/init.d/found");
			cmd.su.runWaitFor("mount -o remount,ro /system");

		} else {

			File[] filelist = dir.listFiles();
			scripts = new String[filelist.length];
			Log.v("INITD", "number of scripts found : " + filelist.length);
			for (int i = 0; i < scripts.length; i++) {
				scripts[i] = filelist[i].getName();
			}
			listview = (ListView) ll.findViewById(R.id.listview);

			final ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < scripts.length; ++i) {
				list.add(scripts[i]);
			}

			adapter = new StableArrayAdapter(fa,
					android.R.layout.simple_list_item_1, list);
			listview.setAdapter(adapter);

			listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, final View view,
						int position, long id) {

					pos = position;
					v = view;

					if (mActionMode != null) {

					} else {
						mActionMode = fa.startActionMode(mActionModeCallback);
					}
				}
			});
		}

	}

	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}
	}

	private android.view.View.OnClickListener testListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			CMDProcessor cmd = new CMDProcessor();
			String sdcard = Environment.getExternalStorageDirectory()
					.toString();

			isOn = prefs.getBoolean("IS_ON", false);
			if (isOn == true) {

				Toast.makeText(fa, getString(R.string.turn_switch_off),
						Toast.LENGTH_SHORT).show();
			} else {

				cmd.su.runWaitFor("busybox mount -o remount,rw /system");
				// Just in case the user doesn't have busybox
				cmd.su.runWaitFor("mount -o remount,rw /system");

				cmd.su.runWaitFor("echo \"#!/system/bin/sh\" >> /system/etc/init.d/testinitd");
				cmd.su.runWaitFor("echo \"\" >> /system/etc/init.d/testinitd");
				cmd.su.runWaitFor("echo \"echo \"init.d is working\" >> "
						+ sdcard
						+ "/initdsupport.txt\" >> /system/etc/init.d/testinitd");

				cmd.su.runWaitFor("busybox mount -o remount,ro /system");
				// Just in case the user doesn't have busybox
				cmd.su.runWaitFor("mount -o remount,ro /system");

				testValue.setText(getString(R.string.test_reboot_required));

				Editor e = prefs.edit();
				e.putBoolean("DO_TEST", true);
				e.commit();
			}
		}
	};

	private void setTestValue() {

		result = prefs.getInt("TEST_RESULT", 0);

		if (result == 1) {
			testValue.setText(getString(R.string.test_positive));
			testValue.setTextColor(getResources().getColor(R.color.test_red));

		} else if (result == 2) {
			testValue.setText(getString(R.string.test_negative));
			testValue.setTextColor(getResources().getColor(R.color.test_green));

		} else if (result == 0) {
			testValue.setText(getString(R.string.test_not_started));
			testValue.setTextColor(getResources().getColor(R.color.holo_blue));
		}
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.main, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		// our method to execute the selected script
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {

			case R.id.delete:
				deleteScript();
				mode.finish();
				return true;

			case R.id.edit:
				editDialog();
				mode.finish();
				return true;

			case R.id.execute:
				executeScript();
				mode.finish();
				return true;

			default:
				return false;
			}
		}

		// our method to delete the selected script
		// we try both with busybox & without it to ensure compatibility with
		// most setups
		// because anyway if a command fail it has no incidence in this case.
		private void deleteScript() {

			String toDelete = scripts[pos];
			Log.v("Script to Delete :", " " + toDelete);
			CMDProcessor cmd = new CMDProcessor();
			cmd.su.runWaitFor("busybox mount -o remount,rw /system");
			cmd.su.runWaitFor("mount -o remount,rw /system");
			cmd.su.runWaitFor("rm -f /system/etc/init.d/" + toDelete);
			cmd.su.runWaitFor("busybox rm -f /system/etc/initd/" + toDelete);
			cmd.su.runWaitFor("mount -o remount,ro /system");
			cmd.su.runWaitFor("busybox mount -o remount,ro /system");

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
				animateDelete();
			else {
				v.setAlpha(1);
				generateList();
				adjustListViewHeight(listview);
			}
		}

		
		private void animateDelete() {

			v.animate().setDuration(2000).alpha(0)
					.withEndAction(new Runnable() {

						@Override
						public void run() {

							v.setAlpha(1);
							generateList();
							adjustListViewHeight(listview);
						}
					});
		}

		// our method to execute the selected script
		private void executeScript() {

			String currScript = scripts[pos];
			CMDProcessor cmd = new CMDProcessor();
			cmd.su.runWaitFor("sh /system/etc/init.d/" + currScript);
		}

		// our method to edit the selected script
		private void editDialog() {

			AlertDialog.Builder alert = new AlertDialog.Builder(fa);

			String currScript = scripts[pos];
			CMDProcessor cmd = new CMDProcessor();
			cmd.su.runWaitFor("busybox mount -o remount,rw /system");

			final EditText input = new EditText(fa);
			alert.setView(input);
			StringBuilder text = new StringBuilder();

			try {
				String line;
				Process process = Runtime.getRuntime().exec("su");
				OutputStream stdin = process.getOutputStream();
				InputStream stderr = process.getErrorStream();
				InputStream stdout = process.getInputStream();

				stdin.write(("cat /system/etc/init.d/" + currScript + "\n")
						.getBytes());
				stdin.write("exit\n".getBytes());
				stdin.flush();

				stdin.close();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						stdout));
				while ((line = br.readLine()) != null) {
					text.append(line);
					text.append('\n');
					Log.d("[Output]", line);
				}
				br.close();
				br = new BufferedReader(new InputStreamReader(stderr));
				while ((line = br.readLine()) != null) {
					Log.e("[Error]", line);
				}
				br.close();

				process.waitFor();
				process.destroy();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			alert.setTitle(currScript);
			input.setText(text);
			input.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			input.setHeight(400);

			alert.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

							String value = input.getText().toString();
							String currScript = scripts[pos];
							CMDProcessor cmd = new CMDProcessor();
							cmd.su.runWaitFor("busybox mount -o remount,rw /system");
							cmd.su.runWaitFor("rm -f /system/etc/init.d/"
									+ currScript);
							cmd.su.runWaitFor("echo \"" + value
									+ "\" >> /system/etc/init.d/" + currScript);
							cmd.su.runWaitFor("busybox mount -o remount,ro /system");
						}
					});

			alert.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled
						}
					});
			alert.setCancelable(false);
			alert.show();
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
	};

	private void adjustListViewHeight(ListView listView) {
		StableArrayAdapter listAdapter = adapter;
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup)
				listItem.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
