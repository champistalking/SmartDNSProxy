package com.dnasoftware.smartdnsproxy.BroadCastReceivers.WiFi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.dnasoftware.smartdnsproxy.BroadCastReceivers.Notifications.NotificationUpdateInputReceiver;
import com.dnasoftware.smartdnsproxy.R;
import com.dnasoftware.smartdnsproxy.Utils.AlarmManagerUtils;
import com.dnasoftware.smartdnsproxy.Utils.IPUtils;
import com.dnasoftware.smartdnsproxy.Utils.NetworkUtils;
import com.dnasoftware.smartdnsproxy.Utils.SharedPrefsUtils;

import java.util.Calendar;


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

    private SharedPrefsUtils spUtil;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)){
            // EXTRA_SUPPLICANT_CONNECTED doesn't mean that the WiFi is completely connected but it
            // does work when the wifi get's disconnected. We check connection with CONNECTIVITY_ACTION.

            boolean connected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);

            if(!connected){
                //Log.e("WifiStatusChangeReceiver", "WiFi disconnected! Cancel alarm");
                // Cancel the alarm (if needed) if WiFi connection goes off.
                AlarmManagerUtils.cancelAlarm(context);
            }

        } else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) ||
                intent.getAction().equals("com.dnasoftware.smartdnsproxy.POLLSERVICE")){

            final Context ctx = context;
            cancelUpdateNotification(context); //Cancel any previous notification

            spUtil = new SharedPrefsUtils(context);

            boolean isWiFi = NetworkUtils.isWiFiConnected(context);

            if(isWiFi){
                //Log.e("WifiStatusChangeReceiver", "WiFi connected!");
                handler = new Handler();

                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String prevIP = spUtil.getSavedIP();
                        currentIP = IPUtils.getCurrentIP();

                        if(!prevIP.equals("0")){
                            if(prevIP.equals(currentIP)){
                                //Log.e("com.dnasoftware.smartdnsproxy", "Previous IP (" + prevIP + ") and Current IP match, will not update since it's not required");

                                // If the current IP matches the previous IP it means that the user already wanted to
                                // update it, so we enable the service to monitor for dynamic IP changes.
                                AlarmManagerUtils.configureAlarm(ctx, Calendar.MINUTE, 15);
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
