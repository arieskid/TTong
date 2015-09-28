package com.ttong.call_activity;

import com.example.ttong.R;
import com.example.ttong.R.color;
import com.ttong.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

// me : 3
// dest : 0

// me기준 : send text, receive text
public class C30Activity extends Activity implements OnClickListener {

	LinearLayout showText;
	ImageButton btn_send, btn_stt;
	EditText editText;
	
	Handler handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_edit_text);
		
		btn_stt = (ImageButton) findViewById(R.id.micBtn);
		btn_send = (ImageButton) findViewById(R.id.sendBtn);
		editText = (EditText) findViewById(R.id.textEt);
		showText = (LinearLayout) findViewById(R.id.showText);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
		params.weight=0;
		btn_stt.setLayoutParams(params);
		btn_send.setOnClickListener(this);
		
		MainActivity.clientThread.setContext(this);
		
		handler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
				String str = bundle.getString("msg")+"\n";
				//textView.append();
				showText(str, 1);
			}
		};
		
		// test!!!!!!!
		MainActivity.clientThread.changeHandler(handler);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.sendBtn){
			String str = editText.getText().toString();
			editText.setText("");
			MainActivity.clientThread.send(str);
			showText(str, 0);
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
	
	public void showText(String str, int flag){
		
		int dp_5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, this.getResources().getDisplayMetrics());
		int dp_10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
		int dp_15 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, this.getResources().getDisplayMetrics());
		
		TextView tv = new TextView(this);
		tv.setText(str);
		tv.setTextColor(color.Indigo8);
		tv.setTextSize(dp_15);
		LinearLayout.LayoutParams params = null;
		
		// send text
		if(flag == 0){
			tv.setPadding(0, dp_5, dp_10, dp_5);
			tv.setGravity(Gravity.RIGHT);
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		}
		// receive text
		else if(flag == 1){
			tv.setPadding(dp_10, dp_5, 0, dp_5);
			tv.setGravity(Gravity.LEFT);
			params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		}
		
		if(params != null)
			tv.setLayoutParams(params);
		
		showText.addView(tv);
	}
}
