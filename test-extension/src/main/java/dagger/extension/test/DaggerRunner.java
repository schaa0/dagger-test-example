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

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        application = (T) super.newApplication(cl, parseApplicationClassName(), context);
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

}
