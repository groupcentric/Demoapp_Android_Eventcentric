package com.eventcentric;





import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



import android.os.Bundle;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.View;
import android.widget.ImageButton;
import com.eventcentric.helper.*;

import android.support.v4.app.FragmentActivity;

import com.groupcentric.android.gc_GroupsListFragment;
import com.groupcentric.android.gc_NotificationsButton;

/*
 * This activity will be the list of the users Groups provided through the Groupcentric SDK.
 * 
 * It must extend FragmentActivity
 * It must import android.support.v4.app.Fragment and android.support.v4.app.FragmentActivity;
 * 
 * In the onCreate method it must also access the assets/groupcentric.properties file to grab
 * your groupcentric.apikey and pass it to the GroupsListFragment
 * 
 * The layout for this activity must include the groups list fragment. 
 * If you look at the layout activity_groups you will see it at the bottom of the layout 
 * beneath the header and sub navigational bars.
 * 
 * <fragment class="com.groupcentric.android.gc_GroupsListFragment"
        android:layout_below="@+id/layout_navmenu"
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
 */

//this activity must extend FragmentActivity to get the GroupsListFragment to work
public class GroupsActivity extends FragmentActivity implements View.OnClickListener, com.groupcentric.android.gc_GroupsListFragment.GroupcentricNotificationsButtonListener {
	
	private ImageButton imgEvents;
	private ImageButton imgGroups;
    private ImageButton imgAbout;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Necessary to make sure the device can register with Urban Airship and the Groupcentric push reciever has been started
        App app = (App) getApplication();
		
        //notice this layout contains the nav/header like the rest of the app, but
        //it also includes a fragment for an in-app notifications button and a fragment for the user's groups list
        setContentView(R.layout.activity_groups);
        
        //menu listeners
        imgAbout = (ImageButton) findViewById(R.id.slider_btn_about);
        imgGroups = (ImageButton) findViewById(R.id.slider_btn_groups);
        imgEvents = (ImageButton) findViewById(R.id.slider_btn_events);
        imgEvents.setOnClickListener(this);
        imgGroups.setOnClickListener(this);
        imgAbout.setOnClickListener(this);
        imgGroups.setImageDrawable(null);
        imgGroups.setBackgroundResource(R.drawable.img_slidedown_groups_selected);
        
        
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
	
	@Override
    public void onClick(View v) { //just some menu stuff for this app
        switch (v.getId()) {
        
        case R.id.slider_btn_events:
        	startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        	break;
        case R.id.slider_btn_about:
        	startActivity(new Intent(this, AboutActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        	break;
        
        }
        
    }
	
	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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