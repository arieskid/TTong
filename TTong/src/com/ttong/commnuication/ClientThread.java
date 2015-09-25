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
	public void sendMyInfo(String myName, String myPhone){
		try{
			bufferWriter.write("MyName "+myName+"\n");
			bufferWriter.flush();
			bufferWriter.write("MyPhone "+myPhone+"\n");
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

	public void changeActivity(String msg){

		if(msg.startsWith("StartCall ")){

		} else if(msg.startsWith("OkayCall ")){

		}
	}


	//諛쏄린
	public String receive(){
		String msg = null;
		try{
			while(true){
				msg = bufferReader.readLine();

				cha();

				// when receive call
				if(msg.startsWith("StartCall ")){
					// change the activity according to it and its destination's state
					String[] arr = msg.split("/");
					//destState = Integer.valueOf(msg.substring(10, 11));
					destState = Integer.valueOf(arr[1]);
					destName = arr[2];
					destPhone = arr[3];

					// 전화받을래??
					Intent i = new Intent(context, CallAskActivity.class);
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

					Intent i;
					int myState = MainActivity.pref.getInt("myState", 0);

					switch(destState + (myState*10)){
					case 0:
						i = new Intent(context, C00Activity.class);
						break;

					case 1:
						i = new Intent(context, C01Activity.class);
						break;

					case 2:
						i = new Intent(context, C02Activity.class);
						break;

					case 3:
						i = new Intent(context, C03Activity.class);
						break;

					case 10: case 12:
						i = new Intent(context, C10Activity.class);
						break;

					case 11: case 13:
						i = new Intent(context, C11Activity.class);
						break;

					case 20: case 21:
						i = new Intent(context, C20Activity.class);
						break;

					case 22: case 23:
						i = new Intent(context, C22Activity.class);
						break;

					case 30: case 31: case 32: case 33:
						i = new Intent(context, C30Activity.class);
						break;

						i.putExtra("destName", destName);
						i.putExtra("destPhone", destPhone);
						context.startActivity(i);

					}
				}
				else if(msg.startsWith("StopCall ")){
					// return main activity
					client.close();
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