package dagger.extension.example;

import android.support.multidex.MultiDexApplication;

import dagger.Config;

@Config(
        applicationClass = ExampleApplication.class,
        baseApplicationClass = "android.support.multidex.MultiDexApplication"
)
public class AppConfig
{
}
