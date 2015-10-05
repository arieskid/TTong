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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class CallAskActivity extends Activity implements OnClickListener{

	// 만약 내가 call ok 한 사이에 상대방이 전화 끊으면??
	
	ImageButton btn_ok, btn_no;
	TextView name, phone;
	
	
	int destState;
	String destPhone;
	String destName;
	
	String callerIp; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_ask);
		
		Intent i = getIntent();
		destState = i.getExtras().getInt("destState");
		destPhone = i.getExtras().getString("destPhone");
		destName = i.getExtras().getString("destName");
		
		callerIp = i.getExtras().getString("callerIp");

		btn_ok = (ImageButton) findViewById(R.id.callAgree);
		btn_no = (ImageButton) findViewById(R.id.callReject);
		btn_ok.setOnClickListener(this);
		btn_no.setOnClickListener(this);
		
		name = (TextView) findViewById(R.id.callName);
		phone = (TextView) findViewById(R.id.callPhone);
		name.setText(destName);
		phone.setText(destPhone);
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		int myState = MainActivity.pref.getInt("userState", 0);

		String myName = MainActivity.pref.getString("UserName", "no name");
		String myPhone = MainActivity.pref.getString("UserPhone", "no phone number");
		
		Intent i = null;
		Log.d("***", "test callask myState : "+String.valueOf(myState));
		Log.d("***", "test callask destState : "+String.valueOf(destState));
		
		// ok button
		if(v.getId() == R.id.callAgree){
			
			int num = destState + (myState*10);
			MainActivity.clientThread.activityNum = num; 
			Log.d("####", "CallAsk : "+ num);
			
			switch(num){
				case 0:
					i = new Intent(this, C00Activity.class);
					Log.d("****", "test callask : 0");
					break;
				case 1:
					i = new Intent(this, C01Activity.class);
					Log.d("****", "test callask : 1");
					break;
				case 2:
					i = new Intent(this, C02Activity.class);
					Log.d("****", "test callask : 2");
					break;
				case 3:
					i = new Intent(this, C03Activity.class);
					Log.d("****", "test callask : 3");
					break;
				case 10: case 12:
					i = new Intent(this, C10Activity.class);
					Log.d("****", "test callask : 10");
					break;
				case 11: case 13:
					i = new Intent(this, C11Activity.class);
					Log.d("****", "test callask : 11");
					break;
				case 20: case 21:
					i = new Intent(this, C20Activity.class);
					Log.d("****", "test callask : 20");
					break;
				case 22: case 23:
					i = new Intent(this, C22Activity.class);
					Log.d("****", "test callask :22");
					break;
				case 30: case 31: case 32: case 33:
					i = new Intent(this, C30Activity.class);
					Log.d("****", "test callask : 30");
					break;
			}
			
			MainActivity.clientThread.send("OkayCall "+"/"+myState+"/"+myName+"/"+myPhone);
			i.putExtra("destName", destName);
			i.putExtra("destPhone", destPhone);
			
			i.putExtra("destIp", callerIp);
			i.putExtra("sendPort", 1989);
			i.putExtra("recvPort", 1988);
			
			startActivity(i);
			finish();
		} 
		
		// no button
		else if(v.getId() == R.id.callReject){
			MainActivity.clientThread.send("StopCall ");

			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
