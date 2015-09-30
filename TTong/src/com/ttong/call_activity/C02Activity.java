package com.ttong.call_activity;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Pattern;

import com.example.ttong.R;
import com.ttong.activity.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import jay.media.MediaService;

// me : 0(not disabled)
// dest : 2(언어장애인)

// me 기준 : send voice streaming, receive text->speech(tts)		====>>> same C00
public class C02Activity extends Activity implements OnInitListener, OnClickListener {
	
	TextToSpeech tts;
	Handler handler;
	
	TextView nameTv, phoneTv;
	Button btn;
	String name, phone;
	
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
		setContentView(R.layout.call_vs);
				
		nameTv = (TextView) findViewById(R.id.callName);
		phoneTv = (TextView) findViewById(R.id.callPhone);
		btn = (Button) findViewById(R.id.callStopBtn);
		
		Intent i = getIntent();
		name = i.getExtras().getString("destName");
		phone = i.getExtras().getString("destPhone");
		
		sendIp = i.getExtras().getString("destIp");
		sendPort = i.getExtras().getInt("sendPort");
		recvPort = 65432;
		
		nameTv.setText(name);
		phoneTv.setText(phone);
		btn.setOnClickListener(this);
		
		tts = new TextToSpeech(this, this);
		
		startVS();
		
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

	@Override
	public void onClick(View v) {
		MainActivity.clientThread.send("StopCall ");
		
		stopVS();
		
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
	
	public void stopVS(){
		if (isStarted == true) {
			mMediaService.stopAudio();
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
