package dagger.extension.example.view.weather;

import android.location.Location;

import javax.inject.Inject;

import dagger.AllowStubGeneration;
import dagger.extension.example.di.qualifier.RxObservable;
import dagger.extension.example.di.qualifier.RxScheduler;
import dagger.extension.example.service.LocationService;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.PermissionService;
import dagger.extension.example.service.WeatherService;
import dagger.extension.example.service.filter.TodayWeatherResponseFilter;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static dagger.extension.example.di.qualifier.RxObservable.Type.PAGE;
import static dagger.extension.example.di.qualifier.RxScheduler.Type.MAIN;

public class TodayWeatherViewModel extends WeatherViewModel {

    @Inject @AllowStubGeneration
    public TodayWeatherViewModel(NavigationController navigation,
                                 @RxObservable(PAGE) Observable<Integer> pageChangeObservable,
                                 PermissionService permissionService,
                                 LocationService locationService, WeatherService weatherService,
                                 TodayWeatherResponseFilter weatherParser,
                                 @RxScheduler(MAIN) Scheduler androidScheduler) {
        super(navigation, pageChangeObservable, permissionService, locationService, weatherService, weatherParser, androidScheduler);
    }

    @Override
    protected void loadWeather(double longitude, double latitude) {
        dispatchRequestStarted();
        disposables.add(weatherService.getCurrentWeather(longitude, latitude)
                      .observeOn(androidScheduler)
                      .subscribe(weather ->
                      {
                          updateState(weather);
                          disposables.add(weatherService.loadIcon(weather.icon()).subscribe(icon::set));
                      }, t -> {
                          this.showError(t);
                          this.clearView();
                      }, this::dispatchRequestFinished)
        );
    }

    @Override
    protected boolean isOwnPosition(int position) {
        return position == 0;
    }

    public void loadForecastWeatherDataForToday() {
        Location lastKnownLocation = locationService.lastLocation();
        if (lastKnownLocation != null)
        {
            double longitude = lastKnownLocation.getLongitude();
            double latitude = lastKnownLocation.getLatitude();
            dispatchRequestStarted();
            weatherService.getForecastWeather(longitude, latitude)
                          .map(weatherParser::parse)
                          .observeOn(androidScheduler)
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
