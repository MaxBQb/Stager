package main.stager.utils.BroadcasterHolders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/* Class that allows to add event listeners to
*  objects, which isn't created yet
* */
public class BroadcastersHolder<TB> {
    private Map<String, Set<OnBroadcasterGained<TB>>> onBroadcastersGainedListeners;

    BroadcastersHolder() {
        this(new HashMap<>());
    }

    BroadcastersHolder(Map<String, Set<OnBroadcasterGained<TB>>> onBroadcastersGainedListeners) {
        this.onBroadcastersGainedListeners = onBroadcastersGainedListeners;
    }

    public BroadcastersHolder<TB> addOnBroadcastGainListener(String key, OnBroadcasterGained<TB> listener) {
        if (!onBroadcastersGainedListeners.containsKey(key))
            onBroadcastersGainedListeners.put(key, new HashSet<>());
        Set<OnBroadcasterGained<TB>> onBroadcasterGainedListeners =
                                    onBroadcastersGainedListeners.get(key);
        if (onBroadcasterGainedListeners != null)
            onBroadcasterGainedListeners.add(listener);
        return this;
    }

    public void postBroadcaster(String key, TB broadcaster) {
        Set<OnBroadcasterGained<TB>> onBroadcasterGainedListeners =
                                    onBroadcastersGainedListeners.get(key);
        if (onBroadcasterGainedListeners == null) return;
        for (OnBroadcasterGained<TB> onBroadcasterGained : onBroadcasterGainedListeners)
            if (onBroadcasterGained != null)
                onBroadcasterGained.addListeners(broadcaster);
    }
}
