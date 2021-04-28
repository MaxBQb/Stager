package main.stager.utils.ChangeListeners.firebase.front;

import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;

import main.stager.utils.DataProvider;

public class OnLostFocusDBUpdater extends FBFrontListener
        implements View.OnFocusChangeListener {

    public OnLostFocusDBUpdater(DatabaseReference ref) { super(ref); }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) return;
               DataProvider.trySetValue(mRef, ((TextView)v).getText().toString());
    }
}
