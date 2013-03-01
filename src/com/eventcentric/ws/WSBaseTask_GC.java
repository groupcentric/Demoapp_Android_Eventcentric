package com.eventcentric.ws;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.eventcentric.helper.L;

public abstract class WSBaseTask_GC<T> extends BaseTask<Void, Void, T>{

	private static Matcher methodResolver = Pattern.compile("(WS\\d+[a-z]*)([A-Z].*)").matcher("");

	protected static String NAMESPACE = "http://tempuri.org/";
    //Blachat - Needs to be changed BACK -
	protected static String HOST = "www.groupcentric.com";
	protected static String URL = "http://www.groupcentric.com/GroupWS.asmx";

//    protected static String HOST = "www.portugalson.com";
//	protected static String URL = "http://" + HOST + "/ShizzlrWS.aspx";


	protected final SoapObject request;
	private final String methodName = "getAppsForAbout_Android";
	class MyHandler extends Handler {
	    public MyHandler(Looper l) {
	        super(l);
        }
	    @Override
	    public void handleMessage(Message msg) {
	        boolean res = cancel(true);
	        //System.out.println("CANCELED " + res);
	            
	    }
	}
	private MyHandler handler;

    public WSBaseTask_GC(TaskContext taskContext) {
	    super(taskContext);
	   // methodName = getMethodName();
	    request = new SoapObject(NAMESPACE, methodName);
	    handler = new MyHandler(taskContext.getContext().getMainLooper());
	}

	protected String getMethodName() {
		methodResolver.reset(getClass().getSimpleName());
		methodResolver.find();
		String method = methodResolver.group(2);
		//System.out.println("TASK [" + methodResolver.group(1)+"] " + method);
		return method;
	};

	protected String getSoapAction() {
		StringBuilder sb = new StringBuilder(NAMESPACE);
		sb.append(methodName);
		return sb.toString();
	}

    protected CharSequence getSafeParam(CharSequence s) {
        return TextUtils.isEmpty(s) ? "0" : s; 
    }

	protected void addParameter(String name, Object value) {
//	    if (value instanceof CharSequence)
//	        value = getSafeParam((CharSequence) value);
//	    name = name.toLowerCase();
	    //System.out.println(name + " :=> " + value);
	    request.addProperty(name, value);
	}

	@Override
	final protected T getResult(Void... params) throws Exception {
	    return getResult(connect());
	}

	protected abstract T getResult(String[] result) throws Exception;

	protected void addClassMappings(SoapSerializationEnvelope envelope) {
	}

	protected static JSONObject getRow(String s) throws JSONException {
	    JSONArray obj = getArray(s);
	    return obj != null ? obj.getJSONObject(0) : null;
	}

    protected static JSONArray getArray(String s) throws JSONException {
        return new JSONObject(s).optJSONArray("rows");
    }

	protected String[] connect() throws IOException, XmlPullParserException {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        addClassMappings(envelope);

        final HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        androidHttpTransport.debug = isDebug();
        handler.sendEmptyMessageDelayed(0, 45 * 1000);
        androidHttpTransport.call(getSoapAction(), envelope);
        //System.out.println("url:" + URL);
        //System.out.println("SoapAction: " + getSoapAction());
       // System.out.println("envelope: " );

        handler.removeMessages(0);
        if (androidHttpTransport.debug)
        	System.out.println("DUMP:" + androidHttpTransport.responseDump);

        Object response = envelope.getResponse();

        String[] result = null;

        // the whole idea seems fishy to me - soap has way too much overhead for a mobile device, 
        // and the best thing is that there is a json in it, i don't understand it. 
        // if i should be honest - i've never worked with soap before, so maybe this is normal, but i seriously doubt it,
        // however all this tricky, and maybe heavy-weight code is here, so it can be changed at later time
        if (response instanceof SoapObject) {
        	SoapObject obj = (SoapObject) response;
        	System.err.println("attr count " + obj.getAttributeCount());
//        	for (int i = 0; i < obj.getAttributeCount(); i++) {
//        		Object attr = obj.getAttribute(i);
//        		System.out.println(attr);
//        	}
        	int L = obj.getPropertyCount();
        	System.err.println("prop count " + L);
        	result = new String[L];
        	for (int i = 0; i < L; i++) {
        		    result[i] = obj.getProperty(i).toString();



//        		PropertyInfo info = new PropertyInfo();
//        		obj.getPropertyInfo(i, info);
        	}
        } else if (response instanceof SoapPrimitive) {
        	result = new String[] {response.toString()};

        }

/*        for (int i = 0; i < result.length; i++)
            L.p("WS RESULT(" + i + ")", result[i]);*/
        return result;
	}

	boolean isDebug() {
		return false;
	}

   @Override
    protected void onCancelled() {
        taskResult.response.setErrorMessage("Connection Timeout....");
        finish(null);
    }
}
