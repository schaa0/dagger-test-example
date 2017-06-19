package dagger.extension.example.view.weather;

import android.view.View;

import javax.inject.Inject;

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
