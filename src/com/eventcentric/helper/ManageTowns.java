package com.eventcentric.helper;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.Bundle;

import com.eventcentric.db.DB;
//import com.eventcentric.helper.SearchAsYouTypeBaseActivity;
import com.eventcentric.helper.Town;
import com.eventcentric.helper.Util;
import com.eventcentric.ws.*;
import com.eventcentric.R;

public class ManageTowns extends com.eventcentric.helper.SearchAsYouTypeBaseActivity implements OnClickListener, OnCheckedChangeListener {

    private ListView lv;
    private TownAdapter townAdapter;
    private AutoCompleteTextView townName;
    //    private static final int MENU_REMOVE = 0;
    private static final int DLG_REMOVE_TOWN = 65756;
    private ProgressBar progressBar;
    public boolean mBroadcasting;
    int posTown2Remove;
    //Blachat 03/09/2011
    public static final String FromMakeAPlan = "FromMakeAPlan";
    private boolean isFromMakeAPlan = false;
    private boolean bUserSelectedTown = false;


    @Override
    public void onSuccess(BaseTask<?, ?, ?> task) {
        if (task instanceof WS29bShizzlrRegistrationSearchTowns) {
            
            arrayAdapter = new ArrayAdapter<String>(this, getLayoutResId(), ((WS29bShizzlrRegistrationSearchTowns) task).getActualResult());
            townName.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            townName.showDropDown();

            //
            if (bUserSelectedTown) {
                bUserSelectedTown = false;
                String town = townName.getEditableText().toString();
                if (TextUtils.isEmpty(town)) {
                    Util.shortToast(this, "Please enter a town name");
                    return;
                } else {
                    Town myTown = new Town(town);
                    if (town != null) {
                        app.getDatabaseManager().saveTown(myTown);
                        townAdapter.getCursor().requery();
                        searchText.setText("");
                        InputMethodManager imm = (InputMethodManager)getSystemService(
                      	      Context.INPUT_METHOD_SERVICE);
                      	imm.hideSoftInputFromWindow(townName.getWindowToken(), 0);
                        
                    }
                }
            }


            //
//            super.onSuccess(task);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_btn_add:
            case R.id.manage_towns__btn_add:
                String town = townName.getEditableText().toString();
                if (TextUtils.isEmpty(town)) {
                    Util.shortToast(this, "Please enter a town name");
                    return;
                }
                String s = townName.getEditableText().toString();
                Town myTown = new Town(s);
                if (town != null) {
                    app.getDatabaseManager().saveTown(myTown);
                    townAdapter.getCursor().requery();
                    searchText.setText("");
                    
                }
                InputMethodManager imm = (InputMethodManager)getSystemService(
                	      Context.INPUT_METHOD_SERVICE);
                	imm.hideSoftInputFromWindow(townName.getWindowToken(), 0);
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
        if (isFromMakeAPlan) {
            setResult(RESULT_OK, new Intent().putExtra("EXTRA_TOWN", townAdapter.getTownName(pos).toString()));
            finish();
        }
    }

//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//        startTask(new WS32RemoveTown(this, app.getShizzlrUserId(), (int) townAdapter.getItemId(info.position)));
//        return true;
//    }

    @Override
    List<String> getResult(BaseTask<?, ?, ?> task) {
        return ((WS29bShizzlrRegistrationSearchTowns) task).getActualResult();
    }

    @Override
    void initUI() {
        //Blachat 03/09/2011   -  Determine if this was called from Make a plan screen
        //                     -
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isFromMakeAPlan = extras.getBoolean(FromMakeAPlan, false);
        }

        setContentView(R.layout.activity_manage_towns);
        lv = (ListView) findViewById(android.R.id.list);
        Cursor c = app.getDatabaseManager().getTownsCursor();
        startManagingCursor(c);
        townAdapter = new TownAdapter(this, c);
        lv.setAdapter(townAdapter);
        townName = (AutoCompleteTextView) findViewById(R.id.manage_towns__search);
        townName.setAdapter(arrayAdapter);
        searchText = townName;
        lv.setOnItemClickListener(this);
//        registerForContextMenu(lv);
        progressBar = (ProgressBar) findViewById(R.id.header_progress);
        findViewById(R.id.manage_towns__btn_add).setOnClickListener(this);
        findViewById(R.id.manage_towns__btn_add).setVisibility(View.GONE);

        findViewById(R.id.header_title).setVisibility(View.GONE);

        townName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                bUserSelectedTown = true;

            }
        });


    }


    @Override
    void startSearchTask(String search) {
        startTask(new WS29bShizzlrRegistrationSearchTowns(this, search));
    }

    @Override
    public boolean shouldShowProgress() {
        return !(taskHelper.getTask() instanceof WS29bShizzlrRegistrationSearchTowns);
    }

    class TownAdapter extends CursorAdapter {
        class ViewHolder {
            TextView tv;
            CheckBox chk;
        }

        private int mColumnName;

        public TownAdapter(Context context, Cursor c) {
            super(context, c);
            mColumnName = c.getColumnIndex(DB.COLUMN_TOWNNAME);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder vh = (ViewHolder) view.getTag();
            vh.tv.setText(cursor.getString(mColumnName));
            mBroadcasting = true;
            vh.chk.setChecked(true);
            vh.chk.setTag(cursor.getPosition());
            mBroadcasting = false;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = View.inflate(context, R.layout.item_checkbox, null);
            CheckBox chk = (CheckBox) v.findViewById(R.id.item__checkbox);
            chk.setVisibility(View.VISIBLE);
            //Blachat 03/09/2011 - Display Checkbox based on where this screen is being called from
            if (isFromMakeAPlan) {
                chk.setVisibility(View.GONE);
            }
            chk.setOnCheckedChangeListener(ManageTowns.this);
            ViewHolder vh = new ViewHolder();
            vh.chk = chk;
            vh.tv = (TextView) v.findViewById(R.id.item__text);
            v.setTag(vh);
            v.findViewById(R.id.item__image).setVisibility(View.GONE);
            return v;
        }

        public String getTownName(int position) {
            return ((Cursor) getItem(position)).getString(mColumnName);
        }
    }

    @Override
    protected int getHeaderViewResId() {
        return R.layout.header_logo_slider_no_tab_clickable;
    }

    @Override
    public void setInProgress(boolean inProgress) {
        if (!shouldShowProgress())
            progressBar.setVisibility(inProgress ? View.VISIBLE : View.GONE);
        else
            super.setInProgress(inProgress);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked && !mBroadcasting) {
            posTown2Remove = (Integer) buttonView.getTag();
            mBroadcasting = true;
            buttonView.setChecked(true);
            mBroadcasting = false;
            showDialog(DLG_REMOVE_TOWN);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (DLG_REMOVE_TOWN == id) {
            return new AlertDialog.Builder(this)
                    .setTitle("Remove this town")
                    .setMessage("")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            app.getDatabaseManager().removeTown(townAdapter.getTownName(posTown2Remove).toString());
                            townAdapter.getCursor().requery();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }
        return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if (DLG_REMOVE_TOWN == id) {
            ((AlertDialog) dialog).setMessage(townAdapter.getTownName(posTown2Remove));
            return;
        }
        super.onPrepareDialog(id, dialog);
    }
}
