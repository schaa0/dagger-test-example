package dagger.extension.example.view.forecast;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.extension.example.di.FragmentBindingsModule;
import dagger.extension.example.di.qualifier.ForecastWeather;
import dagger.extension.example.view.forecast.ForecastActivity;

@Module(includes = FragmentBindingsModule.class)
public abstract class ForecastActivityModule {

    @Binds
    public abstract AppCompatActivity appCompatActivity(ForecastActivity activity);

    @Provides
    @ForecastWeather("data")
    public static String forecastWeather(ForecastActivity activity) {
        return activity.forecastWeather;
    }

    @Provides
    public static FragmentManager fragmentManager(AppCompatActivity activity) {
        return activity.getSupportFragmentManager();
    }
}
