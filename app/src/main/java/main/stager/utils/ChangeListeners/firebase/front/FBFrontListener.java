package main.stager.utils.ChangeListeners.firebase.front;

import com.google.firebase.database.DatabaseReference;

// Update DB <- info
public abstract class FBFrontListener {
    protected DatabaseReference mRef;

    public FBFrontListener(DatabaseReference ref) { mRef = ref; }
}
