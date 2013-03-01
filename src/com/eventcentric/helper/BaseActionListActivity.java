package com.eventcentric.helper;
import com.eventcentric.*;

import android.widget.CheckBox;
import com.eventcentric.ws.BaseTask;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.awt.*;


abstract public class BaseActionListActivity extends BaseActionActivity implements OnItemClickListener {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        /*textInfo = (TextView) findViewById(R.id.no_information);
        if (getParent() != null)
            textInfo.setTextColor(getResources().getColor(R.color.font_white));*/
        adapter = createListAdapter();

        if (adapter != null) {
            adapter.registerDataSetObserver(observer);
            listView = (ListView) findViewById(android.R.id.list);
            beforeAdapterBond();
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
           // findViewById(R.id.list_title).setVisibility(shoudShowListTitle() ? View.VISIBLE : View.GONE);
            //showNoInfoTextIfNeeded();
        }
    }

    protected void beforeAdapterBond() {
    }

    abstract protected int getLayoutResId();
    abstract BaseAdapter createListAdapter();

    protected String getInfoText() {
        return null;
    }

    protected void setListInfoVisibility(boolean visible) {
        String s = getInfoText();
        if (s != null)
            textInfo.setText(s);
      // textInfo.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public ListView getListView() {
        return listView;
    }

    @Override
    public void onSuccess(BaseTask<?, ?, ?> task) {
    }

    private void showNoInfoTextIfNeeded() {
        setListInfoVisibility(adapter.getCount() == 0);
    }

    protected boolean shoudShowListTitle() {
        return false;
    }

}
