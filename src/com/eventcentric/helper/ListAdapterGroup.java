package com.eventcentric.helper;





import java.util.ArrayList;
import java.util.List;

import com.eventcentric.R;



import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

abstract public class ListAdapterGroup<T> extends GenericAdapter<T> {

	Typeface face = null;
    
    private final class ViewHolder {
        ImageView img;
        TextView title, subtitle, eventDate;
        ImageLayout imgLayout;
    }
    
    
    public ListAdapterGroup(Context context) { 
            super(context);
    }
    public abstract String getTitle(T item);
    public String getSubtitle(T item) {
        return null;
    }
    public abstract long getId(T item);
    public String getPlanDate(T item) {
        return null;
    }
    List<String> getImages(T item) {
        return null;
    }
    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getId(getItem(position));
    }
    
    

  //How many layouts will we be using
    @Override
    public int getViewTypeCount() {
        return 1;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	//TownEvent o = items.get(position);
    	T item = getItem(position);
    	
    	ViewHolder vh;
    	if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.list_item_event_eventbrite, null);
            convertView.setBackgroundResource(R.drawable.list_item_background);
            vh.img = (ImageView) convertView.findViewById(R.id.item__image);
            vh.eventDate = (TextView) convertView.findViewById(R.id.item__date);
            vh.title = (TextView) convertView.findViewById(R.id.item__title);
            vh.subtitle = (TextView) convertView.findViewById(R.id.item__subtitle);
            convertView.setTag(vh);
    	} else {
            vh = (ViewHolder) convertView.getTag();
        }
    	vh.title.setText(getTitle(item).replace("<BR>"," "));
        imgDownloader.download(getImage(item), vh.img);
        String subtitle1 = getSubtitle(item);
        String strEventDate = getPlanDate(item);
        vh.eventDate.setText(strEventDate);
        vh.eventDate.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(subtitle1)) {
            vh.subtitle.setVisibility(View.GONE);
        } else {
            vh.subtitle.setText(subtitle1);
            vh.subtitle.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(subtitle1) != true && subtitle1.toString().trim().length() == 1) {
            vh.subtitle.setVisibility(View.GONE);
        }
        
            
        return convertView;
   

    }
}
