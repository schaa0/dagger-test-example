package dagger.extension.example.decoration;

import android.location.Location;

import java.io.IOException;

import dagger.extension.example.di.GraphDecorator;
import dagger.extension.example.service.LocationService;
import dagger.extension.example.service.WeatherApi;
import dagger.extension.example.stubs.DateProviderStub;
import dagger.extension.example.stubs.Fakes;
import dagger.extension.example.stubs.Responses;
import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

import static dagger.extension.example.stubs.Fakes.fakeResponse;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doReturn;

public class DefaultDecorations {

    public static final double FAKE_LONGITUDE = 1.0;
    public static final double FAKE_LATITUDE = 1.0;

    public static void defaultWeatherApiAnswers(WeatherApi weatherApi) {
        try {
            doReturn(Observable.just(fakeResponse(Responses.JSON.TOMORROW_WEATHER)))
                    .when(weatherApi).getTomorrowWeather(anyDouble(), anyDouble());

            doReturn(Observable.just(fakeResponse(Responses.JSON.TODAY_WEATHER)))
                    .when(weatherApi).getCurrentWeather(anyDouble(), anyDouble());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
