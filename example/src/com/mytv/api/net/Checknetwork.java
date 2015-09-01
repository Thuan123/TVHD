package com.mytv.api.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Checknetwork {
	public static boolean isConnected(Context c){
    	ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
    	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	    if (networkInfo != null && networkInfo.isConnected()) 
    	    	return true;
    	    else
    	    	return false;	
    }

}
