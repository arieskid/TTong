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

		//////////////////////////////
		runOnUiThread(new Runnable() {

			public void run() {

				TelephonyManager telManager = (TelephonyManager) getApplicationContext()
						.getSystemService(getApplicationContext().TELEPHONY_SERVICE);
				String phoneNum = telManager.getLine1Number();
				phoneNum = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, 7) + "-" + phoneNum.substring(7, 11);

				try {
					URL url = new URL(SERVER_ADDRESS + "/ttong_updateIP.php?" + "phone_number="
							+ URLEncoder.encode(phoneNum, "UTF-8"));
					url.openStream();

					String result = getXmlData("updateipresult.xml", "result");

					if (result.equals("1")) {
						Toast.makeText(LoadingActivity.this, "update IP", Toast.LENGTH_SHORT).show();

					} else
						Toast.makeText(LoadingActivity.this, "update IP failed", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private String getXmlData(String filename, String str) {
		String rss = SERVER_ADDRESS + "/result/";
		String ret = "";

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			URL server = new URL(rss + filename);
			InputStream is = server.openStream();
			xpp.setInput(is, "UTF-8");

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(str)) {
						ret = xpp.nextText();
					}
				}
				eventType = xpp.next();
			}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
