package com.ttong.activity;

import com.example.ttong.R;
import com.ttong.call_activity.C00Activity;
import com.ttong.call_activity.C01Activity;
import com.ttong.call_activity.C02Activity;
import com.ttong.call_activity.C03Activity;
import com.ttong.call_activity.C10Activity;
import com.ttong.call_activity.C11Activity;
import com.ttong.call_activity.C20Activity;
import com.ttong.call_activity.C22Activity;
import com.ttong.call_activity.C30Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class CallLoadingActivity extends Activity{

	TextView name, phone;
	
	String destName;
	String destPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_loading);
		
		Log.d("#### 1", MainActivity.pref.toString());
		Log.d("#### 2", MainActivity.clientThread.toString());
		Log.d("#### 3", this.toString());
		
		MainActivity.clientThread.setContext(this);
		MainActivity.clientThread.send("StartCall ");

		name = (TextView) findViewById(R.id.receiver);
		phone = (TextView) findViewById(R.id.receiverPhone);
		
		Intent intent = getIntent();
		destName = intent.getExtras().getString("name");
		destPhone = intent.getExtras().getString("phone");

		name.setText(destName);
		phone.setText(destPhone);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.call, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_call_stop) {
			MainActivity.clientThread.send("StopCall ");
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}
}
