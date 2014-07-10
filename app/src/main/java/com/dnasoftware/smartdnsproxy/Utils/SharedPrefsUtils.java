package com.dnasoftware.smartdnsproxy.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * Created by Matias Radzinski on 09/07/2014, 10:14 PM.
 */
public class SharedPrefsUtils {
    private final SharedPreferences prefs;

    public SharedPrefsUtils(Context ctx) {
        this.prefs = ctx.getSharedPreferences("DNSSmartProxy", Context.MODE_PRIVATE);
    }

    public void saveIP(String Ip){
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("IP", Ip);
        editor.apply();
    }

    public String getSavedIP(){
        return prefs.getString("IP","0");
    }
}
