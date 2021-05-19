package main.stager.utils.pushNotifications;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import main.stager.SECRETS;

public class PushNotificationGenerator {
    static final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    static final private String serverKey = "key=" + SECRETS.SERVER_KEY;
    static final private String contentType = "application/json";
    static final private Response.ErrorListener ignoreError = (x)->{};
    static final private Response.Listener<JSONObject> ignoreResponse = (x)->{};

    public static boolean send(String topic, String title, String message,
                               Response.Listener<JSONObject> listener) {
        return send(topic, title, message, listener, ignoreError);
    }

    public static boolean send(String topic, String title, String message,
                               Response.ErrorListener errorListener) {
        return send(topic, title, message, ignoreResponse, errorListener);
    }

    public static boolean send(String topic, String title, String message) {
        return send(topic, title, message, ignoreResponse, ignoreError);
    }

    public static boolean send(String topic, String title, String message,
                               Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener) {
        String TOPIC = "/topics/" + topic;

        JSONObject notification = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title", title);
            notificationBody.put("message", message);

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
        RequestQueueController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
