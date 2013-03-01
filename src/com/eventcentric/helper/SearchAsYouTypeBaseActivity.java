package com.eventcentric.helper;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;

import com.eventcentric.ws.BaseTask;
import com.eventcentric.ws.ErrorResponse;

import com.eventcentric.R;

public abstract class SearchAsYouTypeBaseActivity extends BaseTaskActivity
        implements TextWatcher, OnItemClickListener {

	protected EditText searchText;
	protected ArrayAdapter<String> arrayAdapter;

	protected void preInitUI() {};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		arrayAdapter = new ArrayAdapter<String>(this, getLayoutResId());
		preInitUI();
		initUI();
		if (searchText == null)
		    throw new RuntimeException();
//		listView = (ListView) findViewById(android.R.id.list);
		searchText.addTextChangedListener(this);
//		listView.setAdapter(adapter);
//		listView.setOnItemClickListener(this);
	}

	abstract void initUI();
	abstract List<String> getResult(BaseTask<?, ?, ?> task);
	abstract void startSearchTask(String search);

	protected int getLayoutResId() {
	    return R.layout.item_text;
	}

    @Override
    public void onSuccess(BaseTask<?, ?, ?> task) {
        List<String> result = getResult(task);
        //System.out.println("items " + result.size());
    	arrayAdapter.clear();
        for (String item: result) {
            arrayAdapter.insert(item, arrayAdapter.getCount());
            //System.out.println("add " + item);
        }
//        System.out.println("item1 " + arrayAdapter.getCount());
    	arrayAdapter.notifyDataSetChanged();
    	//System.out.println("item2 " + arrayAdapter.getCount());
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String str = (String) msg.obj;
            startSearchTask(str);
        };
    };

	@Override
	public void afterTextChanged(Editable s) {
		String str = s.toString();
		if (str.length() > 2) {
		    mHandler.removeMessages(0);
		    mHandler.sendMessageDelayed(Message.obtain(mHandler, 0, str), 400);
		} else {
			arrayAdapter.clear();
			arrayAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public boolean shouldShowProgress() {
		return false;
	}

	@Override
	public void onError(ErrorResponse err) {
//	    super.onError(err);
	    System.err.println("SEARCHERROR: " + err.getErrorMessage());
	}


}
