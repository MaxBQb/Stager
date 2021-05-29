package main.stager.utils.pushNotifications;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.database.annotations.NotNull;
import java.util.Set;

public class ListenedEventsController {
    private SharedPreferences events;

    private static final String LISTENED_EVENTS = "Stager.main.settings.events.listened";

    public ListenedEventsController(Context c) {
        events = c.getApplicationContext()
                  .getSharedPreferences(LISTENED_EVENTS, Context.MODE_PRIVATE);
    }

    public @NotNull Set<String> getListenedEvents() {
        return events.getAll().keySet();
    }

    public boolean isEventListened(String name) {
        return events.getBoolean(name, false);
    }

    public void setEventListened(String name, boolean isListened) {
        events.edit().putBoolean(name, isListened).apply();
    }

    public void setEventListened(String name) {
        setEventListened(name, true);
    }

    public void setEventNotListened(String name) {
        setEventListened(name, false);
    }

    public void reset() {
        events.edit().clear().apply();
    }
}
