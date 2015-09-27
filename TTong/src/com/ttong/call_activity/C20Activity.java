package com.ttong.call_activity;

import com.example.ttong.R;
import com.example.ttong.R.color;
import com.ttong.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

// me : 2
// dest : 0

// me기준 : send text, receive streaming		===>>> C01, C03, C21, C22, C23
public class C20Activity extends Activity implements OnClickListener {

	ImageButton btn_send, btn_stt;
	EditText editText;
	LinearLayout ll;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_edit_text);
		
		btn_stt = (ImageButton) findViewById(R.id.micBtn);
		btn_send = (ImageButton) findViewById(R.id.sendBtn);
		editText = (EditText) findViewById(R.id.textEt);
		ll = (LinearLayout) findViewById(R.id.showText);
		
		btn_stt.setEnabled(false);
		btn_send.setOnClickListener(this);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.call, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
////////////////////////// 연진이한테 물어보기!! 
		int id = item.getItemId();
		if (id == R.id.action_call_stop) {
			MainActivity.clientThread.send("StopCall ");
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void showText(String str){
		int dp_5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, this.getResources().getDisplayMetrics());
		int dp_10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
		
		TextView tv = new TextView(this);
		tv.setText(str);
		tv.setTextColor(color.Indigo8);
		tv.setPadding(0, dp_5, dp_10, dp_5);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, Gravity.RIGHT);
		tv.setLayoutParams(params);
		ll.addView(tv);
	}
}
