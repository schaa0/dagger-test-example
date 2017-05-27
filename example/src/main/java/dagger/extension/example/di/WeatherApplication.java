package dagger.extension.example.di;

import android.app.Activity;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.extension.example.view.ForecastActivity;
import dagger.extension.example.view.MainActivity;
import injector.Injector;

public class WeatherApplication extends DaggerApplication {

    ComponentSingleton singletonComponent;

    @Inject ComponentActivity.Builder componentActivityBuilder;
    @Inject ComponentForecastPresenter.Builder componentForecastBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        singletonComponent = getInjector().componentSingleton(this);
        singletonComponent.inject(this);
    }

    public void inject(MainActivity activity) {
        getInjector().componentActivity(componentActivityBuilder, activity).inject(activity);
    }

    public void inject(ForecastActivity forecastActivity, String forecastWeather) {
        getInjector().componentForecastPresenter(componentForecastBuilder, forecastWeather).inject(forecastActivity);
    }
}
