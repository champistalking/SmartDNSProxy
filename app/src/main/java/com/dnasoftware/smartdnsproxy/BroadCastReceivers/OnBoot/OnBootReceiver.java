package com.dnasoftware.smartdnsproxy.BroadCastReceivers.OnBoot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
        //AlarmManagerUtils.configureAlarm(context, Calendar.MINUTE, 3);
    }
}