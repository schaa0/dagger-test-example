package dagger.extension.example.view.weather;

import android.location.Location;

import javax.inject.Inject;

import dagger.extension.example.service.LocationProvider;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.PermissionService;
import dagger.extension.example.service.WeatherService;
import dagger.extension.example.service.filter.TodayWeatherResponseFilter;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class TodayWeatherViewModel extends WeatherViewModel {

    @Inject
    public TodayWeatherViewModel(NavigationController navigation, PermissionService permissionService, LocationProvider
            locationProvider, WeatherService weatherService, TodayWeatherResponseFilter weatherParser) {
        super(navigation, permissionService, locationProvider, weatherService, weatherParser);
    }

    @Override
    protected void loadWeather(double longitude, double latitude) {
        dispatchRequestStarted();
        disposables.add(weatherService.getCurrentWeather(longitude, latitude)
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(weather ->
                      {
                          updateState(weather);
                          dispatchRequestFinished();
                          disposables.add(weatherService.loadIcon(weather.icon()).subscribe(icon::set));
                      }, this::showError));
    }

    private void showError(Throwable t) {
        this.showError("", t.getMessage());
    }

    public void loadForecastWeatherDataForToday() {
        Location lastKnownLocation = locationProvider.lastLocation();
        if (lastKnownLocation != null)
        {
            double longitude = lastKnownLocation.getLongitude();
            double latitude = lastKnownLocation.getLatitude();
            dispatchRequestStarted();
            weatherService.getForecastWeather(longitude, latitude)
                          .map(weatherParser::parse)
                          .observeOn(AndroidSchedulers.mainThread())
                          .subscribe(forecastData ->
                          {
                              dispatchRequestFinished();
                              navigateToForecastActivity(forecastData);
                          }, this::showError);
        }
    }

    private void navigateToForecastActivity(String forecastData) {
        navigate().toForecastActivity(forecastData);
    }
}
