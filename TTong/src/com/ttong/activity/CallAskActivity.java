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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_ask);
		
		Intent i = getIntent();
		destState = i.getExtras().getInt("destState");
		destPhone = i.getExtras().getString("destPhone");
		destName = i.getExtras().getString("destName");

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
	public void onClick(View v) {
		int myState = MainActivity.pref.getInt("userState", 0);

		String myName = MainActivity.pref.getString("UserName", "no name");
		String myPhone = MainActivity.pref.getString("UserPhone", "no phone number");
		
		Intent i = null;
		Log.d("***", "test myState : "+String.valueOf(myState));
		Log.d("***", "test destState : "+String.valueOf(destState));
		
		// ok button
		if(v.getId() == R.id.callAgree){
			switch(destState + (myState*10)){
				case 0:
					i = new Intent(this, C00Activity.class);
					break;
				case 1:
					i = new Intent(this, C01Activity.class);
					break;
				case 2:
					i = new Intent(this, C02Activity.class);
					break;
				case 3:
					i = new Intent(this, C03Activity.class);
					break;
				case 10: case 12:
					i = new Intent(this, C10Activity.class);
					break;
				case 11: case 13:
					i = new Intent(this, C11Activity.class);
					break;
				case 20: case 21:
					i = new Intent(this, C20Activity.class);
					break;
				case 22: case 23:
					i = new Intent(this, C22Activity.class);
					break;
				case 30: case 31: case 32: case 33:
					Log.d("****", "test : 0");
					i = new Intent(this, C30Activity.class);
					break;
			}
			
			Log.d("****", "test : 1");
			MainActivity.clientThread.send("OkayCall "+"/"+myState+"/"+myName+"/"+myPhone);
			Log.d("****", "test : 2");
			i.putExtra("destName", destName);
			i.putExtra("destPhone", destPhone);
			startActivity(i);
		} 
		
		// no button
		else if(v.getId() == R.id.callReject){
			MainActivity.clientThread.send("StopCall ");
		}
	}
}
