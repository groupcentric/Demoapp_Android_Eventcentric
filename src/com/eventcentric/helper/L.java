package com.eventcentric.helper;

import android.util.Log;

public final class L {

    public static boolean DEBUG = true;

    public static final void p(Object line) {
        p("", line);
    }

	public static final void p(String pfx, Object line) {
	    if (DEBUG)
	        Log.d("Shizzrl/" + pfx, String.valueOf(line));
	}
	
}
