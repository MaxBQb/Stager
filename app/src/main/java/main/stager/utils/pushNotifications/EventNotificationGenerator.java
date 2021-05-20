package main.stager.utils.pushNotifications;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import com.android.volley.Response;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import main.stager.R;
import main.stager.utils.DataProvider;

public class EventNotificationGenerator {
    private static EventNotificationGenerator instance;
    private Context context;

    private EventNotificationGenerator(@NonNull Context ctx) {
        context = ctx;
    }

    public static synchronized void init(Context context) {
        if (instance != null)
            throw new RuntimeException("EventNotificationGenerator already exists");
        instance = new EventNotificationGenerator(context);
    }

    public static EventNotificationGenerator getInstance() {
        if (instance == null)
            throw new RuntimeException("EventNotificationGenerator not init yet");
        return instance;
    }

    public boolean sendAndListen(@NonNull String topic, @NonNull EventType event,
                                 Map<String, String> payload,
                                 Response.Listener<JSONObject> listener) {
        return PushNotificationGenerator.sendAndListen(topic,
                getTextMessage(event), "",
                createPayload(payload, event),
                listener);
    }

    public boolean sendAndListen(@NonNull String topic, @NonNull EventType event,
                                 Response.Listener<JSONObject> listener) {
        return sendAndListen(topic, event, null, listener);
    }

    public boolean sendAndListenError(@NonNull String topic, @NonNull EventType event,
                                      Map<String, String> payload,
                                      Response.ErrorListener errorListener) {
        return PushNotificationGenerator.sendAndListenError(topic,
                getTextMessage(event), "",
                createPayload(payload, event),
                errorListener);
    }

    public boolean sendAndListenError(@NonNull String topic, @NonNull EventType event,
                                      Response.ErrorListener errorListener) {
        return sendAndListenError(topic, event, null, errorListener);
    }

    public boolean send(@NonNull String topic, @NonNull EventType event,
                        Map<String, String> payload) {
        return PushNotificationGenerator.send(topic,
                getTextMessage(event), "",
                createPayload(payload, event));
    }

    public boolean send(@NonNull String topic, @NonNull EventType event) {
        return send(topic, event, null);
    }

    public boolean sendAndListenBoth(@NonNull String topic, @NonNull EventType event,
                                     Map<String, String> payload,
                                     Response.Listener<JSONObject> listener,
                                     Response.ErrorListener errorListener) {
        return PushNotificationGenerator.sendAndListenBoth(topic,
                getTextMessage(event), "",
                createPayload(payload, event),
                listener, errorListener);
    }

    private @NonNull Map<String, String> createPayload(
            Map<String, String> payload,
            @NonNull EventType event) {
        if (payload == null)
            payload = new HashMap<>();
        payload.put(StagerPushNotificationHandler.SENDER,
                DataProvider.getInstance().getUID());
        payload.put(StagerPushNotificationHandler.EVENT_TYPE,
                event.name());
        return payload;
    }

    public @StringRes int getMessage(@NonNull EventType event) {
        switch (event) {
            case FRIENDSHIP_REQUEST: return R.string.Notifications_message_FriendshipRequest;
        }
        throw new IllegalStateException("Unsupported EventType");
    }

    public String getTextMessage(EventType event) {
        return context.getString(getMessage(event));
    }

}
