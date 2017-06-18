package dagger.extension.example.view.weather;

import android.support.v4.app.Fragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class WeatherModule<T extends WeatherFragment> {

    @Binds
    public abstract WeatherFragment fragment(T fragment);

    @Provides
    public static WeatherViewModel.WeatherViewModelState provideState(WeatherFragment weatherFragment) {
        return weatherFragment.state;
    }

}
