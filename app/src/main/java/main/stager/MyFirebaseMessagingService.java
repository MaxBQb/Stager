package main.stager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Random;
import main.stager.utils.pushNotifications.StagerPushNotificationHandler;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this,
                getString(R.string.Notification__channel_id)
        );

        StagerPushNotificationHandler.getInstance(getBaseContext())
                                     .handleAny(builder, remoteMessage);

        NotificationManagerCompat.from(getApplicationContext()).notify(
                new Random().nextInt(3000),
                builder.build()
        );
    }
}