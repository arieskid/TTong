package com.ttong.call_activity;

import java.util.ArrayList;

import com.example.ttong.R;
import com.ttong.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

// me : 1
// dest : 1

// me기준 : send speech -> text(stt), receive text
public class C11Activity extends Activity implements OnClickListener {

	ImageButton btn_send;
	ImageButton btn_stt;
	EditText editText;
	
	SpeechRecognizer mRecognizer;
	Intent i;
	
	TextView textView;
	Handler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_c11);
		
		btn_stt = (Button)findViewById(R.id.btn_connect);
		btn_send = (Button)findViewById(R.id.btn_send);
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
		
		mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
		
		btn_send.setOnClickListener(this);
        btn_stt.setOnClickListener(this);
	}
	
	private RecognitionListener listener = new RecognitionListener() {
	       
        @Override
        public void onResults(Bundle results) {
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            editText.setText(rs[0].toString()); // 결과값 보여주기.
            System.out.println(editText.getText().toString());
        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }

		@Override
		public void onReadyForSpeech(Bundle params) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBeginningOfSpeech() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRmsChanged(float rmsdB) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBufferReceived(byte[] buffer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onEndOfSpeech() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onError(int error) {
			// TODO Auto-generated method stub
			
		}
    };

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_send){
			MainActivity.clientThread.send(editText.getText().toString());
			editText.setText("");
		}
		else if(v.getId()==R.id.btn_stt){
			mRecognizer.startListening(i);
		}
		
	}
}
