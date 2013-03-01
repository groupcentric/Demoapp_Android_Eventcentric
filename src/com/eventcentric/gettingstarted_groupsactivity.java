package com.eventcentric; //<--your package name

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import android.os.Bundle;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.View;
import android.widget.ImageButton;
import android.support.v4.app.FragmentActivity;

import com.groupcentric.android.gc_GroupsListFragment;
import com.groupcentric.android.gc_NotificationsButton;

public class gettingstarted_groupsactivity extends FragmentActivity implements com.groupcentric.android.gc_GroupsListFragment.GroupcentricNotificationsButtonListener {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        
        /*
         * The groupcentric.properties file in your assets folder must be accessed here to get 
         * your apikey and pass it to the Groupcentric SDK, in this case the GroupsListFragment
         */
        try {
        	AssetManager am = this.getAssets();
        	InputStream is;
			is = am.open("groupcentric.properties");
	        Properties properties = new Properties();
	        properties.load(is);
	        String groupcentricAPIKey = properties.getProperty("groupcentric.apikey");
	        gc_GroupsListFragment fragment = (gc_GroupsListFragment)getSupportFragmentManager().findFragmentById(R.id.list);
			fragment.setGroupcentricAPIKey(groupcentricAPIKey);
        } catch (IOException e) {}
	}
	
	
	//required for Groupcentric in-app notifications
	@Override
	public void onGroupcentricNotificationsCheck(int updatecount) {
		gc_NotificationsButton gc_notifications = (gc_NotificationsButton)
        getSupportFragmentManager().findFragmentById(R.id.gc_notifications_btn);
		//pass the in-app notifications count to the button fragment
		gc_notifications.updateButton(updatecount);
		
	}

}