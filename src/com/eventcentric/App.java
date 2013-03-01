package com.eventcentric;

//urbanairship classes


import com.groupcentric.android.gc_CustomUANotification;
import com.eventcentric.db.DB;
import com.eventcentric.db.DatabaseManager;
import com.eventcentric.helper.Town;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;
import com.urbanairship.push.PushPreferences;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import java.util.HashMap;

public class App extends Application {
	
	private DatabaseManager dbMgr;
	//required to use Groupcentric SDK
	public static final String IncomingGCPushMSG = "incomingGCPushMSG";

	@Override
	public void onCreate() {
		
		dbMgr = new DatabaseManager(this);
		
		//this starts the Urban Airship push notification registration
		UAirship.takeOff(this);
		gc_CustomUANotification nb = new gc_CustomUANotification();
		PushManager.shared().setNotificationBuilder(nb);
		PushManager.enablePush();
		//set the reciever for when a push notification is sent to this app through urban airship
		PushManager.shared().setIntentReceiver(com.groupcentric.android.gc_UAIntentReceiver.class);
		registerReceiver(UAReceiver, new IntentFilter(IncomingGCPushMSG));
		
		Town myTown = new Town("Boston, MA");
		getDatabaseManager().saveTown(myTown);
		myTown = new Town("New York, NY");
		getDatabaseManager().saveTown(myTown);
	}

	

    public BroadcastReceiver UAReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          // If we want to do anything on the app level when pushes are received do it here
        }
    };
    
    public DatabaseManager getDatabaseManager() {
        return dbMgr;
    }
    
    public void setLastViewedTown(String town) {
        storeString(DB.LAST_TOWN_VIEWED, town);
    }

    public String getLastViewedTown() {
        return getStoredString(DB.LAST_TOWN_VIEWED);
    }
    
    private void storeString(String key, String value) {
        //System.out.println(key + " => " + value);
        objects.put(key, value);
        dbMgr.putString(key, value);
    }
    
    private String getStoredString(String key) {
        if (objects.containsKey(key))
            return (String) objects.get(key);
        String res = dbMgr.getString(key, null);
        if (res != null)
            objects.put(key, res);
        return res;
    }
    
    private final HashMap<String, Object> objects = new HashMap<String, Object>();
    
    public Object getObject(String key) {
        return objects.get(key);
    }
    
    public Object putObject(String key, Object object) {
        return objects.put(key, object);
    }
	
}
