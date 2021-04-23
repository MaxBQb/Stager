package main.stager.utils.ChangeListeners;

import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;

// Update DB <- info
public class OnLostFocusDBUpdater implements View.OnFocusChangeListener {
    private DatabaseReference mRef;

    public OnLostFocusDBUpdater(DatabaseReference ref) { mRef = ref; }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) return;
                mRef.setValue(((TextView)v).getText().toString());
    }
}
