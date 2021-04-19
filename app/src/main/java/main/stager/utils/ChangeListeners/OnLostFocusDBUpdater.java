package main.stager.utils.ChangeListeners;

import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import main.stager.utils.DataProvider;

// Update DB <- info
public abstract class OnLostFocusDBUpdater implements View.OnFocusChangeListener {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) return;
            getDataRef(DataProvider.getInstance())
                    .setValue(((TextView)v).getText().toString());
    }

    public abstract DatabaseReference getDataRef(DataProvider dp);
}
