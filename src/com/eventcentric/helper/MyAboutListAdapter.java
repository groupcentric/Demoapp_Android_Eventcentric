
package com.eventcentric.helper;

import com.eventcentric.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.eventcentric.helper.ImageLayout;
import com.eventcentric.helper.Util;

import java.util.List;

abstract public class MyAboutListAdapter<T> extends GenericAdapter<T> {

    private static final int TYPE_ABOUT_APP_TOP = 0;
    private static final int TYPE_ABOUT_APP = 1;


    private final class ViewHolder {
        ImageView img;
        TextView title;
        TextView subtitle;
        ImageLayout imgLayout;
        View oneRow;
    }


    public MyAboutListAdapter(Context context) {
        super(context);
    }

    
    abstract String getPicurl(T item);
    abstract String getTitle(T item);
    abstract String getSubtitle(T item);
    abstract String getURL(T item);
    

    

    String getOneRow(T item) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        T item = getItem(position);
        // get the view type we wish to use
        int type = getItemViewType(position);


        //Header item
//blachat
        if (convertView == null) {
            vh = new ViewHolder();

            switch (type) {
                case TYPE_ABOUT_APP_TOP:
                    // Plan item
                    convertView = View.inflate(mContext, R.layout.list_item_aboutapptop_list, null);
                    
                    break;

                case TYPE_ABOUT_APP:
                    convertView = View.inflate(mContext, R.layout.list_item_aboutapp_list, null);
                    vh.img = (ImageView) convertView.findViewById(R.id.item__image);
                    vh.title = (TextView) convertView.findViewById(R.id.item__text);
                    vh.subtitle = (TextView) convertView.findViewById(R.id.item__text2);
                    break;

            }
            convertView.setTag(vh);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        switch (type) {
            case TYPE_ABOUT_APP_TOP:

                //Confirm friend
                //vh.title.setText(getTitle(item));
                //imgDownloader.download(getPicurl(item), vh.img);
                break;

            case TYPE_ABOUT_APP:
                //Friend item
                vh.title.setText(getTitle(item));
                vh.subtitle.setText(getSubtitle(item));
                imgDownloader.download(getPicurl(item), vh.img);
                break;

        }

        if (type == TYPE_ABOUT_APP) {
           /* vh.imgConfirm.setTag(position);
            vh.imgConfirm.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Integer pos = (Integer) v.getTag();
                    ((FriendsList) mContext).acceptFriendRequest(pos);
                }
            });

            vh.imgIgnore.setTag(position);
            vh.imgIgnore.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Integer pos = (Integer) v.getTag();
                    ((FriendsList) mContext).denyFriendRequest(pos);
                }
            });*/
        }

        return convertView;
    }


    //How many layouts will we be using
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    // return the layout type we wish to use
    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return TYPE_ABOUT_APP_TOP;
        else {
            return TYPE_ABOUT_APP;
        }

    }

    protected int getInflateLayout() {
        return 0;
    }

    protected void fillExtraRow(View oneRow, T item) {
        ((TextView) oneRow).setText(getOneRow(item));
    }
}
