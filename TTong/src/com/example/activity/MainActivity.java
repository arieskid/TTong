package com.example.activity;


import java.util.ArrayList;

import com.example.ttong.R;

import Adapter.MainListViewAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity implements OnItemClickListener{
    
	ListView listView;
	MainListViewAdapter adapter;
	
	public static SharedPreferences pref; 
	SharedPreferences.Editor editor;
	ArrayList<String> arr;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        pref = getSharedPreferences("TTongLogin", 0);
        editor = pref.edit();
        
        listView = (ListView) findViewById(R.id.listView);
        adapter = new MainListViewAdapter(this, null);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        
        checkLogin();
    }

    public void checkLogin(){
    	if(!pref.getBoolean("Status", false)){	// true=login, false=not login 
    		Intent i = new Intent(MainActivity.this, LoginActivity.class);
    		startActivity(i);
    	}else{
    		setData();
    	}
    }
    
    public void setData(){
    	arr = new ArrayList<String>();
    	arr.add("professor");
    	arr.add("in");
    	arr.add("chancejin");
    	arr.add("dahyeon");
    	
    	adapter.setData(arr);
    	adapter.notifyDataSetChanged();
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, CallActivity.class);
		String str = adapter.getData().get(position);
		intent.putExtra("name", str);
		startActivity(intent);
	}
    
}