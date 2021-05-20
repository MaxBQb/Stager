package main.stager.utils.pushNotifications;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import main.stager.SECRETS;
import main.stager.StagerApplication;

public class PushNotificationGenerator {
    static final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    static final private String serverKey = "key=" + SECRETS.SERVER_KEY;
    static final private String contentType = "application/json";

    public static final String TITLE = "title";
    public static final String MESSAGE = "message";


    static final private Response.ErrorListener ignoreError = (x)->{};
    static final private Response.Listener<JSONObject> ignoreResponse = (x)->{};

    public static boolean sendAndListen(String topic, String title, String message,
                                        Map<String, String> payload,
                                        Response.Listener<JSONObject> listener) {
        return sendAndListenBoth(topic, title, message, payload, listener, ignoreError);
    }

    public static boolean sendAndListen(String topic, String title, String message,
                                        Response.Listener<JSONObject> listener) {
        return sendAndListen(topic, title, message, null, listener);
    }

    public static boolean sendAndListenError(String topic, String title, String message,
                                             Map<String, String> payload,
                                             Response.ErrorListener errorListener) {
        return sendAndListenBoth(topic, title, message, payload, ignoreResponse, errorListener);
    }

    public static boolean sendAndListenError(String topic, String title, String message,
                                             Response.ErrorListener errorListener) {
        return sendAndListenError(topic, title, message, null, errorListener);
    }

    public static boolean send(String topic, String title, String message,
                               Map<String, String> payload) {
        return sendAndListenBoth(topic, title, message, payload, ignoreResponse, ignoreError);
    }

    public static boolean send(String topic, String title, String message) {
        return send(topic, title, message, null);
    }

    public static boolean sendAndListenBoth(String topic, String title, String message,
                                            Map<String, String> payload,
                                            Response.Listener<JSONObject> listener,
                                            Response.ErrorListener errorListener) {
        String TOPIC = "/topics/" + topic;

        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            if (title != null)
                notificationBody.put(TITLE, title);

            if (message != null)
                notificationBody.put(MESSAGE, message);

            if (payload != null)
                for (Map.Entry<String, String> entry: payload.entrySet())
                    notificationBody.put(entry.getKey(), entry.getValue());

            notification.put("to", TOPIC);
            notification.put("data", notificationBody);
        } catch (JSONException e) {
           return false;
        }
        sendNotification(notification, listener, errorListener);
        return true;
    }

    private static void sendNotification(JSONObject notification,
                                  Response.Listener<JSONObject> listener,
                                  Response.ErrorListener errorListener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API,
                notification, listener, errorListener) {
            @Override
            @NotNull public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        StagerApplication.getRequestQueueController()
                .addToRequestQueue(jsonObjectRequest);
    }
}
