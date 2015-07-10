package com.example.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.example.tab.ContactTab;
import com.example.tab.DialTab;
import com.example.tab.HistoryTab;
import com.example.tab.TtongTab;
import com.example.voicerecognition.R;

public class MainActivity extends TabActivity implements TabHost.OnTabChangeListener{
    TabHost tabHost;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = getTabHost();

        tabHost.setOnTabChangedListener(this);

        TabHost.TabSpec spec;
        Intent intent;

        // DialTab
        intent = new Intent().setClass(this, DialTab.class);
        spec = tabHost.newTabSpec("Dial").setIndicator("").setContent(intent);

        tabHost.addTab(spec);

        //TtongTab
        intent = new Intent().setClass(this, TtongTab.class);
        spec = tabHost.newTabSpec("Ttong").setIndicator("").setContent(intent);

        tabHost.addTab(spec);

        //HistoryTab
        intent = new Intent().setClass(this, HistoryTab.class);
        spec = tabHost.newTabSpec("History").setIndicator("").setContent(intent);

        tabHost.addTab(spec);

        //ContactTab
        intent = new Intent().setClass(this, ContactTab.class);
        spec = tabHost.newTabSpec("Contact").setIndicator("").setContent(intent);

        tabHost.addTab(spec);

        // default tab is dial tab
        tabHost.getTabWidget().setCurrentTab(0);

        // tab widget image
        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.dial_over);
        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.ttong);
        tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.history);
        tabHost.getTabWidget().getChildAt(3).setBackgroundResource(R.drawable.contact);

    }

    // tab changed
    public void onTabChanged(String tabID){
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++){
            if(i==0){
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.dial);
            }
            else if(i==1){
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.ttong);
            }
            else if(i==2){
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.history);
            }
            else if(i==3){
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.contact);
            }
        }

        // just information
        Log.i("tabs", "CurrentTab : " + tabHost.getCurrentTab());

        if(tabHost.getCurrentTab()==0){
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.dial_over);
        }
        else if(tabHost.getCurrentTab()==1){
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.ttong_over);
        }
        else if(tabHost.getCurrentTab()==2){
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.history_over);
        }
        else if(tabHost.getCurrentTab()==3){
            tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundResource(R.drawable.contact_over);
        }
    }
}