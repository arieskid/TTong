package com.ttong.commnuication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.ttong.activity.CallAskActivity;
import com.ttong.activity.CallLoadingActivity;
import com.ttong.activity.MainActivity;
import com.ttong.call_activity.C00Activity;
import com.ttong.call_activity.C01Activity;
import com.ttong.call_activity.C02Activity;
import com.ttong.call_activity.C03Activity;
import com.ttong.call_activity.C10Activity;
import com.ttong.call_activity.C11Activity;
import com.ttong.call_activity.C20Activity;
import com.ttong.call_activity.C22Activity;
import com.ttong.call_activity.C30Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ClientThread extends Thread{

	BufferedReader bufferReader;
	BufferedWriter bufferWriter;

	Socket client;
	Handler handler;
	Context context;

	
	int destState = 0;
	String destName;
	String destPhone;

	public ClientThread(Socket client, Handler handler){
		this.handler = handler;

		try{
			this.client = client;

			bufferReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			bufferWriter = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void changeHandler(Handler handler){
		this.handler = handler;
	}

	public void setContext(Context context){
		this.context = context;
	}

	//send my Information
	public void sendMyInfo(String myName, String myPhone, int myState){
		try{
			bufferWriter.write("MyName "+myName+"\n");
			bufferWriter.flush();
			bufferWriter.write("MyPhone "+myPhone+"\n");
			bufferWriter.flush();
			bufferWriter.write("MyState "+myState+"\n");
			bufferWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// send Destination's phone number
	public void sendDest(String phone){
		try {
			bufferWriter.write("dest "+phone+"\n");
			bufferWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 
	public void send(String text){
		try{
			System.out.println(text);
			bufferWriter.write(text+"\n");
			bufferWriter.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

//	public void changeActivity(String msg){
//
//		if(msg.startsWith("StartCall ")){
//
//		} else if(msg.startsWith("OkayCall ")){
//
//		}
//	}

	//諛쏄린
	public String receive(){
		String msg = null;
		try{
			while(true){
				msg = bufferReader.readLine();

				// when receive call
				if(msg.startsWith("StartCall ")){
					// change the activity according to it and its destination's state
					String[] arr = msg.split("/");
					//destState = Integer.valueOf(msg.substring(10, 11));
					destState = Integer.valueOf(arr[1]);
					destName = arr[2];
					destPhone = arr[3];
					
					Log.d("****", "test ct msg : "+msg);

					// 전화받을래??
					Intent i = new Intent(context, CallAskActivity.class);
					
					///////// 전화 건 사람의 ip 주소 넘겨주기
					String callerIp = "192.168.0.25";
					i.putExtra("callerIp", callerIp);
					
					i.putExtra("destState", destState);
					i.putExtra("destName", destName);
					i.putExtra("destPhone", destPhone);
					context.startActivity(i);
				}
				// when destination answer the call. - when i call and receiver answered.
				else if(msg.startsWith("OkayCall ")){
					// change the activity according to it and its destination's state
					
					String[] arr = msg.split("/");
					//destState = Integer.valueOf(msg.substring(10, 11));
					destState = Integer.valueOf(arr[1]);
					destName = arr[2];
					destPhone = arr[3];
					Intent i = null;
					int myState = MainActivity.pref.getInt("userState", 0);
					
					Log.d("***", "test ct my : "+String.valueOf(myState));
					Log.d("***", "test ct dest : "+String.valueOf(destState));
					
				
					switch(destState + (myState*10)){
					case 0:
						i = new Intent(context, C00Activity.class);
						Log.d("****", "test ct : 0");
						break;

					case 1:
						i = new Intent(context, C01Activity.class);
						Log.d("****", "test ct : 1");
						break;

					case 2:
						i = new Intent(context, C02Activity.class);
						Log.d("****", "test ct : 2");
						break;

					case 3:
						i = new Intent(context, C03Activity.class);
						Log.d("****", "test ct : 3");
						break;

					case 10: case 12:
						i = new Intent(context, C10Activity.class);
						Log.d("****", "test ct : 10");
						break;

					case 11: case 13:
						i = new Intent(context, C11Activity.class);
						Log.d("****", "test ct : 11");
						break;

					case 20: case 21:
						i = new Intent(context, C20Activity.class);
						Log.d("****", "test ct : 20");
						break;

					case 22: case 23:
						i = new Intent(context, C22Activity.class);
						Log.d("****", "test ct : 22");
						break;

					case 30: case 31: case 32: case 33:
						i = new Intent(context, C30Activity.class);
						Log.d("****", "test ct : 30");
						break;
					}
					
					//////////////////
					// 서버부터 상대방 ip받아서 통화화면으로 넘겨주기
					//////////////////
					String destIp = "192.168.0.24"; // 인이 droptop ip
					i.putExtra("destIp", destIp);
					i.putExtra("sendPort", 1988);
					i.putExtra("recvPort", 1989);
					i.putExtra("destName", destName);
					i.putExtra("destPhone", destPhone);
					msg = null;
					context.startActivity(i);
					
				}
				else if(msg.startsWith("StopCall ")){
					// return main activity
					client.close();
					
					if(context instanceof C00Activity){
						((C00Activity)context).stopVS();	
					}
					
					Intent i = new Intent(context, MainActivity.class);
					context.startActivity(i);
				}

				if(handler != null){
					Message m = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("msg", msg);
					m.setData(bundle);
					handler.sendMessage(m);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return msg;
	}

	// Calling to destination
	public void run(){
		super.run();
		receive();
	}
}