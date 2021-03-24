package main.stager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;

public abstract class AuthorizedOnlyActivity extends SmartActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Prevent unauthorized access
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Прикрепляем значок настроек на панель инструментов
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() != R.id.action_settings)
            return super.onOptionsItemSelected(item);
        SettingsActivity.openSettings(this);
        return true;
    }
}
