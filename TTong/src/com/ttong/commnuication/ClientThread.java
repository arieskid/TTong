package com.ttong.commnuication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class ClientThread extends Thread{

	BufferedReader bufferReader;
	BufferedWriter bufferWriter;
	
	Socket client;
	Handler handler;
	
	int destState = 0;
	
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
	
	//諛쏄린
	public String receive(){
		String msg = null;
		try{
			while(true){
				msg = bufferReader.readLine();
				
				destState = Integer.valueOf(msg.substring(10, 11));
				
				// when receive call
				if(msg.startsWith("StartCall ")){
					// change the activity according to it and its destination's state
					
					// 잔화받을래??
					
					
					switch(destState){
						case 0:
							
							break;
						case 1:
							
							break;
						case 2:
							
							break;
						case 3:
							break;
					}
					send("OkayCall ");
				}
				// when destination answer the call. - when i call and receiver answered.
				else if(msg.startsWith("OkayCall ")){
					// change the activity according to it and its destination's state
					switch(destState){
						case 0:
							
							break;
						case 1:
							
							break;
						case 2:
							
							break;
						case 3:
							break;
					}
				}
				else if(msg.startsWith("StopCall ")){
					client.close();
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