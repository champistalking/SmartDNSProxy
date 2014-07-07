package com.dnasoftware.smartdnsproxy;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.dnasoftware.smartdnsproxy.BroadCastReceivers.Notifications.NotificationUpdateInputReceiver;
import com.dnasoftware.smartdnsproxy.Services.PollService;
import com.dnasoftware.smartdnsproxy.Utils.IPUtils;
import com.dnasoftware.smartdnsproxy.Utils.NetworkUtils;

import java.util.Calendar;

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

        configureAlarm();
        finish();
    }

    private void configureAlarm(){
        if(doesTheAlarmExists()){ return; }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pIntent = PendingIntent.getService(this, 0, new Intent(this, PollService.class), 0);

        // Alarm will trigger off every 15 minutes and will wake up the device if required.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15*60*1000, pIntent);
    }

    private boolean doesTheAlarmExists(){
        return (PendingIntent.getService(this, 0,
                new Intent(this, PollService.class),
                PendingIntent.FLAG_NO_CREATE) != null);
    }
}
