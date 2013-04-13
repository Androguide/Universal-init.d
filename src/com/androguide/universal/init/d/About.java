package com.androguide.universal.init.d;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class About extends SherlockFragment {
	private ScrollView ll;
	SherlockFragmentActivity fa;

	ImageView github, play;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		fa = super.getSherlockActivity();
		ll = (ScrollView) inflater.inflate(R.layout.about, container, false);
		
		github = (ImageView) ll.findViewById(R.id.github);
		play = (ImageView) ll.findViewById(R.id.googlePlay);
		
		github.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("https://github.com/Androguide"));
		        startActivity(intent);
		    }
		});
		
		play.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v){
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_VIEW);
		        intent.addCategory(Intent.CATEGORY_BROWSABLE);
		        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.androguide.pimp.my.rom"));
		        startActivity(intent);
		    }
		});

		return ll;
	}

}
