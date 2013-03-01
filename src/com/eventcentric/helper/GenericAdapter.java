package com.eventcentric.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;



abstract public class GenericAdapter<T> extends BaseAdapter {

    protected ArrayList<T> data = new ArrayList<T>();
    protected final Context mContext;

    ImageDownloader imgDownloader;

    public GenericAdapter(Context context) {
        this.mContext = context;
        imgDownloader = new ImageDownloader();
    }

    public void setData(List<T> data) {
        this.data.clear();
        if (data != null)
            this.data.addAll(data);
        notifyDataSetChanged();
    }

    public abstract String getImage(T item);

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    public ArrayList<T> getData() {
        return data;
    }
}
