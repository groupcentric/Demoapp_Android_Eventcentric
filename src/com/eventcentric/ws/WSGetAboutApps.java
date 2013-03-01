package com.eventcentric.ws;

import android.util.Log;

import com.eventcentric.helper.aboutapp;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class WSGetAboutApps extends WSBaseTask_GC<WSGetAboutAppsListResult>{

    public WSGetAboutApps(TaskContext taskContext) {
        super(taskContext);
        addParameter("userid", "1");
        
    }

    @Override
    protected WSGetAboutAppsListResult getResult(String[] data) throws Exception {
        WSGetAboutAppsListResult myresult = new WSGetAboutAppsListResult();
      //  ArrayList<Friend> friends = new ArrayList<Friend>();
      //  int L = data.length;
        JSONArray array = getArray(data[0]);
        
        if (array != null) {
            for (int i = 0; i < array.length(); i++)
                myresult.appList.add(WSUtils.aboutappListItemFromJson(array.getJSONObject(i)));
        }
        return myresult;
    }
}
