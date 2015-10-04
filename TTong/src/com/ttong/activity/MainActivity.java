
package com.ttong.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.ttong.R;
import com.ttong.adapter.MainListViewAdapter;
import com.ttong.commnuication.ClientThread;
import com.ttong.model.UserData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private final String SERVER_ADDRESS = "http://14.63.226.208";

	ListView list;
	ArrayList<UserData> data;
	MainListViewAdapter adapter;

	static final String ip = "14.63.226.208";
	static final int port = 8080;

	Socket client;
	public static ClientThread clientThread;
	Thread thread;

	public static SharedPreferences pref; 
	SharedPreferences.Editor editor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		StrictMode.enableDefaults();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pref = getSharedPreferences("TTongLogin", 0);
		editor = pref.edit();

		list = (ListView) findViewById(R.id.listView);
		data = new ArrayList<UserData>();
		adapter = new MainListViewAdapter(this, null);
		list.setAdapter(adapter);

		checkLogin();
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(this, SettingActivity.class);
			i.putExtra("userName", pref.getString("UserName", "no name"));
			i.putExtra("userPhone", pref.getString("UserPhone", "no phone number"));
			startActivity(i);
			return true;
		}
		else if (id == R.id.action_refresh) {
			getDataFromDB();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void checkLogin(){   	
		if(!pref.getBoolean("LoginStatus", false)) {	// true=login, false=not login 
			Log.d("####", "login 안됬음");
			Intent i = new Intent(MainActivity.this, RegisterActivity.class);
			startActivity(i);
			finish();
		} else {
			Log.d("####", "login 성공 후");
			updateIpOnDB();
			getDataFromDB();
			connect();
		}
	}

	public void connect(){
		Log.d("####", "connect");
		final Context context = this;
		thread = new Thread(){
			public void run(){
				super.run();
				try{
					client = new Socket(ip,port);
					clientThread = new ClientThread(client, null);
					clientThread.setContext(context);
					clientThread.start();

					TelephonyManager telManager = (TelephonyManager) getApplicationContext()
							.getSystemService(getApplicationContext().TELEPHONY_SERVICE);
					String phoneNum = telManager.getLine1Number();
					if(phoneNum != null)
						phoneNum = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, 7) + "-" + phoneNum.substring(7, 11);
					else phoneNum="456-7891-1234";

					clientThread.send("MyPhone "+phoneNum);

				}catch(UnknownHostException e){
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}

	public void getDataFromDB(){

		Log.d("####", "getDataFromDB");

		final Handler handler = new Handler();
		TelephonyManager telManager = (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
		String phoneNum = telManager.getLine1Number();
		final String userPhone;
		if(phoneNum != null)
			userPhone = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, 7) + "-" + phoneNum.substring(7, 11);
		else userPhone = "456-7891-1234";

		runOnUiThread(new Runnable() {

			public void run() {
				final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "불러오는중.....", "잠시만 기다려주세요.");
				handler.post(new Runnable() {
					public void run() {
						try {
							data.clear(); //반복적으로 누를경우 똑같은 값이 나오는 것을 방지하기 위해 data를 클리어함
							URL url = new URL(SERVER_ADDRESS + "/ttong_search.php");
							url.openStream(); //서버의 serarch.php파일을 실행함

							ArrayList<String> namelist = getXmlDataList("searchresult.xml", "name");
							ArrayList<String> phone_number_list = getXmlDataList("searchresult.xml", "phone_number");
							ArrayList<String> user_state = getXmlDataList("searchresult.xml", "is_disabled");

							if(namelist.isEmpty()) {
								UserData ud = new UserData("default","000-0000-0000",0);
								data.add(ud);
							} else {
								for(int i = 0; i < namelist.size(); i++) {
									if(!phone_number_list.get(i).equals(userPhone)) {
										UserData ud = new UserData(namelist.get(i), phone_number_list.get(i), Integer.parseInt(user_state.get(i)));
										data.add(ud);
									} else {
										final int userState = pref.getInt("userState", 0);

										ImageView iv = (ImageView) findViewById(R.id.my_info);

										if(userState == 0) {
											iv.setImageResource(R.drawable.star_non);
										} else if(userState == 1) {
											iv.setImageResource(R.drawable.star_hear);
										} else if(userState == 2) {
											iv.setImageResource(R.drawable.star_speak);
										} else if(userState == 3) {
											iv.setImageResource(R.drawable.star_both);
										}

										//iv.setImageDrawable(drawable);
										//iv.setImageResource(resId);

										TextView name_t = (TextView)findViewById(R.id.myName);
										name_t.setText(pref.getString("UserName", "default name"));

										TextView phone_t = (TextView) findViewById(R.id.myPhone_number);
										phone_t.setText(pref.getString("UserPhone", "000-0000-0000"));
									}
								}
							}
						} catch(Exception e) {
							Log.e("Error","error");
						} finally{
							dialog.dismiss();
							adapter.setData(data);
							adapter.notifyDataSetChanged();
						}
					}
				});
			}
		});
	}



	private ArrayList<String> getXmlDataList(String filename, String str) { // 태그값 여러 개를 받아오기 위한 ArrayList<string>형 변수
		String rss = SERVER_ADDRESS + "/result/";
		ArrayList<String> ret = new ArrayList<String>();

		try { // XML 파싱을 위한 과정
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			URL server = new URL(rss + filename);
			InputStream is = server.openStream();
			xpp.setInput(is, "UTF-8");

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(str)) { // 태그 이름이 str 인자값과 같은 경우
						ret.add(xpp.nextText());
					}
				}
				eventType = xpp.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
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


	public void updateIpOnDB(){
		runOnUiThread(new Runnable() {

			public void run() {

				TelephonyManager telManager = (TelephonyManager) getApplicationContext()
						.getSystemService(getApplicationContext().TELEPHONY_SERVICE);
				String mySubnetIP="";

				if(telManager != null){
					String phoneNum = telManager.getLine1Number();
					try {
						mySubnetIP = getLocalIPv4Address();
					} catch (SocketException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if(phoneNum != null)
						phoneNum = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, 7) + "-" + phoneNum.substring(7, 11);
					else phoneNum="456-7891-1234";

					try {
						URL url = new URL(SERVER_ADDRESS + "/ttong_updateIP.php?" + "phone_number="
								+ URLEncoder.encode(phoneNum, "UTF-8") + "&ip_address_sub=" + URLEncoder.encode(mySubnetIP, "UTF-8"));
						url.openStream();

						String result = getXmlData("updateipresult.xml", "result");

						if (result.equals("1")) {
							Toast.makeText(MainActivity.this, "update IP", Toast.LENGTH_SHORT).show();

						} else
							Toast.makeText(MainActivity.this, "update IP failed", Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private String getXmlData(String filename, String str) {
		String rss = SERVER_ADDRESS + "/result/";
		String ret = "";

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			URL server = new URL(rss + filename);
			InputStream is = server.openStream();
			xpp.setInput(is, "UTF-8");

			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					if (xpp.getName().equals(str)) {
						ret = xpp.nextText();
					}
				}
				eventType = xpp.next();
			}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return ret;
	}
}