package com.eventcentric;




import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eventcentric.helper.*;

import com.groupcentric.android.*;
//

/*EventDetails is a details activity of an Eventbrite event.
 * What's important to notice is the 'Share' button added to the activity.
 * The share button fires off the groupcentric share selector activity where a 
 * user can start or select a group of theirs to share the event into.
 * You can also see how the event details are passed to the share selector activity.
*/

public class EventDetails extends BaseActivity implements OnClickListener {

    
    private EventObject event;
    private TextView eventTitle, eventLocation, eventWhen;
    private ImageView eventImage;
    private ImageDownloader imgDownload;
    private WebView desc_webview;
    private TextView txtEventURLLabel, txtEventURL;
    private WebView map_webview;
    private static final SimpleDateFormat resultDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    //local variable to store your GroupcentricAPIKey needed if the user taps 'Share'
    private String GroupcentricAPIKey = "";
     
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        /*
         * EventDetails has a Share button that fires off a Share Selector activity in the Groupcentric SDK.
         * When calling the intent to the Share Selector activity, your Groupcentric APIKey must be passed with it.
         * So grab your Groupcentric APIKey now from the assets/groupcentric.properties file and save it in this activity
         * in case the user taps the Share button
         */
        try {
        	AssetManager am = this.getAssets();
        	InputStream is;
			is = am.open("groupcentric.properties");
	        Properties properties = new Properties();
	        properties.load(is);
	        GroupcentricAPIKey = properties.getProperty("groupcentric.apikey");
        } catch (IOException e) {}
        

        setContentView(R.layout.activity_eventother_details);

        imgDownload = new ImageDownloader();
        eventImage = (ImageView) findViewById(R.id.item__image);
        desc_webview = (WebView) findViewById(R.id.desc_webview);
        eventTitle = (TextView) findViewById(R.id.item__title);
        eventLocation = (TextView) findViewById(R.id.table_detail__where_value);
        eventWhen = (TextView) findViewById(R.id.table_detail__when_value);
        txtEventURLLabel = (TextView) findViewById(R.id.txt_lbl_website);
        txtEventURL = (TextView) findViewById(R.id.txt_website_url);
        map_webview = (WebView) findViewById(R.id.map_webview);
        map_webview.setOnClickListener(this);

        findViewById(R.id.layout_map_row).setOnClickListener(this);
        findViewById(R.id.layout_event_map_row).setOnClickListener(this);

        getIntent();
        event = getIntent().getParcelableExtra("EVENT");
       
        initUI();

       //find the share button and set its onclick listener
         ((ImageButton) findViewById(R.id.header_btn_add)).setImageResource(R.drawable.btn_share);
         findViewById(R.id.header_btn_add).setVisibility(View.VISIBLE);
         findViewById(R.id.header_btn_add).setOnClickListener(this);
         
         
    } 

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
	        case R.id.header_btn_add:
	        	/*
	        	 * A user has just tapped the 'Share' button within an Event Details activity. 
	        	 * This will start the Groupcentric gc_ShareSelector activity from the Groupcentric SDK.
	        	 * This Share Selector activity allows a user to select which of their groups they'd like
	        	 * to share the Event to.
	        	 * For a general object like an Event that has a title, subtitle, date, etc, you
	        	 * must denote it is MessageType 4 and include (even if blank "") the following:
	        	 * ObjectTitle, ObjectSubtitle, ObjectImage, ObjectDate, ObjectDetails, ObjectURL.
	        	 * You must always pass your APIKey too.
	        	 * 
	        	 * When this activity is started and the user selects a group (or starts a new one) it
	        	 * will bring them into the group chat details and NOT back to this current Event Details activity.
	        	 */
	        	//to share this event as a type 4 object:
	        	startActivity(new Intent(this, gc_ShareSelector.class)
	            		.putExtra("APIKey", GroupcentricAPIKey)
	            		.putExtra("MessageType", "4")
	                    .putExtra("ObjectTitle", event.getName())
	                    .putExtra("ObjectSubtitle", event.getLocation() + " "+event.getStreet() + " "+ event.getTown())
	                    .putExtra("ObjectImage", event.getImage())
	                    .putExtra("ObjectDate", event.getDate())
	                    .putExtra("ObjectDetails", event.getDescription())
	                    .putExtra("ObjectURL", event.getEventURL()) 
	            );
	        	//if you wanted to share this event as a type 3 object, so when user taps on it within the group chat that it just opens up a webpage
	        	  //type 3 objects require ObjectTitle, ObjectImage, and ObjectURL   (ObjectImage may be blank "")
	        	/*startActivity(new Intent(this, gc_ShareSelector.class)
        		.putExtra("APIKey", GroupcentricAPIKey)
        		.putExtra("MessageType", "3")
                .putExtra("ObjectTitle", event.getName())
                .putExtra("ObjectImage", event.getImage())
                .putExtra("ObjectURL", event.getEventURL())
        		);*/
	        break;
            case R.id.layout_map_row:
                openMyMap();
                break;
            case R.id.layout_event_map_row:
                openMyMap();
                break;
        }
    }
    
  //rendering the event details
    private void initUI() {
        eventImage.setAdjustViewBounds(true);
        eventImage.setMaxHeight(150);
        eventImage.setMaxWidth(150);
        imgDownload.download(event.getImage(), eventImage);

        eventTitle.setText(Html.fromHtml(event.getName()));
        
        if (event.getLocation()==null)
            event.setLocation("");
        if (event.getEventAddress()==null)
            event.setEventAddress("");
        if (event.getTown()==null)
            event.setTown("");
        if (event.getState()==null)
            event.setState("");

        String strLocation = "";
        if (event.getLocation().length()>0)
            strLocation = event.getLocation() + "\n";

        if (event.getEventAddress().length()>0)
             strLocation = strLocation  + event.getEventAddress() + "\n";

        if (event.getTown().length()>0)
             strLocation = strLocation  + event.getTown();
        if (event.getState().length()>0)
             strLocation = strLocation + ", "+event.getState();
        eventLocation.setText(strLocation);

        try
        {
            if (event.getDate()==null)
            {
                event.setDate((formatToDate(event.getStartDate())));
            }
        } 
        catch (Exception ex)  {}

        desc_webview.getSettings().setJavaScriptEnabled(true);
        desc_webview.loadDataWithBaseURL(null, event.getDescription(), "text/html", "UTF-8", null);
        findViewById(R.id.layout_event_desc_row).setVisibility(View.VISIBLE);

        if (event.getDescription().length()==0){
           findViewById(R.id.lyt_desc_sep).setVisibility(View.GONE);
           findViewById(R.id.layout_event_desc_row).setVisibility(View.GONE);
           findViewById(R.id.layout_desc_row).setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(event.getStartDate()))
            eventWhen.setText(event.getStartDate());

        if (!TextUtils.isEmpty(event.getEndDate()))
            eventWhen.setText(event.getStartDate() + " - " + event.getEndDate());

        txtEventURLLabel.setText("Website");
        String strEventWebsite = "";

        if (!TextUtils.isEmpty(event.getEventURL()))
            strEventWebsite=event.getEventURL();

        

        if (strEventWebsite.length()>0)
            txtEventURL.setText(Html.fromHtml("<font color='#2a4874'>" + strEventWebsite +"</font>"));

            txtEventURL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEventURL =event.getEventURL();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(strEventURL));
                try{
                  startActivity(intent);
                }
                catch (Exception ex){}
            }
        });

        String strLat = "";
        String strLon = "";
        if (!TextUtils.isEmpty(event.getEventLat())) {
            strLat = event.getEventLat();
        }
        if (!TextUtils.isEmpty(event.getEventLon())) {
            strLon = event.getEventLon();
        }

        if (strLat.length() + strLon.length() > 0) {
            String strMapURL = "http://maps.googleapis.com/maps/api/staticmap?&zoom=16&size=300x300&maptype=roadmap&markers=color:blue%7Clabel:A%7C" + strLat + "," + strLon + "&sensor=false";
            map_webview.loadUrl(strMapURL);
            findViewById(R.id.layout_map_row).setVisibility(View.VISIBLE);
            findViewById(R.id.layout_event_map_row).setVisibility(View.VISIBLE);
            String strTapToOpen = "<font color='#2a4874'>(Tap to open)</font>";
            ((TextView) findViewById(R.id.layout_map_text)).setText(Html.fromHtml(strTapToOpen));
        } else {
            findViewById(R.id.layout_map_row).setVisibility(View.GONE);
            findViewById(R.id.layout_map_sep).setVisibility(View.GONE);
            findViewById(R.id.layout_event_map_row).setVisibility(View.GONE);
        }

        final ImageView imgPoweredBy = (ImageView) findViewById(R.id.img_powered_by);
        imgDownload.download(event.getEventLogo(), imgPoweredBy);

    }

    private void openMyMap()
    {
      if (event.getEventLat()!=null & event.getEventLon()!= null)
      {
        String strURL =
                "http://maps.google.com/maps?q=" + event.getEventLat() + ",+" + event.getEventLon();

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
        Uri.parse(strURL));
        
        try
        {
             startActivity(intent);
        }
        catch (Exception ex){}

      }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //@Override
    protected int getHeaderViewResId() {
        return R.layout.header_logo_slider_no_tab_clickable;
    }

    private Date formatToDate(String strPassedDate) {
        Date formattedDate = null;
        try {
            formattedDate = resultDateFormat.parse(strPassedDate);
        } catch (ParseException e) {      
        }
        return formattedDate;
    }
}
