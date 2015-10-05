package com.ttong.call_activity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import com.example.ttong.R;
import com.example.ttong.R.color;
import com.ttong.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import jay.media.MediaService;

// me : 1
// dest : 0

// me기준 : send streaming, receive text
public class C10Activity extends Activity{

	LinearLayout showText;
	Handler handler;
	
	private boolean isStarted = false;
	MediaService mMediaService = null;
	///// 미정인 값들 
	static String myIp, sendIp;
	int sendPort, recvPort;
	final int et_ec_buffer_size = 10;
	Intent i;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_only_text);
		
		showText = (LinearLayout) findViewById(R.id.showText);
		
		i = getIntent();
		sendIp = i.getExtras().getString("destIp");
		sendPort = i.getExtras().getInt("sendPort");
		recvPort = 54321;
		
		
		// for showing the partner information.
        Intent vsi = getIntent();
     	TextView callerName = (TextView) findViewById(R.id.callerName);
     	TextView callerPhone = (TextView) findViewById(R.id.callerPhone);
     	callerName.setText(vsi.getExtras().getString("destName"));
     	callerPhone.setText(vsi.getExtras().getString("destPhone"));
		
		
		// 이거 맞음?
		handler = new Handler(){
			public void handleMessage(Message msg){
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
				showText(bundle.getString("msg")+"\n");
				//textView.append();
			}
		};
		
		MainActivity.clientThread.setContext(this);
		startVS();
		
		// test!!!!!!!
		MainActivity.clientThread.changeHandler(handler);
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
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
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void stopVS(){
		if (isStarted == true) {
			mMediaService.stopAudio();
			isStarted = false;
		}
	}
	
	public void showText(String str){
		int dp_5 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, this.getResources().getDisplayMetrics());
		int dp_10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, this.getResources().getDisplayMetrics());
		int dp_7 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, this.getResources().getDisplayMetrics());
		
		TextView tv = new TextView(this);
		tv.setText(str);
		tv.setTextColor(color.Indigo8);
		tv.setPadding(dp_10, dp_5, 0, dp_5);
		tv.setGravity(Gravity.LEFT);
		tv.setTextSize(dp_7);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		tv.setLayoutParams(params);
		showText.addView(tv);
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
