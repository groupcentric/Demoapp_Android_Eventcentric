package com.eventcentric.ws;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eventcentric.App;
import com.eventcentric.helper.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

class WSUtils {

    private static Matcher digitsMatcher = Pattern.compile(".*\\((\\d+)\\).*").matcher("");

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    static String formatDateTime(Date date) {
        return dateTimeFormat.format(date);
    }

    static String formatDate(Date date) {
        return dateFormat.format(date);
    }

   

    

    static aboutapp aboutappListItemFromJson(JSONObject obj) throws JSONException {
        aboutapp ap = new aboutapp();
        ap.setTitle(optString(obj, null, "name", "name", "name", ""));
        ap.setSubtitle(optString(obj, null, "subtitle", "subtitle", "subtitle", ""));
        ap.setPicurl(optString(obj, null, "pic", "pic", "pic", ""));
        ap.setUrl(optString(obj, null, "androidurl", "androidurl", "androidurl", ""));
        
        return ap;
    }

    

    static String optString(JSONObject obj, String fail, String... args) {
        String res = null;
        for (String s : args) {
            if ((res = obj.optString(s, null)) != null)
                break;
        }
        return res != null ? res : fail;
    }

    static int optInt(JSONObject obj, int fail, String... args) {
        int res = fail;
        for (String s : args) {
            if ((res = obj.optInt(s, fail)) != fail)
                break;
        }
        return res;
    }

    static long optLong(JSONObject obj, long fail, String... args) {
        long res = fail;
        for (String s : args) {
            if ((res = obj.optLong(s, fail)) != fail)
                break;
        }
        return res;
    }

    static double optDouble(JSONObject obj, double fail, String... args) {
        double res = fail;
        for (String s : args) {
            if ((res = obj.optDouble(s, fail)) != fail)
                break;
        }
        return res;
    }
}
