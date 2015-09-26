package com.ttong.call_activity;

import com.example.ttong.R;
import com.example.ttong.R.color;
import com.ttong.activity.MainActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

// me : 1
// dest : 0

// me기준 : send streaming, receive text
public class C10Activity extends Activity{

	LinearLayout showText;
	Handler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_edit_text);
		
		showText = (LinearLayout) findViewById(R.id.showText);
		
		// 이거 맞음?
		handler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
				showText(bundle.getString("msg")+"\n");
				//textView.append();
			}
		};
		
		// test!!!!!!!
		MainActivity.clientThread.changeHandler(handler);
	}
	
	public void showText(String str){
		int dp_5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, this.getResources().getDisplayMetrics());
		int dp_10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
		
		TextView tv = new TextView(this);
		tv.setText(str);
		tv.setTextColor(color.Indigo8);
		tv.setPadding(dp_10, dp_5, 0, dp_5);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, Gravity.LEFT);
		tv.setLayoutParams(params);
		showText.addView(tv);
	}
}
