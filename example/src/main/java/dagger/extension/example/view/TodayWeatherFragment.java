package dagger.extension.example.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import javax.inject.Inject;
import javax.inject.Provider;
import dagger.extension.example.vm.TodayWeatherViewModel;

public class TodayWeatherFragment extends WeatherFragment
{
    @Inject
    Provider<TodayWeatherViewModel> vmProvider;
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
        todayWeatherViewModel.loadForecastWeatherDataForToday(item -> {
            Intent intent = new Intent(getActivity(), ForecastActivity.class);
            intent.putExtra(ForecastActivity.KEY_FORECAST, item);
            startActivity(intent);
        });
    }

    @Override
    protected TodayWeatherViewModel getViewModel()
    {
        return todayWeatherViewModel;
    }

}
