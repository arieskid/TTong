package com.ttong.activity;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.ttong.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private final String SERVER_ADDRESS = "http://14.63.226.208";
	
	EditText nameET, phoneET;
	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		StrictMode.enableDefaults();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		nameET = (EditText) findViewById(R.id.name);
		phoneET = (EditText) findViewById(R.id.phone);
		btn = (Button) findViewById(R.id.okBtn);
		btn.setOnClickListener(new View.OnClickListener() { // 입력 버튼을 눌렀을 때

			public void onClick(View v) {				
				// TODO Auto-generated method stub
				if (nameET.getText().toString().equals("") || phoneET.getText().toString().equals("")) {					
					Toast.makeText(LoginActivity.this, "이름과 전화번호를 입력하세요!", Toast.LENGTH_SHORT).show();
					return;
				}
				
				runOnUiThread(new Runnable() {

					public void run() {
						SharedPreferences preference = MainActivity.pref;
						SharedPreferences.Editor editor = preference.edit();
						
						// TODO Auto-generated method stub
						String name = nameET.getText().toString();
						String phone = phoneET.getText().toString();

						try {
							URL url = new URL(
									SERVER_ADDRESS + "/ttong_insert.php?" + "name=" + URLEncoder.encode(name, "UTF-8")
											+ "&phone=" + URLEncoder.encode(phone, "UTF-8"));
							url.openStream();
							
							String result = getXmlData("insertresult.xml", "result"); 
							
							if (result.equals("1")) {
								Toast.makeText(LoginActivity.this, "가입을 축하합니다.", Toast.LENGTH_SHORT).show();
								
								nameET.setText("");
								phoneET.setText("");
								
								editor.putBoolean("LoginStatus", true);
								editor.putString("UserName", name);
								editor.putString("UserPhone", phone);
								editor.commit();
								
								Intent i = new Intent(LoginActivity.this, MainActivity.class);
								startActivity(i);
							} else 
								Toast.makeText(LoginActivity.this, "가입에 실패했습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
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
             
            while(eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG) {
                    if(xpp.getName().equals(str)) {
                        ret = xpp.nextText();
                    }
                }
                eventType = xpp.next();
            }
        } catch(Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
         
        return ret;
    }	
}
