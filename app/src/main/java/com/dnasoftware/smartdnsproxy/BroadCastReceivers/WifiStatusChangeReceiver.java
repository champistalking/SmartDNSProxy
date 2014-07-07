package com.dnasoftware.smartdnsproxy.BroadCastReceivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dnasoftware.smartdnsproxy.R;
import com.dnasoftware.smartdnsproxy.Utils.IPUtils;


/**
 * Broadcast Receiver in charge of handle a WiFi status change such as activation of WiFi network.
 * It presents the user with a notification questioning if it wants to update the IP to the current one.
 * It also verifies the last updated IP which is stored in a shared preference and decides if to ask or not.
 *
 * <p>Created by Matias Radzinski on 06/07/2014, 12:32 AM.</p>
 */
public class WifiStatusChangeReceiver extends BroadcastReceiver{
    private String currentIP;
    private Handler handler;

    @Override
    public void onReceive(Context context, Intent intent) {
        final Context ctx = context;
        cancelUpdateNotification(context); //Cancel any previous notification

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info == null){ return; }

        boolean isWiFi = info.getType() == ConnectivityManager.TYPE_WIFI;

        if(info.isConnected() && isWiFi){
            handler = new Handler();

            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    String prevIP = getPreviousIP(ctx);
                    currentIP = IPUtils.getCurrentIP();

                    if(!prevIP.equals("0")){
                        if(!prevIP.equals(currentIP)){
                            Log.e("com.dnasoftware.smartdnsproxy", "Previous IP (" + prevIP + ") and Current IP match, will not update since it's not required");
                            return;
                        }
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            askForUpdate(ctx);
                        }
                    });
                }
            });

            th.start();
        }
    }

    private void askForUpdate(Context context){
        Intent intentUpdate = new Intent(context, NotificationUpdateInputReceiver.class);
        intentUpdate.setAction(currentIP);
        PendingIntent pIntentUpdate = PendingIntent.getBroadcast(context, 0, intentUpdate, 0);

        Intent intentNoUpdate = new Intent(context, NotificationUpdateInputReceiver.class);
        intentNoUpdate.setAction("NO");
        PendingIntent pIntentNoUpdate = PendingIntent.getBroadcast(context, 0, intentNoUpdate, 0);

        String message = context.getString(R.string.update_required_message);

        Notification notification;
        notification = new NotificationCompat.Builder(context)
                .setContentIntent(pIntentUpdate)
                .setAutoCancel(true)
                .setContentTitle(context.getString(R.string.update_request_notification_title))
                .setContentText(message)
                .setSubText(context.getString(R.string.update_request_notification_footer) + currentIP)
                .setTicker(message)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setLights(Color.WHITE, 1, 0)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .addAction(android.R.drawable.ic_popup_sync, context.getString(R.string.update_request_notification_button_yes), pIntentUpdate)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, context.getString(R.string.update_request_notification_button_no), pIntentNoUpdate)
                .build();

        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(91345636, notification);
    }


    protected String getPreviousIP(Context context){
        final SharedPreferences pref = context.getSharedPreferences("DNSSmartProxy", Context.MODE_PRIVATE);
        return pref.getString("IP","0");
    }

    private void cancelUpdateNotification(Context ctx){
        String  s = Context.NOTIFICATION_SERVICE;
        NotificationManager mNM = (NotificationManager) ctx.getSystemService(s);
        mNM.cancel(91345636);
    }
}
