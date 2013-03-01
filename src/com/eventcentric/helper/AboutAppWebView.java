package com.eventcentric.helper;

import com.eventcentric.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.eventcentric.App;

import com.groupcentric.util.Base64.OutputStream;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author support@groupcentric.com <br>
 *         Purpose: Handles dynamic profile webview content <br>
 *         Notes: <br>
 *         Revisions:
 * 
 * 
 */
public class AboutAppWebView extends Activity implements OnClickListener {
	App app;
    WebView webview_profile;
    TextView txtHeader;
    private ProgressBar progressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutapp_webview);
		app = (App) getApplication();
		webview_profile = (WebView) findViewById(R.id.webView_profile);
		webview_profile.setWebViewClient(new webClient());
		
		String strWebUrl = "";
		String strHeader = "";
		if (getIntent().getExtras() != null) {
			if (getIntent().getExtras().getString("web_url") != null) {
				strWebUrl = getIntent().getExtras().getString("web_url");
			}
			if (getIntent().getExtras().getString("header_text") != null) {
				strHeader = getIntent().getExtras().getString("header_text");
			}
		}
		
		webview_profile.getSettings().setJavaScriptEnabled(true);
		webview_profile.loadUrl(strWebUrl);
		findViewById(R.id.imgBack).setOnClickListener(this);
		
	}

	public void onClick(View v) {
		if (v.getId() == R.id.imgBack) {
			finish(); // Go back
		}
	}
	
	 public class webClient extends WebViewClient {

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				progressBar = (ProgressBar) findViewById(R.id.header_progress);
	            progressBar.setVisibility(View.GONE);
	            super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
				progressBar = (ProgressBar) findViewById(R.id.header_progress);
		        progressBar.setVisibility(View.VISIBLE);
	        }

	        @Override
	        public void onPageFinished(WebView view, String url) {
	        	progressBar = (ProgressBar) findViewById(R.id.header_progress);
	            progressBar.setVisibility(View.GONE);
	        }
	    }
	


	

}
