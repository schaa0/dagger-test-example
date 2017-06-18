package dagger.extension.example.service;

import javax.inject.Inject;

import dagger.AllowStubGeneration;
import dagger.extension.example.di.qualifier.ApiParam;
import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.model.forecast.tomorrow.TomorrowWeather;
import dagger.extension.example.model.search.SearchModel;
import dagger.extension.example.model.today.TodayWeather;
import io.reactivex.Observable;
import io.reactivex.Single;

public class WeatherApi {

    private final RetrofitWeatherApi api;
    private final String lang;
    private final String metric;
    private final String apiKey;

    @Inject
    @AllowStubGeneration
    public WeatherApi(RetrofitWeatherApi api,
                      @ApiParam("lang") String lang,
                      @ApiParam("units") String metric,
                      @ApiParam("key") String apiKey) {
        this.api = api;
        this.lang = lang;
        this.metric = metric;
        this.apiKey = apiKey;
    }

    public Observable<TodayWeather> getCurrentWeather(double longitude, double latitude) {
        return api.getCurrentWeather(longitude, latitude, metric, lang, apiKey);
    }

    public Observable<TomorrowWeather> getTomorrowWeather(double longitude, double latitude) {
        final int DAYS = 1;
        return api.getTomorrowWeather(longitude, latitude, metric, DAYS, lang, apiKey);
    }

    public Observable<ThreeHoursForecastWeather> getForecastWeather(double longitude, double latitude) {
        return api.getForecastWeather(longitude, latitude, metric, lang, apiKey);
    }

    public Single<SearchModel> searchWeather(String city) {
        return api.searchWeather(city, metric, lang, apiKey);
    }
}
