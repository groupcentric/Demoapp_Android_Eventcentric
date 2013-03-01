package com.eventcentric.helper;
import android.widget.CheckBox;


import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.awt.*;


abstract public class BaseListActivity extends BaseActivity implements OnItemClickListener {

    private ListView listView;
    private BaseAdapter adapter;
    protected TextView textInfo;

    private DataSetObserver observer = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            showNoInfoTextIfNeeded();
        }
    };

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        adapter = createListAdapter();

        if (adapter != null) {
            adapter.registerDataSetObserver(observer);
            listView = (ListView) findViewById(android.R.id.list);
            beforeAdapterBond();
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            showNoInfoTextIfNeeded();
        }
    }

    protected void beforeAdapterBond() {
    }

    abstract protected int getLayoutResId();
    public abstract BaseAdapter createListAdapter();

    protected String getInfoText() {
        return null;
    }

    protected void setListInfoVisibility(boolean visible) {
        String s = getInfoText();
        if (s != null)
            textInfo.setText(s);
    }

    public ListView getListView() {
        return listView;
    }

    
    private void showNoInfoTextIfNeeded() {
        setListInfoVisibility(adapter.getCount() == 0);
    }

    protected boolean shoudShowListTitle() {
        return false;
    }

}
