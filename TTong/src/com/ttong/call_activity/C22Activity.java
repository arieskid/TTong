package com.ttong.call_activity;

import java.util.Locale;

import com.example.ttong.R;
import com.example.ttong.R.color;
import com.ttong.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

// me : 2
// dest : 2

// me기준 : send text, receive text->speech(tts)
public class C22Activity extends Activity implements OnClickListener, OnInitListener {

	ImageButton btn_send, btn_stt;
	EditText editText;
	LinearLayout ll;
	
	TextToSpeech tts;
	Handler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_edit_text);
		
		btn_stt = (ImageButton) findViewById(R.id.micBtn);
		btn_send = (ImageButton) findViewById(R.id.sendBtn);
		editText = (EditText) findViewById(R.id.textEt);
		ll = (LinearLayout) findViewById(R.id.showText);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
		params.weight=0;
		btn_stt.setLayoutParams(params);
		btn_send.setOnClickListener(this);
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
		
		MainActivity.clientThread.changeHandler(handler);
	}
	
	private void speakOut(String text) {
        if (text.length() == 0) {
        	
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.sendBtn){
			String str = editText.getText().toString();
			editText.setText("");
			MainActivity.clientThread.send(str);
			showText(str);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.call, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_call_stop) {
			MainActivity.clientThread.send("StopCall ");
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	
	public void showText(String str){
		int dp_5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, this.getResources().getDisplayMetrics());
		int dp_10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
		int dp_15 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, this.getResources().getDisplayMetrics());
		
		TextView tv = new TextView(this);
		tv.setText(str);
		tv.setTextSize(dp_15);
		tv.setTextColor(color.Indigo8);
		tv.setPadding(0, dp_5, dp_10, dp_5);
		tv.setGravity(Gravity.RIGHT);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(params);
		ll.addView(tv);
	}
}
