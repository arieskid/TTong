package Adapter;

import java.util.ArrayList;

import com.example.ttong.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MainListViewAdapter extends BaseAdapter{

	Context context;
	ArrayList<String> data;
	
	public MainListViewAdapter(Context context, ArrayList<String> data){
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
		
		if(v==null){
			LayoutInflater layoutInflater = LayoutInflater.from(context);
			v = layoutInflater.inflate(R.layout.cell_main_listview, null);
		}
		
		TextView tv = (TextView) v.findViewById(R.id.content);
		tv.setText(data.get(position));
		
		return v;
	}

	public ArrayList<String> getData() {
		return data;
	}

	public void setData(ArrayList<String> data) {
		this.data = data;
	}

}
