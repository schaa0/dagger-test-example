package dagger.extension.example.view.weather;

import android.location.Location;
import javax.inject.Inject;

import dagger.extension.example.model.Weather;
import dagger.extension.example.service.LocationProvider;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.PermissionManager;
import dagger.extension.example.service.WeatherService;
import dagger.extension.example.service.filter.TomorrowWeatherResponseFilter;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class TomorrowWeatherViewModel extends WeatherViewModel {

    private static final int FORECAST_DAYS = 1;

    @Inject
    public TomorrowWeatherViewModel(NavigationController navigation,
                                    PermissionManager permissionManager,
                                    LocationProvider locationProvider,
                                    WeatherService weatherService,
                                    TomorrowWeatherResponseFilter weatherParser) {
        super(navigation, permissionManager, locationProvider, weatherService, weatherParser);
    }

    @Override
    protected void loadWeather(double longitude, double latitude)
    {
            dispatchRequestStarted();
            disposables.add(weatherService.getTomorrowWeather(longitude, latitude, FORECAST_DAYS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleWeatherResult, this::showError));
    }

    private void handleWeatherResult(Weather weather) {
        dispatchRequestFinished();
        updateState(weather);
        disposables.add(weatherService.loadIcon(weather.icon()).subscribe(icon::set));
    }

    public void loadForecastWeatherDataForTomorrow()
    {
        Location lastKnownLocation = locationProvider.lastLocation();
        if (lastKnownLocation != null)
        {
            double longitude = lastKnownLocation.getLongitude();
            double latitude = lastKnownLocation.getLatitude();
            dispatchRequestStarted();
            weatherService.getForecastWeather(longitude, latitude)
                    .map(weatherParser::parse)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleForecastDataResult, this::showError);
        }
    }

    private void showError(Throwable t) {
        this.showError("", t.getMessage());
    }

    private void handleForecastDataResult(String forecastData) {
        dispatchRequestFinished();
        navigate().toForecastActivity(forecastData);
    }
}
