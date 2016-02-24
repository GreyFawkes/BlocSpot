package io.bloc.android.blocspot.api.intent;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import io.bloc.android.blocspot.R;
import io.bloc.android.blocspot.ui.activity.BlocSpotActivity;

/**
 * Created by Administrator on 11/27/2015.
 */
public class GeofenceIntentService extends IntentService {

    private static final String TAG = "GeofenceIntentService";

    public GeofenceIntentService() {
        super(GeofenceIntentService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Intent Created");
    }


    /*
    the following is the code that handles incoming intents
     */

    @Override
    protected void onHandleIntent(Intent intent) {
            //create a geofencing event from the intent
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        Log.i(TAG, "message");

            //check if the geofenceEvent has an error
            // if so output the error to the LOG
        if(geofencingEvent.hasError()) {
            int errorCode = geofencingEvent.getErrorCode();
            Log.e(TAG, "Location Service error: " + errorCode);

            //else check the transition type and determine action
        } else {

            int transitionType = geofencingEvent.getGeofenceTransition();

            if(transitionType == Geofence.GEOFENCE_TRANSITION_ENTER ||
                    transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {

                //get the list of triggered geofences
                List triggeredGeofences = geofencingEvent.getTriggeringGeofences();

                //for testing get the transaction details in a string
                String geofenceTriggerDetails = getGeofenceTransitionDetails(
                        this,
                        transitionType,
                        triggeredGeofences
                );

                //send the notification and log the details
                sendNotification(geofenceTriggerDetails);
                Log.i(TAG, geofenceTriggerDetails);

            } else {

                Log.e(TAG, "notification");
            }

        }
    }

    private String getGeofenceTransitionDetails( Context context, int geofenceTransition,
                                                 List<Geofence> triggeredGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        //get ids of each triggered geofence
        ArrayList triggeredGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeredGeofences) {
            triggeredGeofencesIdsList.add(geofence.getRequestId());
        }

        String triggeringGeofencesIdString = TextUtils.join(", ", triggeredGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeredGeofencesIdsList;
    }

    private String getTransitionString(int transitionType) {

        switch(transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return "geofence_transition_entered";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return "geofence_transition_exited";
            default:
                return "geofence_other_transition";
        }

    }

    private void sendNotification(String notificationDetails) {

        //create an intent for
        Intent notificationIntent = new Intent(getApplicationContext(), BlocSpotActivity.class);

        if()

        //create a task stack?
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        //add the main activity to the task stack as parent
        stackBuilder.addParentStack(BlocSpotActivity.class);

        //push content Intent onto the stack
        stackBuilder.addNextIntent(notificationIntent);

        //get pending intent containing the entire back stack
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //define the notification settings
        builder.setSmallIcon(R.drawable.ic_play_dark)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getResources(), R.drawable.ic_play_dark))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("notification")
                .setContentIntent(notificationPendingIntent);

        //dismiss notification when user touches it
        builder.setAutoCancel(true);

        //get instance of the notification manager
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //issue the notification
        notificationManager.notify(0, builder.build());


    }

}
