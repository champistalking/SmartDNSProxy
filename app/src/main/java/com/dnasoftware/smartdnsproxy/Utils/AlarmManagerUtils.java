package com.dnasoftware.smartdnsproxy.Utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.dnasoftware.smartdnsproxy.Services.PollService;

import java.util.Calendar;

/**
 * AlarmManager Utilities
 * Created by Matias Radzinski on 09/07/2014, 10:43 PM.
 */
public class AlarmManagerUtils {

    public static void configureAlarm(Context context, int timeUnit, int time){
        if(doesTheAlarmExists(context)){ return; }

        Calendar cal = Calendar.getInstance();
        cal.add(timeUnit, time);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pIntent = PendingIntent.getService(context, 4504528, new Intent(context, PollService.class), 0);

        // Alarm will trigger off every 15 minutes and will wake up the device if required.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 15*60*1000, pIntent);

        // Only for debug purpose
        //Log.e("AlarmManagerUtils", "Alarm has been configured");
    }

    public static void cancelAlarm(Context context){
        //Log.e("AlarmManagerUtils", "Cancel alarm began...");

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pIntent = PendingIntent.getService(context, 4504528, new Intent(context, PollService.class), 0);

        alarmManager.cancel(pIntent);
        pIntent.cancel();

        // Only for debug purpose
//        if(!doesTheAlarmExists(context)){
//            Log.e("AlarmManagerUtils", "Cancel alarm succeeded :)");
//        }
    }

    public static boolean doesTheAlarmExists(Context context){
        return (PendingIntent.getService(context, 4504528,
                new Intent(context, PollService.class),
                PendingIntent.FLAG_NO_CREATE) != null);
    }
}
