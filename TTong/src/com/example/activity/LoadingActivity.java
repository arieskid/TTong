package com.example.activity;


import com.example.ttong.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends Activity{

	private final int delayTime = 1 * 1000; 	// wait for 1 second.
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
}
