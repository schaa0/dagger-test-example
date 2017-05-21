package dagger.extension.test;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class DaggerRunner<T extends Application> extends AndroidJUnitRunner
{

    private T application;
    private LifecycleCallback callback;

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        application = (T) super.newApplication(cl, parseApplicationClassName(), context);
        callback = new LifecycleCallback();
        application.registerActivityLifecycleCallbacks(callback);
        return application;
    }

    private String parseApplicationClassName()
    {
        Class<?> applicationClass = ((Class<?>)
                ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        return applicationClass.getName();
    }

    public T getApplication()
    {
        return application;
    }

    @Override
    public void callActivityOnDestroy(Activity activity)
    {
        super.callActivityOnDestroy(activity);
    }

    @Override
    public void callActivityOnRestart(Activity activity)
    {
        super.callActivityOnRestart(activity);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle bundle)
    {
        super.callActivityOnCreate(activity, bundle);
    }

    @Override
    public void callActivityOnStart(Activity activity)
    {
        super.callActivityOnStart(activity);
    }

    @Override
    public void callActivityOnStop(Activity activity)
    {
        super.callActivityOnStop(activity);
    }

    @Override
    public void callActivityOnResume(Activity activity)
    {
        super.callActivityOnResume(activity);
    }

    @Override
    public void callActivityOnPause(Activity activity)
    {
        super.callActivityOnPause(activity);
    }

    public void registerLifecycleCallback(Application.ActivityLifecycleCallbacks activityLifecycleCallbacks)
    {
        callback.addCallback(activityLifecycleCallbacks);
    }

    private static class LifecycleCallback implements Application.ActivityLifecycleCallbacks{
        private List<Application.ActivityLifecycleCallbacks> callbacks = new ArrayList<>();

        public void addCallback(Application.ActivityLifecycleCallbacks callback){
            callbacks.add(callback);
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState)
        {
            for (Application.ActivityLifecycleCallbacks callback : callbacks)
                callback.onActivityCreated(activity, savedInstanceState);
        }

        @Override
        public void onActivityStarted(Activity activity)
        {
            for (Application.ActivityLifecycleCallbacks callback : callbacks)
                callback.onActivityStarted(activity);
        }

        @Override
        public void onActivityResumed(Activity activity)
        {
            for (Application.ActivityLifecycleCallbacks callback : callbacks)
                callback.onActivityResumed(activity);
        }

        @Override
        public void onActivityPaused(Activity activity)
        {
            for (Application.ActivityLifecycleCallbacks callback : callbacks)
                callback.onActivityPaused(activity);
        }

        @Override
        public void onActivityStopped(Activity activity)
        {
            for (Application.ActivityLifecycleCallbacks callback : callbacks)
                callback.onActivityStopped(activity);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState)
        {
            for (Application.ActivityLifecycleCallbacks callback : callbacks)
                callback.onActivitySaveInstanceState(activity, outState);
        }

        @Override
        public void onActivityDestroyed(Activity activity)
        {
            for (Application.ActivityLifecycleCallbacks callback : callbacks)
                callback.onActivityDestroyed(activity);
        }
    }

}
