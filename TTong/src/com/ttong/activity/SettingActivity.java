package com.ttong.activity;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.example.registration.Registration_Form;
import com.example.ttong.R;
import com.ttong.model.UserState;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//만약 다시 로그인했을 때 상태가 매번 0이 될텐데...;;
public class SettingActivity extends Activity {

	private final String SERVER_ADDRESS = "http://14.63.226.208";
	
	TextView name, phone;
	Switch hearDisable, speakDisable;
	Button logoutBtn;
	
	Intent i;
	String userName, userPhone;

	boolean hearState, speakState;
	int userState;

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		name = (TextView) findViewById(R.id.userName);
		phone = (TextView) findViewById(R.id.userName);
		hearDisable = (Switch) findViewById(R.id.hearDisable);
		speakDisable = (Switch) findViewById(R.id.speakDisable);
		logoutBtn = (Button) findViewById(R.id.logoutBtn);
		
		i = getIntent();
		userName = i.getExtras().getString("userName");
		userPhone = i.getExtras().getString("userPhone");
		
		name.setText(userName);
		phone.setText(userPhone);

		hearDisable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					hearState = true;
				}else{
					hearState = false;
				}
				updateUserStateToPref();
				sendUserStateToDB();
			}
		});

		speakDisable.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					speakState = true;
				}else{
					speakState = false;
				}
				updateUserStateToPref();
				sendUserStateToDB();
			}
		});
		
		logoutBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences preference = MainActivity.pref;
				SharedPreferences.Editor editor = preference.edit();
				
				editor.putBoolean("LoginStatus", false);
				editor.putString("UserName", "no name");
				editor.putString("UserPhone", "no phone number");
				editor.commit();
			}
		});
	}

	
	public void updateUserStateToPref(){
		
		if(hearState && speakState){ 		
			userState = UserState.NOT_DISABLED;
		} 
		else if(hearState && (!speakState)){ 
			userState = UserState.HEAR_DISABLED;
		} 
		else if((!hearState) && speakState){
			userState = UserState.SPEAK_DISABLED;
		} 
		else if((!hearState) && (!speakState)){
			userState = UserState.BOTH_DISABLED;
		}
		
		SharedPreferences preference = MainActivity.pref;
		SharedPreferences.Editor editor = preference.edit();
		
		editor.putInt("userState", userState);
		editor.commit();
	}
	
	
	public void sendUserStateToDB(){    
		runOnUiThread(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				String phone_number = userPhone;
				String is_disabled = String.valueOf(userState);

				try {
					URL url = new URL(SERVER_ADDRESS + "/ttong_updateInfo.php?" 
									+ "phone_number=" + URLEncoder.encode(phone_number, "utf-8") 
									+ "&is_disabled=" + URLEncoder.encode(is_disabled, "utf-8")); 
					
					url.openStream();

					String result = getXmlData("insertresult.xml", "result"); 
					
					if (result.equals("1")) { // result 태그값이 1일때 성공
						Toast.makeText(SettingActivity.this, "상태 정보를 변경했습니다.", Toast.LENGTH_SHORT).show();

					} else // result 태그값이 1이 아닐때 실패
						Toast.makeText(SettingActivity.this, "상태 정보 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private String getXmlData(String filename, String str) { //태그값 하나를 받아오기위한 String형 함수
        String rss = SERVER_ADDRESS + "/result/";
        String ret = "";
         
        try { //XML 파싱을 위한 과정
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            URL server = new URL(rss + filename);
            InputStream is = server.openStream();
            xpp.setInput(is, "utf-8");
             
            int eventType = xpp.getEventType();
             
            while(eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG) {
                    if(xpp.getName().equals(str)) { //태그 이름이 str 인자값과 같은 경우
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
