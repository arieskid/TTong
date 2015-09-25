package com.ttong.call_activity;

import com.example.ttong.R;
import com.ttong.activity.MainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

// me : 3
// dest : 0

// me기준 : send text, receive text
public class C30Activity extends Activity implements OnClickListener {

	ImageButton btn_send;
	EditText editText;
	TextView textView;
	
	Handler handler;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_c30);
		
		btn_send = (ImageButton)findViewById(R.id.btn_send);
		editText = (EditText)findViewById(R.id.editText);
		textView = (TextView)findViewById();
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
				textView.append(bundle.getString("msg")+"\n");
			}
		};
		
		// test!!!!!!!
		MainActivity.clientThread.changeHandler(handler);
		
		btn_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_send){
			MainActivity.clientThread.send(editText.getText().toString());
			editText.setText("");
		}	
	}
}
