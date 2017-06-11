package dagger.extension.example.view.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.support.AndroidSupportInjection;
import dagger.extension.example.view.main.MainActivity;

public class TomorrowWeatherFragment extends WeatherFragment {

    @Inject TomorrowWeatherViewModel tomorrowWeatherViewModel;

    @Override
    public void onWeatherIconClicked(View v) {
        viewModel().loadForecastWeatherDataForTomorrow();
    }

    @Override
    protected TomorrowWeatherViewModel viewModel() {
        return tomorrowWeatherViewModel;
    }

}
