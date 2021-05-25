package main.stager.utils.BroadcasterHolders;

import java.util.HashMap;
import java.util.Map;


/* Class that allows to add event listeners to
*  objects, which isn't created yet
* */
public class BroadcastersHolder<TB> {
    protected Map<String, TB> broadcasters;
    private Map<String, OnBroadcasterGained<TB>> onBroadcastersGained;

    BroadcastersHolder() {
        this(new HashMap<>());
    }

    BroadcastersHolder(Map<String, OnBroadcasterGained<TB>> onBroadcastersGained) {
        this.onBroadcastersGained = onBroadcastersGained;
        broadcasters = new HashMap<>();
    }

    public BroadcastersHolder<TB> addOnBroadcastGainListener(String key, OnBroadcasterGained<TB> listener) {
        onBroadcastersGained.put(key, listener);
        return this;
    }

    public void postBroadcaster(String key, TB broadcaster) {
        broadcasters.put(key, broadcaster);
        OnBroadcasterGained<TB> listener = onBroadcastersGained.get(key);
        if (listener != null)
            listener.addListeners(broadcaster);
    }
}
