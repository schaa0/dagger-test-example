package dagger.extension.example.service;

import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.model.forecast.tomorrow.TomorrowWeather;
import dagger.extension.example.model.today.TodayWeather;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi
{

    @GET("/data/2.5/weather")
    Observable<TodayWeather> getCurrentWeather(@Query("lon") double longitude, @Query("lat") double
            latitude, @Query("units") String metric, @Query("lang") String lang, @Query("appid")
            String apiKey);

    @GET("/data/2.5/forecast/daily")
    Observable<TomorrowWeather> getTomorrowWeather(@Query("lon") double longitude, @Query("lat") double
            latitude, @Query("units") String metric, @Query("cnt") int days, @Query("lang")
            String lang, @Query("appid") String apiKey);

    @GET("/data/2.5/forecast")
    Observable<ThreeHoursForecastWeather> getForecastWeather(@Query("lon") double longitude, @Query
            ("lat") double latitude, @Query("units") String metric, @Query("lang") String lang, @Query("appid") String apiKey);
}
