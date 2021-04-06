package main.stager;

import android.app.Application;

public class StagerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocaleController.init(this);
    }
}