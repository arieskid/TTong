package com.ttong.call_activity;

import java.util.ArrayList;
import java.util.Locale;

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
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

// me : 0(not disabled)
// dest : 3(청각+언어장애인)

// me기준 : send speech -> text(stt), receive text -> speech(tts)
public class C03Activity extends Activity implements OnClickListener, OnInitListener {

	// same C01 xml
	
	ImageButton btn_send;
	ImageButton btn_stt;
	EditText editText;
	
	TextToSpeech tts;
	Handler handler;
	
	SpeechRecognizer mRecognizer;
	Intent i;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_c03);
		
		btn_stt = (Button)findViewById(R.id.btn_connect);
		btn_send = (Button)findViewById(R.id.btn_send);
		editText = (EditText)findViewById(R.id.editText);
		
		tts = new TextToSpeech(this, this);
		
		// handler : sttTextComm.java
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
				//textView.append(bundle.getString("msg")+"\n");
				//TTS
				speakOut(bundle.getString("msg"));
			}
		};
		
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);
        
        MainActivity.clientThread.changeHandler(handler);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
		
		btn_send.setOnClickListener(this);
        btn_stt.setOnClickListener(this);
	}
	
	private void speakOut(String text) {
        if (text.length() == 0) {
        	
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }

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
	
	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS) {
            //Setting speech language
            int result = tts.setLanguage(Locale.KOREA);
            //If your device doesn't support language you set above
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                //Cook simple toast message with message
                Toast.makeText(this, "Language not support", Toast.LENGTH_LONG).show();
                Log.e("TTS", "Language is not supported");
            }
            //Enable the button - It was disabled in main.xml (Go back and Check it)
            else {
            }
            //TTS is not initialized properly
        } else {
            Toast.makeText(this, "TTS Initilization Failed", Toast.LENGTH_LONG).show();
            Log.e("TTS", "Initilization Failed");
        }
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
		
		super.onDestroy();
	}
}
