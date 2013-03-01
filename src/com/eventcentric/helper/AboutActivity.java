package com.eventcentric.helper;








import com.eventcentric.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.eventcentric.helper.aboutapp;
import com.eventcentric.helper.Util;
import com.eventcentric.ws.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



public class AboutActivity extends BaseActionListActivity implements View.OnClickListener{

    TextView headerlbl;
    private ImageButton imgEvents;
    private ImageButton imgGroups;
    private ImageButton imgAbout;
    private AboutListAdapter adapter;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       

        imgAbout = (ImageButton) findViewById(R.id.slider_btn_about);
        imgGroups = (ImageButton) findViewById(R.id.slider_btn_groups);
        imgEvents = (ImageButton) findViewById(R.id.slider_btn_events);
        imgAbout.setImageDrawable(null);
        imgAbout.setBackgroundResource(R.drawable.img_slidedown_about_selected);
        imgEvents.setOnClickListener(this);
        imgGroups.setOnClickListener(this);
        imgAbout.setOnClickListener(this);
        
        getaboutapps();
    } //end create

    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        
        case R.id.slider_btn_groups:
        	startActivity(new Intent(this, GroupsActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        	break;
        case R.id.slider_btn_events:
        	startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        	break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    
	@Override
	protected int getLayoutResId() {
		// TODO Auto-generated method stub
		 return R.layout.activity_about;
		
	}

	@Override
	public BaseAdapter createListAdapter() {
		return adapter = new AboutListAdapter(this);
		
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		String u = adapter.getItem(arg2).getUrl();
		if(u.length() > 0)
		{
			openAboutAppWebiew(u, adapter.getItem(arg2).getTitle());
        
		}
	}
	
	private void openAboutAppWebiew(String webURL, String header) {
        startActivity(new Intent(this, AboutAppWebView.class)
        .putExtra("web_url", webURL)
        .putExtra("header_text", header)
    );
	}
	
	void initAboutApps(List<aboutapp> apps) {
        adapter.setData(apps);
        

    }
	
	private void getaboutapps() {
        startTask(new WSGetAboutApps(this));
    }
	
	@Override
    public void onSuccess(BaseTask<?, ?, ?> task) {
        super.onSuccess(task);

       if (task instanceof WSGetAboutApps) {
            WSGetAboutAppsListResult appsListResult;
            appsListResult = ((WSGetAboutApps) task).getActualResult();
            aboutapp ap = new aboutapp();
            ap.setUrl("");
            appsListResult.appList.add(0,ap);
            
            initAboutApps(appsListResult.appList);
        }
	}


}