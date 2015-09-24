package com.example.activity;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.ttong.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity{
	private final String SERVER_ADDRESS = "http://14.63.226.208";
	
	ListView list;
	ArrayList<String> data;
    ArrayAdapter<String> adapter;
	
	public static SharedPreferences pref; 
	SharedPreferences.Editor editor;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        pref = getSharedPreferences("TTongLogin", 0);
        editor = pref.edit();
        
        list = (ListView) findViewById(R.id.listView);
        data = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);
        
        checkLogin();
    }	

    public void checkLogin(){
    	if(!pref.getBoolean("Status", false)) {	// true=login, false=not login 
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
                                    data.add(new String("아무것도 검색되지 않았습니다."));
                                } else {
                                    for(int i = 0; i < namelist.size(); i++) {
                                        String str = namelist.get(i) + " - " + phone_number_list.get(i);
                                        data.add(str);
                                    }
                                }
                            } catch(Exception e) {
                               // Log.e("Error", e.getMessage());
                            } finally{
                                dialog.dismiss();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
    	}
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