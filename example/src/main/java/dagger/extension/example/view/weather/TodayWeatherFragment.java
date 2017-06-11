package dagger.extension.example.view.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import javax.inject.Inject;
import javax.inject.Provider;

import dagger.android.support.AndroidSupportInjection;
import dagger.extension.example.view.main.MainActivity;

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
