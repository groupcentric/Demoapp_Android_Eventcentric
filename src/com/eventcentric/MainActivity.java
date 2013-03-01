package com.eventcentric;





import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/*
 * 
db to store gcid and apikey

then think thru whole share into gc process
 */

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.eventcentric.helper.*;


//The main activity is a list of Eventbrite events.

public class MainActivity extends BaseListActivity implements OnItemClickListener, View.OnClickListener, LocationListener {

	protected ListAdapterGroup <EventObject> m_adapter;
	public String town;
	private GetEventbriteTask getEventbriteTask;
	private ImageButton btnEventSearch;
    private EditText edtSearchTxt;
    private static final int Select_Location = 3;
    private ImageButton imgEvents;
    private ImageButton imgGroups;
    private ImageButton imgAbout;
    private static final int DLG_GPS_DISABLED = 12345;
    private LocationManager locationManager;
    private ProgressBar progressBar;
    private String provider = "";
    
    private static int cat = 1;
    private static int pg = 1;
    
    private ImageButton
    btnCategoryFood,
    btnCategoryCoffee,
    btnCategoryNightlife,
    btnCategoryFeatured;
    
    private class TmpState {
        String LastLat = "";
        String LastLong = "";
        int useGPSLocation;
        String SelectedTownLat = "";
        String SelectedTownLon = "";
    }

    private TmpState tmpState;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Necessary to make sure the device can register with Urban Airship and the Groupcentric push reciever has been started
        App app;
		app = (App) getApplication();
        
        //Change city button listener
        final ImageView btnChange = (ImageView) findViewById(R.id.changeTownBtn);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, LocationSelector.class);
                startActivityForResult(intent, Select_Location);
               
            }
        });
        imgAbout = (ImageButton) findViewById(R.id.slider_btn_about);
        imgGroups = (ImageButton) findViewById(R.id.slider_btn_groups);
        imgEvents = (ImageButton) findViewById(R.id.slider_btn_events);
        imgEvents.setImageDrawable(null);
        imgEvents.setBackgroundResource(R.drawable.img_slidedown_events_selected);
        imgEvents.setOnClickListener(this);
        imgGroups.setOnClickListener(this);
        imgAbout.setOnClickListener(this);
        
        btnCategoryFeatured = (ImageButton) findViewById(R.id.btn_scroll_featured);
       // btnCategoryFeatured.setBackgroundResource(R.drawable.btn_place_category_bg);

        btnCategoryFood = (ImageButton) findViewById(R.id.btn_scroll_food);
        //btnCategoryFood.setBackgroundResource(R.drawable.btn_place_category_bg);

        btnCategoryCoffee = (ImageButton) findViewById(R.id.btn_scroll_coffee);
        //btnCategoryCoffee.setBackgroundResource(R.drawable.btn_place_category_bg);

        btnCategoryNightlife = (ImageButton) findViewById(R.id.btn_scroll_nightlife);
       // btnCategoryNightlife.setBackgroundResource(R.drawable.btn_place_category_bg);

        btnCategoryFeatured.setOnClickListener(this);
        btnCategoryFood.setOnClickListener(this);
        btnCategoryCoffee.setOnClickListener(this);
        btnCategoryNightlife.setOnClickListener(this);
        
        btnEventSearch = (ImageButton) findViewById(R.id.btnSearchEvents);
        btnEventSearch.setOnClickListener(this);
        edtSearchTxt = (EditText) findViewById(R.id.edtEventSearchTxt);
        edtSearchTxt.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onClick(btnEventSearch);
                    return true;
                }
                return false;
            }
        });
        town = "&city=New+York,+NY"; 
        
       
        
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
		criteria  = new Criteria();
		criteria.setAccuracy(Criteria.NO_REQUIREMENT);
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		provider = locationManager.getBestProvider(criteria, true);
        
        //tmpState = (TmpState) getLastNonConfigurationInstance2();
        if (tmpState == null) {
            tmpState = new TmpState();
            tmpState.LastLat = "";
            tmpState.LastLong = "";
        }
        
        String strTown = app.getLastViewedTown();
        if(strTown == null || strTown.equals("")) 
        {
        	Log.i("asdasdf","Asdfasdfsad");
        	startListening();
        }
        else
        {
        	strTown = "&city="+strTown.replaceAll(" ", "%20");
            fireEventbrite(cat, "","",strTown);
        }
      
        
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_events;
    }

    @Override
	public
    ListAdapterGroup<EventObject> createListAdapter() {
        return m_adapter = new TownEventsAdapter(this);
    }
    

    static class TownEventsAdapter extends ListAdapterGroup<EventObject> {

        public TownEventsAdapter(Context context) {
            super(context);
        }

       

        @Override
		public
        String getImage(EventObject item) {
            return item.getImage();
        }

        @Override
		public
        long getId(EventObject item) {
            return item.getId();
        }


        


        @Override
		public
        String getTitle(EventObject item) {
            return item.getName();
        }

        @Override
		public
        String getSubtitle(EventObject item) {
            return "@ " + item.getLocation();
        }

        
        @Override
		public
        String getPlanDate(EventObject item) {
            SimpleDateFormat myEventDate = new SimpleDateFormat("EEE M/d");
            if (item.getId() == -1) {
                myEventDate = new SimpleDateFormat("EEE M/d");
            }


            if (item.getDate() != null) {
                StringBuilder strMyPlanDate = new StringBuilder(myEventDate.format(item.getDate()));

                if (item.getEndDate() != null && !(TextUtils.isEmpty(item.getEndDate()))) {
                    String startdate = myEventDate.format(item.getDate());
                    String enddate = item.getEndDate().substring(0, item.getEndDate().indexOf(":"));
                    enddate = enddate.substring(0, enddate.lastIndexOf(" ")).trim();
                    if (startdate.equalsIgnoreCase(enddate))
                        return startdate;
                    else
                        return startdate + " - " + enddate;

                } else
                	return strMyPlanDate.toString();
            } else {
                return "";
            }

        }

		@Override
		public long getItemId(int position) {
			return 0;
		}
    }

    
    
   public void fireEventbrite(int cat, String strLat, String strLon, String strCityState) {
       
        getEventbriteTask = new GetEventbriteTask();//this);
        String strSearch = edtSearchTxt.getText().toString();
        getEventbriteTask.execute(new String[]{String.valueOf(cat), strSearch, strLat, strLon, strCityState});
    }
    
    private class GetEventbriteTask extends AsyncTask<String, Void, String> {
        
        List<EventObject> eventbriteData = new ArrayList<EventObject>();
        private MainActivity mActivity;

        private final ProgressDialog myEventbriteDialog = new ProgressDialog(MainActivity.this);//mActivity);

        // can use UI thread here
        protected void onPreExecute() {
            //bGetEventbriteIsRunning = true;
            myEventbriteDialog.setMessage("Connecting to Eventbrite..");
            myEventbriteDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    getEventbriteTask.cancel(true);
                    //bGetEventbriteIsRunning = false;
                }
            });
            myEventbriteDialog.show();


        }
        

        // automatically done on worker thread (separate from UI thread)
        protected String doInBackground(String... args) {
            eventbriteData.clear();
            String strEventbriteKey = "D3TJBGJ3AMQ5PA3P6A";//getString(R.string.eventbrite_key);
            String categ = args[0];
            String strSearchFor = args[1];
            strSearchFor = strSearchFor.replaceAll(" ", "%20");
            String strLat = args[2];
            String strLon = args[3];
            String strCityState = args[4];
            Date anotherCurDate = new Date(); 
            eventbriteData = GetEventbriteDataSearch.getFeed(categ, strEventbriteKey, strSearchFor, strLat, strLon, anotherCurDate, strCityState);
            return "";

        }

        // can use UI thread here
        protected void onPostExecute(String strResult) {
         
        	
            if (this.myEventbriteDialog.isShowing()) {
                try {
                    this.myEventbriteDialog.dismiss();
                } catch (Exception e) {
                    // nothing
                }
            }
        	m_adapter.setData((ArrayList<EventObject>)eventbriteData);// = new ListAdapterGroup(m_adapter, R.layout.row_group,	(ArrayList<TownEvent>) eventbriteData);
        	m_adapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	startActivity(new Intent(this, EventDetails.class).putExtra("EVENT", m_adapter.getItem(position)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        
        case R.id.btnSearchEvents:
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSearchTxt.getWindowToken(), 0);
            final Button btnSelectedLocation = (Button) findViewById(R.id.btnSelectedLocation);
            town = "&city="+btnSelectedLocation.getText();
            town = town.replaceAll(" ", "%20");
            fireEventbrite(cat, "","",town);
            break;
        
        case R.id.slider_btn_groups:
        	startActivity(new Intent(this, GroupsActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        	break;
        case R.id.slider_btn_about:
        	startActivity(new Intent(this, AboutActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
        	break;
        
        
	    case R.id.btn_scroll_featured:
	        CategorySelected(v);
	        break;
	    case R.id.btn_scroll_food:
	        CategorySelected(v);
	        break;
	    case R.id.btn_scroll_coffee:
	        CategorySelected(v);
	        break;
	    case R.id.btn_scroll_nightlife:
	        CategorySelected(v);
	        break;
        }
    }
    private void CategorySelected(View v) {
        pg=1;
        
        if (v.getId() == R.id.btn_scroll_nightlife) {
        	btnCategoryNightlife.setImageResource(R.drawable.scroll_nightlife_selected);
            cat = 1;
        } else {
        	btnCategoryNightlife.setImageResource(R.drawable.scroll_nightlife_unselected);
        }

        if (v.getId() == R.id.btn_scroll_featured) {
        	btnCategoryFeatured.setImageResource(R.drawable.scroll_featured_selected);
            cat = 2;
        } else {
        	btnCategoryFeatured.setImageResource(R.drawable.scroll_featured_unselected);
        }

        if (v.getId() == R.id.btn_scroll_food) {
        	btnCategoryFood.setImageResource(R.drawable.scroll_food_selected);
            cat = 3;
        } else {
        	btnCategoryFood.setImageResource(R.drawable.scroll_food_unselected);
        }

        if (v.getId() == R.id.btn_scroll_coffee) {
        	btnCategoryCoffee.setImageResource(R.drawable.scroll_coffee_selected);
            cat = 4;
        } else {
        	btnCategoryCoffee.setImageResource(R.drawable.scroll_coffee_unselected);
        }
        
        String strTown = app.getLastViewedTown();
        if(strTown == null || strTown.equals("")) 
        {
        	startListening();
        }
        else
        {
        	strTown = "&city="+strTown.replaceAll(" ", "%20");
            fireEventbrite(cat, "","",strTown);
        }

    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Select_Location) {
            if (resultCode == RESULT_OK) {
                String strTown = data.getStringExtra("TOWNSELECT");
                
                final Button btnSelectedLocation = (Button) findViewById(R.id.btnSelectedLocation);
                btnSelectedLocation.setText(strTown);
                
                if(strTown.equals("Nearby"))
                {
                	
                	startListening();
                }
                
                else
                {
	                //app.getLastViewedTown());
	                
	                strTown = "&city="+strTown.replaceAll(" ", "%20");
	                fireEventbrite(cat,"","",strTown);
                }
                
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        stopListening();
    }

    private void startListening() {
        //If we have cached LAT & Long lets use this
        //  We are doing this specifically for the events view otherwise every time the user clicks on a new day
        //  it will request new location -  This will be reset if the user exists events for fires off location selector
    	if (tmpState.LastLat.length() > 0 & tmpState.LastLong.length() > 0) {
    		fireEventbrite(cat,tmpState.LastLat, tmpState.LastLong,"");
            return;
        }
    	
         progressBar = (ProgressBar) findViewById(R.id.header_progress);
        progressBar.setVisibility(View.VISIBLE);
        locationManager.requestLocationUpdates(provider, 5000, 0, this);
    }

    private void stopListening() {
        locationManager.removeUpdates(this);
        progressBar = (ProgressBar) findViewById(R.id.header_progress);
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onLocationChanged(Location location) {
        tmpState.LastLat = Double.toString(location.getLatitude());
        tmpState.LastLong = Double.toString(location.getLongitude());

        fireEventbrite(cat,Double.toString(location.getLatitude()), Double.toString(location.getLongitude()),"");
        stopListening();
    }

    @Override
    public void onProviderDisabled(String provider) {
        progressBar = (ProgressBar) findViewById(R.id.header_progress);
        progressBar.setVisibility(View.GONE);
        showDialog(DLG_GPS_DISABLED);
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    private void launchGPSOptions() {
        tmpState.useGPSLocation = -1;
        final ComponentName toLaunch = new ComponentName("com.android.settings", "com.android.settings.SecuritySettings");
        final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(toLaunch);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, 0);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DLG_GPS_DISABLED) {
            return new AlertDialog.Builder(this).setTitle("Device GPS Disabled").setMessage("Your device GPS is disabled in system settings (Location & Security).  Would you like to change this settings?")
                    .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            launchGPSOptions();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }
        return super.onCreateDialog(id);
    }
	
}
