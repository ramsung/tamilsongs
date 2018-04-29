package in.beyonitysoftwares.besttamilsongs.Notification;

import android.content.Intent;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import in.beyonitysoftwares.besttamilsongs.Activities.MainActivity;


/**
 * Created by mohan on 14/2/18.
 */

public class PushNotificaiton extends FirebaseMessagingService {
    private static final String TAG = "PushNotificaiton";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            if(remoteMessage.getData().containsKey("song")) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("song", remoteMessage.getData().get("song"));
                i.putExtra("title",remoteMessage.getData().get("title"));
                i.putExtra("des",remoteMessage.getData().get("des"));

                startActivity(i);
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

}
