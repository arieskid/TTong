package com.ttong.call_activity;

import java.util.Locale;

import com.example.ttong.R;
import com.ttong.activity.MainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// me : 0(not disabled)
// dest : 2(언어장애인)

// me 기준 : send voice streaming, receive text->speech(tts)
public class C02Activity extends Activity implements OnInitListener {

	// voice streaming하는 것 같은 xml이 필요. = same C00 xml
	
	TextToSpeech tts;
	Handler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_c02);
				
		tts = new TextToSpeech(this, this);
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
				//textView.append(bundle.getString("msg")+"\n");
				//TTS
				speakOut(bundle.getString("msg"));
			}
		};
		
		// test!!!!!!!
		MainActivity.clientThread.changeHandler(handler);
	}
	
	private void speakOut(String text) {
        if (text.length() == 0) {
        	
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
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
