package com.eventcentric.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class ReadWritePrefs {
    public static final String PREFS_NAME = "MyPrefsFile";
    
    public String readWriteUserSetting(Context context,String readWrite, String userOption, String settingValue) {

  	  SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

        String usersetting = ""; 
        
        if (readWrite == "Read" )
        {
      	  String strUsersetting = settings.getString(userOption,"");  
      	  usersetting = strUsersetting ;
        } //End Read setting
          
        if (readWrite == "Write" )
        {
      	  SharedPreferences.Editor editor = settings.edit();      
      	  editor.putString(userOption,settingValue.toString());      
      
      	  editor.commit();
            usersetting ="Write";
        } //End Write Setting
          
          return usersetting;

    } // end readWriteUserSetting    
} // End Class