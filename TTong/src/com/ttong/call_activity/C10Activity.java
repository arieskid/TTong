package com.ttong.call_activity;

import com.example.ttong.R;
import com.ttong.activity.MainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

// me : 1
// dest : 0

// me기준 : send streaming, receive text
public class C10Activity extends Activity{

	TextView textView;
	Handler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_c10);
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
				textView.append(bundle.getString("msg")+"\n");
			}
		};
		
		// test!!!!!!!
		MainActivity.clientThread.changeHandler(handler);
	}
}
