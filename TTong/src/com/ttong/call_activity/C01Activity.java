package com.ttong.call_activity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;

import com.example.ttong.R;
import com.example.ttong.R.color;
import com.ttong.activity.MainActivity;
import com.ttong.activity.SettingActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
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
import jay.media.LanAudioRecord;
import jay.media.MediaService;

// me : 0(not disabled)
// dest : 1(청각장애인)

// me기준 : send speech -> text(stt), receive voice streaming
public class C01Activity extends Activity implements OnClickListener {

	ImageButton btn_send, btn_stt;
	EditText editText;
	LinearLayout ll;
	TextView callerName, callerPhone;
	
	SpeechRecognizer mRecognizer;
	Intent i, vsi;
	
	private boolean isStarted = false;
	MediaService mMediaService = null;
	///// 미정인 값들 
	static String myIp, sendIp;
	int sendPort, recvPort;
	final int et_ec_buffer_size = 10;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_edit_text);
		
		LanAudioRecord.flag = false;
		
		btn_stt = (ImageButton) findViewById(R.id.micBtn);
		btn_send = (ImageButton) findViewById(R.id.sendBtn);
		editText = (EditText) findViewById(R.id.textEt);
		ll = (LinearLayout) findViewById(R.id.showText);
		
		// handler : sttTextComm.java
		
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(listener);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
        
        vsi = getIntent();
        sendIp = vsi.getExtras().getString("destIp");
		recvPort = vsi.getExtras().getInt("recvPort");
		sendPort = 12345;
		
		
		
		// for showing the partner information.
		callerName = (TextView) findViewById(R.id.callerName);
		callerPhone = (TextView) findViewById(R.id.callerPhone);
		callerName.setText(vsi.getExtras().getString("destName"));
		callerPhone.setText(vsi.getExtras().getString("destPhone"));
		
		
		
		
		btn_send.setOnClickListener(this);
        btn_stt.setOnClickListener(this);
        
        MainActivity.clientThread.setContext(this);
        startVS();
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
			
			stopVS();
			System.out.println("StopCall C01 Activity!");
			
			Intent i = new Intent(this, MainActivity.class);
			System.out.println("StopCall C01 Activity!2");
			startActivity(i);
			System.out.println("StopCall C01 Activity!3");
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
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
		if(v.getId()==R.id.sendBtn){
			String str = editText.getText().toString();
			editText.setText("");
			MainActivity.clientThread.send(str);
			showText(str);
		}
		else if(v.getId()==R.id.micBtn){
			// 누르고 나면 image 바꿔주기.
			Toast.makeText(this, "20초동안 말해주세요.", Toast.LENGTH_SHORT).show();
			mRecognizer.startListening(i);
		}
		
	}
	
	public void showText(String str){
		int dp_5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, this.getResources().getDisplayMetrics());
		int dp_10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
		int dp_7 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, this.getResources().getDisplayMetrics());
		
		TextView tv = new TextView(this);
		tv.setText(str);
		tv.setTextColor(color.Indigo8);
		tv.setPadding(0, dp_5, dp_10, dp_5);
		tv.setGravity(Gravity.RIGHT);
		tv.setTextSize(dp_7);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(params);
		ll.addView(tv);
	}
	
	public void stopVS(){
		LanAudioRecord.flag = true;
		
		if (isStarted == true) {
			mMediaService.stopAudio();
			System.out.println("StopCall C01Activity stopVS");
			isStarted = false;
		}
	}
	
	public void startVS(){
		try {
        	myIp = getLocalIPv4Address();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		Log.d("####", "test c00 myIp : "+myIp);
		mMediaService = new MediaService();
		
		String ipAddr = sendIp;
		int remotePort = sendPort;
		int localport = recvPort;

		if (isStarted == true) {
			Toast.makeText(this, "Already connected", Toast.LENGTH_SHORT).show();
			return;
		}
		
		// --> make the ip address into match form!!! 
		if (isIpAddr(ipAddr)) {
			int ec_buffer_pkgs = et_ec_buffer_size; 
			mMediaService.startAudio(ipAddr, 101, 8000, remotePort, localport, ec_buffer_pkgs);
			isStarted = true;

			/* 101:Speex 8:G711a 0:G711u 9:G722 */

			// audioWriter = new AudioWriter();
			// audioWriter.init(ipAddr, 4321);
			// audioWriter.start();
		} else {
			Toast.makeText(this, "ip illegal!! @" + ipAddr + " | try again!", Toast.LENGTH_LONG).show();
		}
	}
	
	/* Check the string if it is an legal ip address */  
	private boolean isIpAddr(String str) {
		// parameter: regular expression!!!!!!
		Pattern pattern = Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
											+ "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
		return pattern.matcher(str).matches();
	}
	
	private String getLocalIPv4Address() throws SocketException {
		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();
				if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
					return inetAddress.getHostAddress().toString();
				}
			}
		}
		return "null";
	}
}
