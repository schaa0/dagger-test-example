package dagger.extension.example.service;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

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
    private String apiKey;

    @Inject
    public WeatherService(WeatherApi api, Scheduler scheduler, ImageRequestManager imageRequestManager, @Named("apiKey") String apiKey){
        this.api = api;
        this.scheduler = scheduler;
        this.imageRequestManager = imageRequestManager;
        this.apiKey = apiKey;
    }


    public Observable<Bitmap> loadIcon(String icon) {
        return imageRequestManager.load(icon);
    }

    public Observable<TomorrowWeather> getTomorrowWeather(double longitude, double latitude, int forecastDays)
    {
        return api.getTomorrowWeather(longitude, latitude, "metric", forecastDays, "de", apiKey)
                .subscribeOn(scheduler);
    }

    public Observable<ThreeHoursForecastWeather> getForecastWeather(double longitude, double latitude)
    {
        return api.getForecastWeather(longitude, latitude, "metric", "de", apiKey)
                  .subscribeOn(scheduler);
    }

    public Observable<TodayWeather> getCurrentWeather(double longitude, double latitude)
    {
        return api.getCurrentWeather(longitude, latitude, "metric", "de", apiKey)
                  .subscribeOn(scheduler);
    }

}
