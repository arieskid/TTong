package com.ttong.call_activity;

import com.example.ttong.R;
import com.ttong.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

// me : 0(not disabled)
// dest : 0(not disabled)

// only voice streaming
public class C00Activity extends Activity implements OnClickListener{

	TextView nameTv, phoneTv;
	Button btn;
	String name, phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_vs);
		
		nameTv = (TextView) findViewById(R.id.callName);
		phoneTv = (TextView) findViewById(R.id.callPhone);
		btn = (Button) findViewById(R.id.callStopBtn);
		
		Intent i = getIntent();
		name = i.getExtras().getString("destName");
		phone = i.getExtras().getString("destPhone");
		
		nameTv.setText(name);
		phoneTv.setText(phone);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
///////////////////////////// 연진이가 확인할 부분 - 서버에게 통화 종료 요청.
		MainActivity.clientThread.send("StopCall ");
		
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
}
