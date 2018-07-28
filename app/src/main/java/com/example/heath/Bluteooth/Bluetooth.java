package com.example.heath.Bluteooth;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.heath.R;

@SuppressWarnings("deprecation")
public class Bluetooth extends TabActivity {
    /** Called when the activity is first created. */

	enum ServerOrCilent{
		NONE,
		SERVICE,
		CILENT
	};
    private Context mContext;
    static String BlueToothAddress = "null";
    static ServerOrCilent serviceOrCilent = ServerOrCilent.NONE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        mContext = this;        
    	setContentView(R.layout.main);
    }
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {

		  Toast.makeText(mContext, "address:", Toast.LENGTH_SHORT).show();

	    }
    @Override
    protected void onDestroy() {
        /* unbind from the service */
    	    	    
        super.onDestroy();
    }      
	public static class SiriListItem {
		String message;
		boolean isSiri;

		public SiriListItem(String msg, boolean siri) {
			message = msg;
			isSiri = siri;
		}
	}   
}