package com.ttong.activity;

import com.example.ttong.R;
import com.ttong.model.UserState;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

//만약 다시 로그인했을 때 상태가 매번 0이 될텐데...;;
public class SettingActivity extends Activity {

	TextView name, phone;
	Switch hearDisable, speakDisable;
	
	Intent i;
	String userName, userPhone;

	boolean hearState, speakState;
	int userState;

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		name = (TextView) findViewById(R.id.userName);
		phone = (TextView) findViewById(R.id.userName);
		hearDisable = (Switch) findViewById(R.id.hearDisable);
		speakDisable = (Switch) findViewById(R.id.speakDisable);
		
		i = getIntent();
		userName = i.getExtras().getString("userName");
		userPhone = i.getExtras().getString("userPhone");
		
		name.setText(userName);
		phone.setText(userPhone);

		hearDisable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					hearState = true;
				}else{
					hearState = false;
				}
				updateUserStateToPref();
				sendUserStateToDB();
			}
		});

		speakDisable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					speakState = true;
				}else{
					speakState = false;
				}
				updateUserStateToPref();
				sendUserStateToDB();
			}
		});
	}

	
	public void updateUserStateToPref(){
		
		if(hearState && speakState){ 		
			userState = UserState.NOT_DISABLED;
		} 
		else if(hearState && (!speakState)){ 
			userState = UserState.HEAR_DISABLED;
		} 
		else if((!hearState) && speakState){
			userState = UserState.SPEAK_DISABLED;
		} 
		else if((!hearState) && (!speakState)){
			userState = UserState.BOTH_DISABLED;
		}
		
		SharedPreferences preference = MainActivity.pref;
		SharedPreferences.Editor editor = preference.edit();
		
		editor.putInt("userState", userState);
		editor.commit();
	}
	
	
	public void sendUserStateToDB(){
		
	}
}
