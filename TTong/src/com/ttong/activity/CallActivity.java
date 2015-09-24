package com.ttong.activity;

import com.example.ttong.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CallActivity extends Activity{

	Intent i;	
	String name;
	String phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call);
		
		TextView name_t = (TextView) findViewById(R.id.callName);
		TextView phone_t = (TextView) findViewById(R.id.callPhone);
		
		i = getIntent();
		name = i.getExtras().getString("name");
		phone = i.getExtras().getString("phone");
		name_t.setText(name);
		phone_t.setText(phone);
	}

}
