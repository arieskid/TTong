package com.ttong.call_activity;

import com.example.ttong.R;
import com.ttong.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

// me : 2
// dest : 0

// me기준 : send text, receive streaming
public class C20Activity extends Activity implements OnClickListener {

	ImageButton btn_send;
	EditText editText;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_c20);
		
		btn_send = (ImageButton)findViewById(R.id.btn_send);
		editText = (EditText)findViewById(R.id.editText);
		
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
