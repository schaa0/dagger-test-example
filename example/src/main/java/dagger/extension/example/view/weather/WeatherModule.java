package dagger.extension.example.view.weather;

import android.os.Bundle;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class WeatherModule<T extends WeatherFragment> {

    @Binds
    public abstract WeatherFragment fragment(T fragment);

    @Provides
    public static Bundle provideSavedInstanceState(WeatherFragment weatherFragment) {
        return weatherFragment.getSavedInstanceState();
    }

    @Provides
    public static WeatherViewModel.WeatherViewModelState provideState(Bundle savedInstanceState) {
        return savedInstanceState != null ?
                savedInstanceState.getParcelable(WeatherFragment.VIEW_MODEL_STATE) :
                new WeatherViewModel.WeatherViewModelState();
    }

}
