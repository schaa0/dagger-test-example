package dagger.extension.example.di;

import android.app.Activity;
import android.util.Log;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class WeatherApplication extends DaggerHookApplication implements HasActivityInjector{

    @Inject DispatchingAndroidInjector<Activity> activityInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        long start = System.nanoTime();
        DaggerComponentSingleton.builder(this).create(this).inject(this);
        long end = System.nanoTime() - start;
        Log.d(WeatherApplication.class.getName(), String.format("Average inject time: %d ms", Math.round(end / 1000.0 / 1000.0)));
    }

    public DispatchingAndroidInjector<Activity> activityInjector() {
        return this.activityInjector;
    }
}
