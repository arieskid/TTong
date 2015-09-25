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

public class CallLoadingActivity extends Activity{

	String destName;
	String destPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setC - xml;

		//MainActivity.clientThread.setContext(this);
		Intent intent = getIntent();
		destName = intent.getExtras().getString("name");
		destPhone = intent.getExtras().getString("phone");

		MainActivity.clientThread.setContext(this);
	}


}
