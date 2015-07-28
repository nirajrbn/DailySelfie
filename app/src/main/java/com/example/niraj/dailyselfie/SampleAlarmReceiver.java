package com.example.niraj.dailyselfie;

/**
 * Created by niraj on 7/26/2015.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * When the alarm fires, this WakefulBroadcastReceiver receives the broadcast Intent
 * and then starts the IntentService {@code SampleSchedulingService} which is used here simply to send
 * alarm notification.
 .
 */

public class SampleAlarmReceiver extends WakefulBroadcastReceiver {
    static public final String MYTAG = "alarmreceiver";

    // 2 minute trigger values expressed in milliseconds.
    long triggerAtMillis = 120000;
    long intervalMillis = 120000;

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;

    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;


    @Override
    public void onReceive(Context context, Intent intent) {
        // BEGIN_INCLUDE(alarm_onreceive)
        /*
         * // This intent passed in this call will include the wake lock extra as well as
         * // the receiver intent contents.
         * startWakefulService(context, (intent.setComponent(comp)));
         *
         * In this example, we simply create a new intent to deliver to the service.
         * This intent holds an extra identifying the wake lock.
         */
        Intent service = new Intent(context, SampleSchedulingService.class);

        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, service);
        // END_INCLUDE(alarm_onreceive)
    }


    // BEGIN_INCLUDE(set_alarm)
    /**
     * Sets a repeating alarm that wake up the device to fire the alarm in 2 minutes, and
     * every 2 minutes after that When the
     * alarm fires, the app broadcasts an Intent to this WakefulBroadcastReceiver.
     * @param context
     */
    public void setAlarm(Context context) {
        Log.i(MYTAG, "entered setAlarm method");
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SampleAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        //Alarm is set to trigger after 2 minutes of booting and then every 2 minute henceforth.
        //trigerAtMillis and intervalMillis both are set at 2 minutes.
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerAtMillis,
                intervalMillis, alarmIntent);

        //code to automatically restart the alarm when the device is rebooted has been removed
        //for simplicity. So if the device is rebooted, the alarm system shall not function.
        //This function shall be implemented in future.


    }



}//end sampleAlarmReceiver
