package com.dnasoftware.smartdnsproxy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dnasoftware.smartdnsproxy.BroadCastReceivers.NotificationUpdateInputReceiver;
import com.dnasoftware.smartdnsproxy.Utils.IPUtils;

public class ServiceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Send broadcast to start IP update
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                String currentIP = IPUtils.getCurrentIP();
                final Intent intent = new Intent(ServiceActivity.this, NotificationUpdateInputReceiver.class);
                intent.setAction(currentIP);

                sendBroadcast(intent);
            }
        });

        th.start();

        finish();
    }
}
