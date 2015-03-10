package com.jordinavines.applicationtest.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by jordinavines on 07/03/2015.
 */
public class Utils {


    public static boolean isConnected(Context ctx){
        //ConnectivityManager cm = ( ConnectivityManager ) ctx.getSystemService( Context.CONNECTIVITY_SERVICE );
        //NetworkInfo ni = cm.getActiveNetworkInfo();
        // if(ni == null) return false;
        //return ni.isConnected();
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo()!= null && connectivityManager.getActiveNetworkInfo().isAvailable()) {
            return true;
        }
        return false;
    }

}
