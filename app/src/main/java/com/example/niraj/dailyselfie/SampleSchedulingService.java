package com.example.niraj.dailyselfie;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import java.io.IOException;
/**
 * Created by niraj on 7/26/2015.
 */
public class SampleSchedulingService extends IntentService {

    // Notification Text Elements
    //tickerText scrolls when notification is displayed on Notification bar
    private final CharSequence tickerText = "It's time for your selfie today !";

    //The contentText is the text to be displayed below the Notification title (R.string.alert)
    private final CharSequence contentText = "Time for your selfie !!";

    // Notification Count
    //This counter shall not work because SampleSchedulingService is invoked by SampleAlarmReceiver
    //which will reset mNotificationCount to -1 everytime. Need to send the count value outside of
    // this service. How???????
    //private int mNotificationCount = -1;


    public SampleSchedulingService() {
        //default constructor
        super("SchedulingService");
    }


    // An ID used to post the notification.
    public static final int NOTIFICATION_ID = 1;

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    //onHandleIntent is a mandatory method for SampleSchedulingService.
    @Override
    protected void onHandleIntent(Intent intent)  {

        // Create notification
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        //Increment notification counter

      //  mNotificationCount ++;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.camera_sbar)
                        .setContentTitle(getString(R.string.alert))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(tickerText))
                        .setContentText(contentText);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


        // Release the wake lock provided by the BroadcastReceiver.
        SampleAlarmReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
