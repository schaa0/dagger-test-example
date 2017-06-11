package dagger.extension.example.di;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import javax.inject.Inject;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class WeatherApplication extends DaggerHookApplication implements HasActivityInjector{

    @Inject DispatchingAndroidInjector<Activity> activityInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerComponentSingleton.builder(this).create(this).inject(this);
    }

    public DispatchingAndroidInjector<Activity> activityInjector() {
        return this.activityInjector;
    }


}
