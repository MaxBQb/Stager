package main.stager;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

public class StagerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    // Поодержка динамической смены языка на уровне приложения
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleController.restoreLocale(base));
    }
}