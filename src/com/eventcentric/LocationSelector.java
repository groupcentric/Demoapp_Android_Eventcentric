package com.eventcentric;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.eventcentric.db.DB;
import com.eventcentric.helper.ManageTowns;
import com.eventcentric.helper.ReadWritePrefs;
import com.eventcentric.helper.BaseTaskActivity;
import com.eventcentric.helper.Town;
import com.eventcentric.helper.Util;
import com.eventcentric.ws.*;

import java.util.List;

public class LocationSelector extends BaseTaskActivity implements OnClickListener, OnCheckedChangeListener, OnItemClickListener {


    private ListView lv;
    private TownAdapter townAdapter;
    private AutoCompleteTextView townName;
    //    private static final int MENU_REMOVE = 0;
//    private static final int DLG_REMOVE_TOWN = 65756;
    private static final int DLG_ENABLE_GPS = 1234;

    //   private ProgressBar progressBar;
    public boolean mBroadcasting;
    int posTown2Remove;
    //Blachat 03/09/2011

    private boolean isFromMakeAPlan = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }


    @Override
    public void onSuccess(BaseTask<?, ?, ?> task) {
        /*if (task instanceof WS153AddTownByName_GetTownInfo) {
            Town town  = ((WS153AddTownByName_GetTownInfo) task).getActualResult();
            String s = townName.getEditableText().toString();
            Town myTown = new Town(s);
            if (town != null) {
                myTown.setId(town.getId());
                myTown.setLatitude(town.getLatitude());
                myTown.setLongitude(town.getLongitude());
                app.getDatabaseManager().saveTown(myTown);
                townAdapter.getCursor().requery();
                searchText.setText("");
                //Blachat 03/09/2011    -- If Managing from plan push new town to plan
                if (isFromMakeAPlan) {
                    setResult(RESULT_OK, new Intent().putExtra(EXTRA_TOWN, s.toString()));
                    finish();
                }

            } else {
                Util.longToast(this, R.string.common__fail);
            }
        } else if (task instanceof WS32RemoveTown) {
            int res = ((WS32RemoveTown) task).getActualResult();
            if (res == 0) {
                Util.shortToast(this, R.string.common__fail);
            } else {
                app.getDatabaseManager().removeTown(res);
                townAdapter.getCursor().requery();
            }
        } else {
            arrayAdapter = new ArrayAdapter<String>(this, getLayoutResId(), ((WS29bShizzlrRegistrationSearchTowns) task).getActualResult());
            townName.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            townName.showDropDown();
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.townlist_nearby:

                ReadWritePrefs rwPrefs = new ReadWritePrefs();
                String strGPSEnabled = rwPrefs.readWriteUserSetting(this, "Read", "GPS_ENABLED", "");

                if (strGPSEnabled.contentEquals("1")) {
                    long townID = -1;
                    setResult(RESULT_OK, new Intent()
                            .putExtra(EXTRA_TOWN, getString(R.string.common__nearby))
                            .putExtra("EXTRA_TOWNID", townID)
                    );
                    finish();
                } else {
                    showDialog(DLG_ENABLE_GPS);
                }
                break;    */
            case R.id.townlist_nearby_holder:

                ReadWritePrefs rwPrefs = new ReadWritePrefs();
                String strGPSEnabled = rwPrefs.readWriteUserSetting(this, "Read", "GPS_ENABLED", "");

                if (strGPSEnabled.contentEquals("1")) {
                    long townID = -1;
                    setResult(RESULT_OK, new Intent()
                            .putExtra("EXTRA_TOWN", "Nearby")
                            .putExtra("EXTRA_TOWNID", townID)
                            .putExtra("TOWNSELECT", "Nearby")
                    );
                    app.setLastViewedTown("");
                    finish();
                } else {
                    showDialog(DLG_ENABLE_GPS);
                }
                break;
        }
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        // Get the info on which item was selected
//        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
//        menu.setHeaderTitle(townAdapter.getTownName(info.position));
//        menu.add(Menu.NONE, MENU_REMOVE, Menu.NONE, R.string.manage_towns__remove);
//    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long id) {
//        openContextMenu(view);
        //Blachat 03/09/2011    -- If Managing from plan we need to see what user is selecting

        setResult(RESULT_OK, new Intent()
                .putExtra("EXTRA_TOWN", townAdapter.getTownName(pos).toString())
                .putExtra("EXTRA_TOWNID", townAdapter.getItemId(pos))
                .putExtra("TOWNSELECT", townAdapter.getTownName(pos).toString())
        );
        app.setLastViewedTown(townAdapter.getTownName(pos).toString());
        //Util.longToast(this, "My plan type is + " +  app.getLastViewedTown());
        finish();

    }


   /* @Override
    List<String> getResult(BaseTask<?, ?, ?> task) {
        return ((WS29bShizzlrRegistrationSearchTowns) task).getActualResult();
    }*/

    
    void initUI() {

        //                     -
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // isFromMakeAPlan = extras.getBoolean(FromMakeAPlan, false);
        }

        setContentView(R.layout.activity_location_selector);
        lv = (ListView) findViewById(android.R.id.list);
        Cursor c = app.getDatabaseManager().getTownsCursor();
        startManagingCursor(c);
        townAdapter = new TownAdapter(this, c);
        lv.setAdapter(townAdapter);
        //townName = (AutoCompleteTextView) findViewById(R.id.manage_towns__search);
        //townName.setAdapter(arrayAdapter);
        //searchText = townName;
        lv.setOnItemClickListener(this);
//        registerForContextMenu(lv);
        // progressBar = (ProgressBar) findViewById(R.id.header_progress);
        //findViewById(R.id.townlist_nearby).setOnClickListener(this);
        findViewById(R.id.townlist_nearby_holder).setOnClickListener(this);

        // ImageView image = (ImageView) findViewById(R.id.satellite);
        // Animation fly = AnimationUtils.loadAnimation(this, R.anim.satellite_fly);

        ReadWritePrefs rwPrefs = new ReadWritePrefs();
        String strGPSEnabled = rwPrefs.readWriteUserSetting(this, "Read", "GPS_ENABLED", "");
        if (strGPSEnabled.contentEquals("1")) {
            //  fly.setRepeatMode(2);
            //  image.startAnimation(fly);
        }

        //No more header button
//        findViewById(R.id.item__addtownbtn).setVisibility(View.VISIBLE);
        final ImageView btnAddTown = (ImageView) findViewById(R.id.item__addtownbtn);
        btnAddTown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LocationSelector.this, ManageTowns.class));
            }
        });

        final RelativeLayout lyt_location_cancel = (RelativeLayout) findViewById(R.id.lyt_location_selector);
        lyt_location_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       // lyt_location_cancel.setSoundEffectsEnabled(false);


        final RelativeLayout lyt_town_list_top = (RelativeLayout) findViewById(R.id.townlist_top);
                lyt_town_list_top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              // Do NOTHING we just want to intercept the click event so it does no close if
              // user cliked in the top (missed the add ot nearby)
            }
        });
        lyt_town_list_top.setSoundEffectsEnabled(false);

//        ((ImageButton) findViewById(R.id.header_btn_add)).setImageResource(R.drawable.btn_add_town);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
         //       WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        
        WindowManager.LayoutParams lp = getWindow().getAttributes();  
        lp.dimAmount=0.7f;  
        getWindow().setAttributes(lp);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    //@Override
    //void startSearchTask(String search) {
//        startTask(new WS29bShizzlrRegistrationSearchTowns(this, search));
    //}

/*    @Override
    public boolean shouldShowProgress() {
        return !(taskHelper.getTask() instanceof WS29bShizzlrRegistrationSearchTowns);
    }*/

    class TownAdapter extends CursorAdapter {
        class ViewHolder {
            TextView tv;
      //      CheckBox chk;
     //       ImageView itmImage;
        }

        private int mColumnName;
        private String strLastViewedTown;

        public TownAdapter(Context context, Cursor c) {
            super(context, c);
            mColumnName = c.getColumnIndex(DB.COLUMN_TOWNNAME);
            strLastViewedTown = app.getLastViewedTown();
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder vh = (ViewHolder) view.getTag();
            vh.tv.setText(cursor.getString(mColumnName));
            //No longer show last viewed town
 //           if (vh.tv.getText().toString().equalsIgnoreCase(strLastViewedTown)) {
//                vh.itmImage.setVisibility(View.INVISIBLE);
//            } else {
//                vh.itmImage.setVisibility(View.INVISIBLE);
 //           }
            mBroadcasting = true;
      //      vh.chk.setChecked(true);
      //      vh.chk.setTag(cursor.getPosition());
            mBroadcasting = false;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = View.inflate(context, R.layout.list_item_minitownlistitem, null);
//            CheckBox chk = (CheckBox) v.findViewById(R.id.item__checkbox);
//            chk.setVisibility(View.GONE);

            ViewHolder vh = new ViewHolder();
   //         vh.chk = chk;
            vh.tv = (TextView) v.findViewById(R.id.item__text);
  //          vh.itmImage = (ImageView) v.findViewById(R.id.item__image);
            v.setTag(vh);
            return v;
        }

        public String getTownName(int position) {
            return ((Cursor) getItem(position)).getString(mColumnName);
        }
    }

/*
    @Override
    protected int getHeaderViewResId() {
        return R.layout.header_logo_slider;
    }
*/

/*    @Override
    public void setInProgress(boolean inProgress) {
        if (!shouldShowProgress())
            progressBar.setVisibility(inProgress ? View.VISIBLE : View.GONE);
        else
            super.setInProgress(inProgress);
    }*/

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DLG_ENABLE_GPS) {
            return new AlertDialog.Builder(this).setTitle("GPS Not Enabled").setMessage("You have GPS Location option turned off in settings.  Would you like to turn this on?")
                    .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            enableGPS();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }
        return super.onCreateDialog(id);
    }

    //    @Override
//    protected void onPrepareDialog(int id, Dialog dialog) {
//        if (DLG_REMOVE_TOWN == id) {
//            ((AlertDialog) dialog).setMessage(townAdapter.getTownName(posTown2Remove));
//            return;
//        }
//        super.onPrepareDialog(id, dialog);
//    }
    private void enableGPS() {
        ReadWritePrefs rwPrefs = new ReadWritePrefs();
        rwPrefs.readWriteUserSetting(this, "Write", "GPS_ENABLED", "1");
        // ImageView image = (ImageView) findViewById(R.id.satellite);
        //  Animation fly = AnimationUtils.loadAnimation(this, R.anim.satellite_fly);
        // fly.setRepeatMode(2);
        // image.startAnimation(fly);
        Util.shortToast(this, "GPS Option turned on");
    }
}
