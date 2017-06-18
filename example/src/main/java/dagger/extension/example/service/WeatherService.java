package dagger.extension.example.service;

import android.graphics.Bitmap;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.extension.example.model.Weather;
import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.model.forecast.tomorrow.TomorrowWeather;
import dagger.extension.example.model.today.TodayWeather;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

@Singleton
public class WeatherService
{

    private final WeatherApi api;
    private final Scheduler scheduler;
    private final ImageRequestManager imageRequestManager;

    @Inject
    public WeatherService(WeatherApi api, Scheduler scheduler, ImageRequestManager imageRequestManager){
        this.api = api;
        this.scheduler = scheduler;
        this.imageRequestManager = imageRequestManager;
    }


    public Observable<Bitmap> loadIcon(String icon) {
        return imageRequestManager.load(icon)
                                  .take(1);
    }

    public Observable<TomorrowWeather> getTomorrowWeather(double longitude, double latitude)
    {
        return api.getTomorrowWeather(longitude, latitude)
                .subscribeOn(scheduler)
                .take(1);
    }

    public Observable<ThreeHoursForecastWeather> getForecastWeather(double longitude, double latitude)
    {
        return api.getForecastWeather(longitude, latitude)
                  .subscribeOn(scheduler)
                  .take(1);
    }

    public Observable<TodayWeather> getCurrentWeather(double longitude, double latitude)
    {
        return api.getCurrentWeather(longitude, latitude)
                  .subscribeOn(scheduler)
                  .take(1);
    }

}
