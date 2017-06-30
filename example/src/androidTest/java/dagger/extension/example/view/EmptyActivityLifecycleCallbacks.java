package dagger.extension.example.view;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import dagger.extension.example.service.LocationService;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class EmptyActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    public EmptyActivityLifecycleCallbacks() {

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
