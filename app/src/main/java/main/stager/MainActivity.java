package main.stager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;

public class MainActivity extends SmartActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                SettingsActivity.openSettings(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}