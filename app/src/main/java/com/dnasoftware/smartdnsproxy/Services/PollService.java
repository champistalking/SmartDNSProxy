package com.dnasoftware.smartdnsproxy.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.dnasoftware.smartdnsproxy.BroadCastReceivers.WiFi.WifiStatusChangeReceiver;
import com.dnasoftware.smartdnsproxy.Utils.IPUtils;
import com.dnasoftware.smartdnsproxy.Utils.NetworkUtils;
import com.dnasoftware.smartdnsproxy.Utils.SharedPrefsUtils;

/**
 * This service will poll every 15 minutes the current user IP. This IP will be compared
 * to the saved IP and if they don't match it'll start the update process.
 * Created by Matias Radzinski on 07/07/2014, 03:41 PM.
 */
public class PollService extends Service{
    private SharedPrefsUtils spUtils;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.e("Service", "Service started...");

        if(NetworkUtils.isWiFiConnected(this)){
            this.spUtils = new SharedPrefsUtils(this);
            new getIP().execute();
        }

        this.stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private class getIP extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... ip) {
            String previousIP = spUtils.getSavedIP();
            String currIP = IPUtils.getCurrentIP();

            if(currIP.equals(previousIP)){ return ""; }

            return currIP;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s == null || s.equals("")){ return; }

            Intent intent = new Intent(PollService.this, WifiStatusChangeReceiver.class);
            intent.setAction("com.dnasoftware.smartdnsproxy.POLLSERVICE");

            sendBroadcast(intent);
        }
    }
}
