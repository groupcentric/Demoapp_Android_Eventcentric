package com.eventcentric.helper;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;



public class ImageLayout extends LinearLayout {

    private static final int IMG_SIZE = 35;

    private LayoutParams params;
    private ImageDownloader imgDownloader; 


    public ImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        int s = getImageSize(context);
        params = new LayoutParams(s, s);
        params.rightMargin = getImageMarign(context);
        params.bottomMargin = getImageMarign(context); //(int) (2 * scale);
//        imgDownloader = new ImageDownloader();
    }


    public void setImages(List<String> imgUrls, ImageDownloader imgDownloader) {
        this.imgDownloader = imgDownloader;
        removeAllViews();
        for (String s : imgUrls)
            addImage(s);
        requestLayout();
    }

    private void addImage(String url) {
        ImageView imgView = new ImageView(getContext());
        imgDownloader.download(url, imgView);
        addView(imgView, params);
    }

    public static int getImageSize(Context ctx) {
        return (int) (getScale(ctx) * IMG_SIZE);
    }

    private static float getScale(Context ctx) {
        return ctx.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int getImageMarign(Context ctx) {
        return (int) (6 * getScale(ctx));
    }
}
