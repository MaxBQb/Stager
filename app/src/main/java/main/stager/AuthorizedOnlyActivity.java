package main.stager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public abstract class AuthorizedOnlyActivity extends SmartActivity {

    // Firebase
    protected FirebaseAuth mAuth;
    protected DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        // Предотвращаем доступ неавторизованных пользователей
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, Authorization.class));
            finish();
        } else {
            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            mRef = mDatabase.getReference("stager-main-db");
        }
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
