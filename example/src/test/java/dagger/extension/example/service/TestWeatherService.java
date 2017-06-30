package dagger.extension.example.service;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.text.ParseException;

import dagger.extension.example.model.Weather;
import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.scheduler.CurrentThreadExecutor;
import dagger.extension.example.stubs.Fakes;
import dagger.extension.example.stubs.Responses;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TestWeatherService
{

    @Mock WeatherApi weatherApi;
    @Mock ImageRequestManager imageRequestManager;
    @Mock Bitmap bitmap;

    WeatherService weatherService;

    @Before
    public void setUp() throws Exception {
        weatherService = new WeatherService(weatherApi, Schedulers.from(new CurrentThreadExecutor()), imageRequestManager);
    }

    @Test
    public void shouldLoadIcon() {
        doReturn(Observable.just(bitmap)).when(imageRequestManager).load(anyString());
        TestObserver<Bitmap> testObserver = new TestObserver<>();
        imageRequestManager.load("someId").subscribe(testObserver);
        testObserver.assertValue(bitmap);
    }

    @Test
    public void shouldLoadForecastWeather() throws ParseException, IOException {
        final ThreeHoursForecastWeather item = Fakes.fakeResponse(Responses.JSON.THREE_HOUR_FORECAST);
        doReturn(Observable.just(item))
                .when(weatherApi).getForecastWeather(anyDouble(), anyDouble());
        TestObserver<ThreeHoursForecastWeather> testObserver = new TestObserver<>();
        weatherService.getForecastWeather(1.0, 1.0).subscribe(testObserver);
        testObserver.assertSubscribed().assertNoErrors().assertComplete().assertValue(item);
    }

}
