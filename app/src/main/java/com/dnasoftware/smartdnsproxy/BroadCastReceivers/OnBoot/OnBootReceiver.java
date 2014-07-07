package com.dnasoftware.smartdnsproxy.BroadCastReceivers.OnBoot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dnasoftware.smartdnsproxy.Services.PollService;

import java.util.Calendar;

/**
 * BroadCast Receiver in charge of setting up the service alarm whenever the device boots up.
 * Created by Matias Radzinski on 07/07/2014, 05:07 PM.
 */
public class OnBootReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        configureAlarm(context);
    }

    private void configureAlarm(Context context){
        if(doesTheAlarmExists(context)){ return; }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 3);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pIntent = PendingIntent.getService(context, 0, new Intent(context, PollService.class), 0);

        // Alarm will trigger off every 15 minutes and will wake up the device if required.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15*60*1000, pIntent);
    }

    private boolean doesTheAlarmExists(Context context){
        return (PendingIntent.getService(context, 0,
                new Intent(context, PollService.class),
                PendingIntent.FLAG_NO_CREATE) != null);
    }
}