package com.eventcentric.helper;

import com.eventcentric.App;

import com.eventcentric.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.*;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;



public class BaseActivity extends Activity {

    private FrameLayout content;
    protected App app;
  
    protected int getHeaderViewResId() {
        return -1;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.setContentView(R.layout.activity_base_header);
        content = (FrameLayout) findViewById(R.id.activity_base_content);
        app = (App) getApplication();
    }

    private void initHeader() {
        int res = getHeaderViewResId();
        if (res > -1) {
            ViewStub stub = (ViewStub) findViewById(R.id.activity_base_stub_header);
            stub.setLayoutResource(res);
            stub.inflate();
        }
    }

    public void setContentView(int layoutResID) {
        if (content != null) {
            content.removeAllViews();
            initHeader();
            content.addView(View.inflate(this, layoutResID, null), new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            Util.postInitHeader(this, isAddButtonVisible());//postInitHeader();
            setTitle(getTitle());
        } else {
            super.setContentView(layoutResID);
        }
    }

    public void setContentView(View view, LayoutParams params) {
        if (content != null) {
            content.removeAllViews();
            initHeader();
            content.addView(view, params);
            Util.postInitHeader(this, isAddButtonVisible());//postInitHeader();
            setTitle(getTitle());
        } else {
            super.setContentView(view, params);
        }
    }

    public void setContentView(View view) {
        if (content != null) {
            content.removeAllViews();
            initHeader();
            content.addView(view);
            setTitle(getTitle());
            Util.postInitHeader(this, isAddButtonVisible());// postInitHeader();
        } else {
            super.setContentView(view);
        }
    }

    protected FrameLayout getContentHolder() {
        return content;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setTitle(int titleId) {
        TextView tv = (TextView) findViewById(R.id.header_title);
        if (tv != null)
            tv.setText(titleId);
        else
            super.setTitle(titleId);
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView tv = (TextView) findViewById(R.id.header_title);
        if (tv != null)
            tv.setText(title);
        else
            super.setTitle(title);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        return true;
         
    }

    protected boolean isAddButtonVisible() {
        return true;//!USE_MENU;
    }

    protected void goToMain() {
       // startActivity(Util.getMainIntent(BaseActivity.this));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       

        return super.onOptionsItemSelected(item);
    }

}
