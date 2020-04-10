package com.kachhua.goal.Utility;

import android.content.Context;
import android.net.ConnectivityManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtility {


    public static boolean checknet(Context context){

        boolean networkavailable = isNetworkAvailable(context);

        if(networkavailable){

                return  true;

        }else{

            return  false;
        }

    }
    public static  boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error

        }
        return false;
    }
}
