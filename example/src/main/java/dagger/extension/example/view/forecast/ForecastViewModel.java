package dagger.extension.example.view.forecast;

import android.databinding.ObservableField;

import javax.inject.Inject;

import dagger.AllowStubGeneration;
import dagger.Replaceable;
import dagger.extension.example.di.qualifier.ForecastWeather;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.view.main.NavigationViewModel;

public class ForecastViewModel extends NavigationViewModel {

    public final ObservableField<String> forecastWeatherData;

    @Inject @Replaceable
    public ForecastViewModel(NavigationController navigation, @ForecastWeather("data") String forecastWeatherData){
        super(navigation);
        this.forecastWeatherData = new ObservableField<>(forecastWeatherData);
    }

}
