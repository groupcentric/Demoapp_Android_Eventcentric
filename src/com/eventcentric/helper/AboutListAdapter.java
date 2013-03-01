package com.eventcentric.helper;

import android.content.Context;
import com.eventcentric.helper.aboutapp;


import java.text.SimpleDateFormat;

class AboutListAdapter extends MyAboutListAdapter<aboutapp> {

    public AboutListAdapter(Context context) {
        super(context);
    }

    @Override
    String getPicurl(aboutapp item) {
        return item.getPicurl();
    }
    @Override
    String getTitle(aboutapp item) {
        return item.getTitle();
    }
    @Override
    String getSubtitle(aboutapp item) {
        return item.getSubtitle();
    }
    @Override
    String getURL(aboutapp item) {
        return item.getUrl();
    }

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getImage(aboutapp item) {
		// TODO Auto-generated method stub
		return null;
	}

	

    


}

