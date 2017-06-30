package dagger.extension.example.di;

import android.app.Activity;
import android.content.Context;

import dagger.android.DispatchingAndroidInjector;

public class TestApplicationForRobolectric extends WeatherApplication {

    private TestWeatherApplication app = new TestWeatherApplication() {
        @Override
        protected ComponentSingleton.Builder createComponentBuilder() {
            return TestDaggerComponentSingleton.builder(this);
        }

        @Override
        public Context getApplicationContext() {
            return TestApplicationForRobolectric.this;
        }
    };

    @Override
    public void onCreate() {
        app.onCreate();
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return app.activityInjector();
    }

    public GraphDecorator decorator() {
        return app;
    }
}
