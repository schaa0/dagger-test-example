package dagger.extension.example.vm;

import android.location.Location;
import android.os.AsyncTask;

import java.io.IOException;
import java.text.ParseException;

import javax.inject.Inject;

import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.model.forecast.tomorrow.TomorrowWeather;
import dagger.extension.example.service.LocationProvider;
import dagger.extension.example.service.PermissionManager;
import dagger.extension.example.service.WeatherService;
import dagger.extension.example.service.filter.TomorrowWeatherResponseFilter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class TomorrowWeatherViewModel extends WeatherViewModel {
    private static final int FORECAST_DAYS = 1;


    @Inject
    public TomorrowWeatherViewModel(PermissionManager permissionManager,
                                    LocationProvider locationProvider,
                                    WeatherService weatherService,
                                    TomorrowWeatherResponseFilter weatherParser) {
        super(permissionManager, locationProvider, weatherService, weatherParser);
    }

    @Override
    protected void loadWeather(double longitude, double latitude)
    {
            dispatchRequestStarted();
            weatherService.getTomorrowWeather(longitude, latitude, FORECAST_DAYS)
                    .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(weather -> {
                        dispatchRequestFinished();
                        updateState(weather);
                        disposables.add(weatherService.loadIcon(weather.icon()).subscribe(icon::set));
                    }, throwable -> dispatchRequestFinished());
    }

    public void loadForecastWeatherDataForTomorrow(Result<String> result)
    {
        Location lastKnownLocation = locationProvider.lastLocation();
        if (lastKnownLocation != null)
        {
            double longitude = lastKnownLocation.getLongitude();
            double latitude = lastKnownLocation.getLatitude();
            dispatchRequestStarted();
            weatherService.getForecastWeather(longitude, latitude)
                    .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(weather -> {
                        dispatchRequestFinished();
                        result.onResult(weatherParser.parse(weather));
                    }, throwable -> dispatchRequestFinished());
        }
    }

}
