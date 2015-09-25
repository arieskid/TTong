
package com.ttong.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
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
	
	
    public void onCreate(Bundle savedInstanceState) {
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

    public void connect(){
    	
    	final Context context = this;
    	
		thread = new Thread(){
    		
			public void run(){
				super.run();
				
				try{
					client = new Socket(ip,port);			
					clientThread = new ClientThread(client, null);
					clientThread.setContext(context);
					clientThread.start();
				}catch(UnknownHostException e){
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		};
		thread.start();
		
	}
    
    
    
    public void checkLogin(){
    	
    	if(!pref.getBoolean("LoginStatus", false)) {	// true=login, false=not login 
    		Intent i = new Intent(MainActivity.this, LoginActivity.class);
    		startActivity(i);
    	} else {
    		final Handler handler = new Handler();
            runOnUiThread(new Runnable() {
                 
                public void run() {
                    // TODO Auto-generated method stub
                    final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "불러오는중.....", "잠시만 기다려주세요.");
                     
                    handler.post(new Runnable() {
                         
                        public void run() {
                            // TODO Auto-generated method stub
                            try {
                                data.clear(); //반복적으로 누를경우 똑같은 값이 나오는 것을 방지하기 위해 data를 클리어함
                                URL url = new URL(SERVER_ADDRESS + "/ttong_search.php");
                                url.openStream(); //서버의 serarch.php파일을 실행함
                     
                                ArrayList<String> namelist = getXmlDataList("searchresult.xml", "name");//name 태그값을 읽어 namelist 리스트에 저장
                                ArrayList<String> phone_number_list = getXmlDataList("searchresult.xml", "phone_number"); //price 태그값을 읽어 prica 리스트에 저장
                                if(namelist.isEmpty()) {
                                	UserData ud = new UserData("default","000-0000-0000",0);
                                	data.add(ud);
                                } else {
                                    for(int i = 0; i < namelist.size(); i++) {
                                    	////////////////////////////////////////////////////////
                                    	UserData ud = new UserData(namelist.get(i), phone_number_list.get(i), 0);
                                        data.add(ud);
                                    }
                                }
                            } catch(Exception e) {
                               // Log.e("Error", e.getMessage());
                            } finally{
                                dialog.dismiss();
                                
                                connect();
                                
                                adapter.setData(data);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
    	}
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
			Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
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

}