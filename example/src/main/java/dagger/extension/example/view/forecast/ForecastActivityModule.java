package dagger.extension.example.view.forecast;

import android.content.Intent;

import dagger.Module;
import dagger.Provides;
import dagger.extension.example.di.ActivityModule;
import dagger.extension.example.di.qualifier.ForecastWeather;
import dagger.extension.example.view.error.ComponentErrorDialogFragment;

@Module(subcomponents = {ComponentErrorDialogFragment.class})
public abstract class ForecastActivityModule extends ActivityModule<ForecastActivity> {

    @Provides
    @ForecastWeather("data")
    public static String forecastWeather(Intent intent) {
        return intent.getStringExtra(ForecastActivity.INTENT_KEY_FORECAST);
    }

}
