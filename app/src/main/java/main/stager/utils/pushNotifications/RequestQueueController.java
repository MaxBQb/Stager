package main.stager.utils.pushNotifications;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueController {
    private static RequestQueueController instance;
    private RequestQueue requestQueue;
    private Context ctx;

    private RequestQueueController(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized void init(Context context) {
        if (instance != null)
            throw new RuntimeException("RequestQueueController already exists");
        instance = new RequestQueueController(context);
    }

    public static RequestQueueController getInstance() {
        if (instance == null)
            throw new RuntimeException("SettingsWrapper not init yet");
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}