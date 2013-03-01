package com.eventcentric.ws;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WS29bShizzlrRegistrationSearchTowns extends WSListOfStringsTask {

    public WS29bShizzlrRegistrationSearchTowns(TaskContext taskContext, String searchPattern) {
	    super(taskContext);
	    addParameter("town", searchPattern);
	}

    @Override
    protected List<String> getResult(String[] result) throws JSONException {
    	JSONObject obj = new JSONObject(result[0]);
    	ArrayList<String> towns = new ArrayList<String>();
    	JSONArray array = obj.optJSONArray("rows");
    	if (array != null) {
	    	for (int i = 0; i < array.length(); i++)
	    		towns.add(array.getJSONObject(i).getString("town").replace("_", ", "));
    	}
    	return towns;
    };
}