package dagger.extension.example.vm;

import android.location.Location;
import android.os.AsyncTask;

import javax.inject.Inject;

import dagger.extension.example.service.LocationProvider;
import dagger.extension.example.service.PermissionManager;
import dagger.extension.example.service.WeatherService;
import dagger.extension.example.service.filter.TodayWeatherResponseFilter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TodayWeatherViewModel extends WeatherViewModel {

    @Inject
    public TodayWeatherViewModel(PermissionManager permissionManager, LocationProvider
            locationProvider, WeatherService weatherService, TodayWeatherResponseFilter
                                         weatherParser) {
        super(permissionManager, locationProvider, weatherService, weatherParser);
    }

    @Override
    protected void loadWeather(double longitude, double latitude) {
        dispatchRequestStarted();
        weatherService.getCurrentWeather(longitude, latitude)
                      .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(weather ->
                      {
                          updateState(weather);
                          dispatchRequestFinished();
                          disposables.add(weatherService.loadIcon(weather.icon()).subscribe(icon::set));
                      }, throwable -> dispatchRequestFinished());
    }

    public void loadForecastWeatherDataForToday(Result<String> result) {
        Location lastKnownLocation = locationProvider.lastLocation();
        if (lastKnownLocation != null)
        {
            double longitude = lastKnownLocation.getLongitude();
            double latitude = lastKnownLocation.getLatitude();
            dispatchRequestStarted();
            weatherService.getForecastWeather(longitude, latitude)
                          .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                          .observeOn(AndroidSchedulers.mainThread())
                          .subscribe(weather ->
                          {
                              dispatchRequestFinished();
                              final String forecastData = weatherParser.parse(weather);
                              result.onResult(forecastData);
                          }, throwable -> dispatchRequestFinished());
        }
    }

}
