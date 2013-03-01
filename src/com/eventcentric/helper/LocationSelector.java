package com.eventcentric.helper;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.eventcentric.R;

public class LocationSelector extends BaseActivity implements OnClickListener {
    private ImageDownloader imageDownloader;
    private ImageView imgShareItemImage;
    private Button btnAustinTX;
    private Button btnBostonMA;
    private Button btnNewYorkNY;
    private Button btnNewHavenCT;
    private Button btnSanFranciscoCA;
    private TextView txtItemTitle;
    private TextView txtItemAddress;
    private RelativeLayout lyt_cancel;
    private EventObject event;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selector);
        
        btnAustinTX = (Button) findViewById(R.id.btnAustinTX);
        btnAustinTX.setOnClickListener(this);

        btnBostonMA = (Button) findViewById(R.id.btnBostonMA);
        btnBostonMA.setOnClickListener(this);
        
        btnNewHavenCT = (Button) findViewById(R.id.btnNewHavenCT);
        btnNewHavenCT.setOnClickListener(this);
        
        btnNewYorkNY = (Button) findViewById(R.id.btnNewYorkNY);
        btnNewYorkNY.setOnClickListener(this);
        
        btnSanFranciscoCA = (Button) findViewById(R.id.btnSanFranciscoCA);
        btnSanFranciscoCA.setOnClickListener(this);

        lyt_cancel = (RelativeLayout) findViewById(R.id.layout_root);
        lyt_cancel.setOnClickListener(this);

        Intent intent = getIntent();
        event = getIntent().getParcelableExtra("EVENT");
            
        WindowManager.LayoutParams lp = getWindow().getAttributes();  
        lp.dimAmount=0.7f;  
        getWindow().setAttributes(lp); 
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAustinTX:
                setResult(RESULT_OK, new Intent()
		                .putExtra("TOWNSELECT", "Austin, TX")
		        );
                    finish();
                
                break;
            case R.id.btnBostonMA:
                setResult(RESULT_OK, new Intent()
		                .putExtra("TOWNSELECT", "Boston, MA")
		        );
                    finish();
                
                break;
            case R.id.btnNewHavenCT:
                setResult(RESULT_OK, new Intent()
		                .putExtra("TOWNSELECT", "New Haven, CT")
		        );
                    finish();
                
                break;
            case R.id.btnNewYorkNY:
                setResult(RESULT_OK, new Intent()
		                .putExtra("TOWNSELECT", "New York, NY")
		        );
                    finish();
                
                break;
            case R.id.btnSanFranciscoCA:
                setResult(RESULT_OK, new Intent()
		                .putExtra("TOWNSELECT", "San Francisco, CA")
		        );
                    finish();
                
                break;
           
                
            case R.id.layout_root:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       
    }

}
