package dagger.extension.example.vm;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;

import javax.inject.Inject;

public class ForecastViewModel extends BaseObservable {

    private ObservableField<String> forecastWeather;

    @Inject
    public ForecastViewModel(String forecastWeather){
        this.forecastWeather = new ObservableField<>(forecastWeather);
    }

}
