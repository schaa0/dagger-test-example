package dagger.extension.example.view;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import javax.inject.Inject;
import javax.inject.Provider;
import dagger.extension.example.vm.TomorrowWeatherViewModel;

public class TomorrowWeatherFragment extends WeatherFragment {

    @Inject Provider<TomorrowWeatherViewModel> vmProvider;
    private TomorrowWeatherViewModel tomorrowWeatherViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        ((MainActivity)getActivity()).inject(this);
        tomorrowWeatherViewModel = vmProvider.get();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onWeatherIconClicked(View v) {
        getViewModel().loadForecastWeatherDataForTomorrow(item ->
        {
            Intent intent = new Intent(getActivity(), ForecastActivity.class);
            intent.putExtra(ForecastActivity.KEY_FORECAST, item);
            startActivity(intent);
        });
    }

    @Override
    protected TomorrowWeatherViewModel getViewModel() {
        return tomorrowWeatherViewModel;
    }

}
