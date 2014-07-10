package com.dnasoftware.smartdnsproxy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dnasoftware.smartdnsproxy.BroadCastReceivers.Notifications.NotificationUpdateInputReceiver;
import com.dnasoftware.smartdnsproxy.Utils.IPUtils;
import com.dnasoftware.smartdnsproxy.Utils.NetworkUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(NetworkUtils.isWiFiConnected(this)){
            // Send broadcast to start IP update

            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    String currentIP = IPUtils.getCurrentIP();
                    final Intent intent = new Intent(MainActivity.this, NotificationUpdateInputReceiver.class);
                    intent.setAction(currentIP);

                    sendBroadcast(intent);
                }
            });

            th.start();
        } else {
            Toast.makeText(this, getString(R.string.error_no_wifi_manual_update), Toast.LENGTH_LONG).show();
        }

        finish();
    }
}
