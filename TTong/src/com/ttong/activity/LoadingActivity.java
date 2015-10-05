package com.ttong.activity;


import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.ttong.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class LoadingActivity extends Activity{

	private final int delayTime = 2 * 1000; 	// wait for 2 second.
	private final String SERVER_ADDRESS = "http://14.63.226.208";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.enableDefaults();
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent i = new Intent(LoadingActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		}, delayTime);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
}
