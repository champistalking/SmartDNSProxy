package com.dnasoftware.smartdnsproxy.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Network Utilities
 * Created by Matias Radzinski on 07/07/2014, 05:00 PM.
 */
public class NetworkUtils {

    public static boolean isWiFiConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
    }
}
