package com.ttong.activity;

import com.example.ttong.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CallActivity extends Activity{

	TextView tv;
	Intent i;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call);
		
		tv = (TextView) findViewById(R.id.callName);
		
		i = getIntent();
		String str = i.getExtras().getString("name");
		tv.setText(str);
	}

}
