package com.ttong.adapter;

import java.util.ArrayList;

import com.example.ttong.R;
import com.ttong.activity.CallLoadingActivity;
import com.ttong.activity.MainActivity;
import com.ttong.commnuication.ClientThread;
import com.ttong.model.UserData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainListViewAdapter extends BaseAdapter {

	Context context;
	ArrayList<UserData> data;
	
	public MainListViewAdapter(Context context, ArrayList<UserData> data){
		this.context = context;
		this.data = data;
	}
	
	@Override
	public int getCount() {
		if(data != null) return data.size();
		else return 0;
	}

	@Override
	public Object getItem(int position) {
		if(data != null) return data.get(position);
		else return null;
	}

	@Override
	public long getItemId(int position) {
		if(data != null) return data.size();
		else return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		final String name = data.get(position).getUserName();
		final String phone = data.get(position).getUserPhone();
		final int state = data.get(position).getUserState();
		
		if(v==null){
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			v = layoutInflater.inflate(R.layout.main_listview_cell, null);
		}
		
		if(data != null){
			ImageView iv = (ImageView) v.findViewById(R.id.user_info);
			
			if(state == 0) {
				iv.setImageResource(R.drawable.star_non);
			} else if(state == 1) {
				iv.setImageResource(R.drawable.star_hear);
			} else if(state == 2) {
				iv.setImageResource(R.drawable.star_speak);
			} else if(state == 3) {
				iv.setImageResource(R.drawable.star_both);
			}
			
			//iv.setImageDrawable(drawable);
			//iv.setImageResource(resId);
			
			TextView name_t = (TextView) v.findViewById(R.id.name);
			name_t.setText(name);
			
			TextView phone_t = (TextView) v.findViewById(R.id.phone_number);
			phone_t.setText(phone);
			
			ImageButton btn = (ImageButton) v.findViewById(R.id.btn_call);
			btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					SharedPreferences preference = ((MainActivity)context).pref;
					String myName = preference.getString("UserName", "no name");
					String myPhone = preference.getString("UserPhone", "no phone number");
					int myState = preference.getInt("userState", 0);
					
					ClientThread ct = ((MainActivity)context).clientThread;
					ct.sendDest(phone);
					ct.sendMyInfo(myName, myPhone, myState);
					
					Intent intent = new Intent(context, CallLoadingActivity.class);
					intent.putExtra("name", name);
					intent.putExtra("phone", phone);
					context.startActivity(intent);
					((MainActivity)context).finish();
				}
			});
		}
		
		return v;
	}

	public ArrayList<UserData> getData() {
		return data;
	}

	public void setData(ArrayList<UserData> data) {
		this.data = data;
	}

}
