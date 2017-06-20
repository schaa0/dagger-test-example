package dagger.extension.example.view.weather;

import android.location.Location;

import javax.inject.Inject;

import dagger.extension.example.di.qualifier.RxObservable;
import dagger.extension.example.model.Weather;
import dagger.extension.example.service.LocationService;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.PermissionService;
import dagger.extension.example.service.WeatherService;
import dagger.extension.example.service.filter.TomorrowWeatherResponseFilter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static dagger.extension.example.di.qualifier.RxObservable.Type.PAGE;

public class TomorrowWeatherViewModel extends WeatherViewModel {

    @Inject
    public TomorrowWeatherViewModel(NavigationController navigation,
                                    @RxObservable(PAGE) Observable<Integer> pageChangeObservable,
                                    PermissionService permissionService,
                                    LocationService locationService,
                                    WeatherService weatherService,
                                    TomorrowWeatherResponseFilter weatherParser) {
        super(navigation, pageChangeObservable, permissionService, locationService, weatherService, weatherParser);
    }

    @Override
    protected void loadWeather(double longitude, double latitude)
    {
            dispatchRequestStarted();
            disposables.add(weatherService.getTomorrowWeather(longitude, latitude)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleWeatherResult, t -> {
                        this.showError(t);
                        this.clearView();
                    }));
    }

    @Override
    protected boolean isOwnPosition(int position) {
        return position == 1;
    }

    private void handleWeatherResult(Weather weather) {
        dispatchRequestFinished();
        updateState(weather);
        disposables.add(weatherService.loadIcon(weather.icon()).subscribe(icon::set));
    }

    public void loadForecastWeatherDataForTomorrow()
    {
        Location lastKnownLocation = locationService.lastLocation();
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

    private void handleForecastDataResult(String forecastData) {
        dispatchRequestFinished();
        navigate().toForecastActivity(forecastData);
    }
}
