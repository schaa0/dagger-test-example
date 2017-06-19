package dagger.extension.example.view.weather;

import android.view.View;

import javax.inject.Inject;

public class TodayWeatherFragment extends WeatherFragment
{

    @Inject TodayWeatherViewModel todayWeatherViewModel;

    @Override
    public void onWeatherIconClicked(View v)
    {
        todayWeatherViewModel.loadForecastWeatherDataForToday();
    }

    @Override
    protected TodayWeatherViewModel viewModel()
    {
        return todayWeatherViewModel;
    }

}
