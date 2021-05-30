package main.stager.utils.pushNotifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;
import com.google.firebase.messaging.RemoteMessage;
import main.stager.MainActivity;
import main.stager.R;
import main.stager.StagerApplication;
import main.stager.model.ContactType;
import main.stager.ui.contact_info.ContactInfoFragment;
import main.stager.ui.monitored_action.MonitoredActionFragment;
import main.stager.utils.SettingsWrapper;
import main.stager.utils.Utilits;

public class StagerPushNotificationHandler {
    private Context context;
    private final Bitmap largeIcon;
    private final SettingsWrapper settings;

    public static final String EVENT_DETAILS = "event_details";
    private static final String SEP = ".";
    public static final String EVENT_TYPE = EVENT_DETAILS+SEP+"type";
    public static final String ACTION = EVENT_DETAILS+SEP+"action_id";
    public static final String SENDER = EVENT_DETAILS+SEP+"sender";

    public StagerPushNotificationHandler(@NonNull Context ctx) {
        context = ctx.getApplicationContext();
        largeIcon = BitmapFactory.decodeResource(
            context.getResources(),
            R.mipmap.ic_launcher_round
        );
        settings = StagerApplication.getSettings();
    }

    public boolean handleAny(NotificationCompat.Builder builder, RemoteMessage message) {
        handleBasicNotification(builder, message);
        try {
            String event = message.getData().get(EVENT_TYPE);
            if (event == null) return true;
            EventType eventType = EventType.valueOf(event);
            return handleEvent(builder, message, eventType);
        } catch (IllegalArgumentException ignore) {}
        return true;
    }

    public void handleBasicNotification(NotificationCompat.Builder builder, RemoteMessage message) {
        builder.setSmallIcon(R.mipmap.ic_launcher)
               .setLargeIcon(largeIcon)
               .setAutoCancel(true)
               .setContentIntent(PendingIntent.getActivity(
                   context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_ONE_SHOT
               ));

        String title = message.getData().get(PushNotification.TITLE);
        if (!Utilits.isNullOrBlank(title))
            builder.setContentTitle(title);

        String body = message.getData().get(PushNotification.MESSAGE);
        if (!Utilits.isNullOrBlank(body))
            builder.setContentText(body);
    }

    public boolean handleEvent(NotificationCompat.Builder builder, RemoteMessage message,
                               @NonNull EventType event) {
        builder.setContentTitle(context.getString(EventNotificationBuilder.getTitle(event)))
               .setContentText(context.getString(EventNotificationBuilder.getMessage(event)));
        switch (event) {
            case FRIENDSHIP_REQUEST: return handleFriendshipRequest(builder, message, event);
            case FRIENDSHIP_REQUEST_ACCEPTED: return handleFriendshipRequestAccepted(builder, message, event);
            case ACTION_COMPLETED_SUCCEED:
            case ACTION_COMPLETED_ABORTED: return handleActionCompleted(builder, message, event);
        }
        return true;
    }



    public boolean handleFriendshipRequest(NotificationCompat.Builder builder,
                                           RemoteMessage message,
                                           @NonNull EventType selfEvent) {
        if (!settings.isNotifyFriendshipRequestAllowed(true))
            return false;

        Bundle args = new Bundle();
        args.putString(ContactInfoFragment.ARG_CONTACT_TYPE,
                       ContactType.INCOMING.name());
        args.putString(ContactInfoFragment.ARG_CONTACT_KEY,
                       message.getData().get(SENDER));

        builder.setGroup(selfEvent.name());
        addOnClickTransition(builder, R.id.nav_contact_info, args);
        return true;
    }

    public boolean handleFriendshipRequestAccepted(NotificationCompat.Builder builder,
                                                   RemoteMessage message,
                                                   @NonNull EventType selfEvent) {
        if (!settings.isNotifyFriendshipRequestAcceptedAllowed(false))
            return false;

        Bundle args = new Bundle();
        args.putString(ContactInfoFragment.ARG_CONTACT_TYPE,
                ContactType.ACCEPTED.name());
        args.putString(ContactInfoFragment.ARG_CONTACT_KEY,
                message.getData().get(SENDER));

        builder.setGroup(selfEvent.name());
        addOnClickTransition(builder, R.id.nav_contact_info, args);
        return true;
    }

    public boolean handleActionCompleted(NotificationCompat.Builder builder,
                                                RemoteMessage message,
                                                @NonNull EventType selfEvent) {
        String action = message.getData().get(ACTION);
        String owner = message.getData().get(SENDER);
        if (Utilits.isNullOrBlank(action) ||
            Utilits.isNullOrBlank(owner))
            return false;

        if (!StagerApplication.getListenedEventsController().isEventListened(
                selfEvent.equals(EventType.ACTION_COMPLETED_ABORTED)
                ? StagerApplication.getDataProvider().getActionCompleteAbortedEventName(owner, action)
                : StagerApplication.getDataProvider().getActionCompleteSucceedEventName(owner, action)
            ))
            return false;

        Bundle args = new Bundle();
        args.putString(MonitoredActionFragment.ARG_ACTION_KEY, action);
        args.putString(MonitoredActionFragment.ARG_ACTION_OWNER, owner);

        builder.setGroup(selfEvent.name());
        addOnClickTransition(builder, R.id.nav_monitored_action, args);
        return true;
    }

    private void addOnClickTransition(NotificationCompat.Builder builder,
                                      @IdRes int destination,
                                      Bundle args) {
        builder.setContentIntent(new NavDeepLinkBuilder(context)
            .setComponentName(MainActivity.class)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(destination)
            .setArguments(args)
            .createPendingIntent()
        );
    }
}
