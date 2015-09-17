package com.example.activity;

import com.example.ttong.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener{

	EditText nameET, phoneET;
	Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		nameET = (EditText) findViewById(R.id.name);
		phoneET = (EditText) findViewById(R.id.phone);
		btn = (Button) findViewById(R.id.okBtn);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
	
		SharedPreferences preference = MainActivity.pref;
		SharedPreferences.Editor editor = preference.edit();
		
		String name = nameET.getText().toString();
		String phone = phoneET.getText().toString();
		
		editor.putBoolean("Status", true);
		editor.putString("UserName", name);
		editor.putString("UserPhone", phone);
		editor.commit();
		
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
	
}
