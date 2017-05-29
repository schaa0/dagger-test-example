package dagger.extension.example.view.weather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import javax.inject.Inject;
import javax.inject.Provider;

import dagger.extension.example.view.main.MainActivity;

public class TodayWeatherFragment extends WeatherFragment
{

    @Inject Provider<TodayWeatherViewModel> vmProvider;
    private TodayWeatherViewModel todayWeatherViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        ((MainActivity)getActivity()).inject(this);
        todayWeatherViewModel = vmProvider.get();
        super.onActivityCreated(savedInstanceState);
    }

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
