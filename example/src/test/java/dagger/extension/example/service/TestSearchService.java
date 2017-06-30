package dagger.extension.example.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.text.ParseException;

import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.model.search.SearchModel;
import dagger.extension.example.scheduler.CurrentThreadExecutor;
import dagger.extension.example.stubs.Fakes;
import dagger.extension.example.stubs.Responses;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

import static dagger.extension.example.stubs.Fakes.fakeResponse;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TestSearchService
{

    private static final String CITY = "city";

    @Mock
    WeatherApi weatherApi;
    private SearchService searchService;
    private SearchModel item;

    @Before
    public void setUp() throws Exception {
        item = fakeResponse(Responses.JSON.SEARCH);
        doReturn(Observable.just(item))
                .when(weatherApi).searchWeather(anyString());
        searchService = new SearchService(weatherApi, Schedulers.from(new CurrentThreadExecutor()));
    }

    @Test
    public void searchApiFunctionIsCalled() {
        final TestObserver<SearchModel> observer = new TestObserver<>();
        searchService.searchByCity(CITY).subscribe(observer);
        verify(weatherApi).searchWeather(CITY);
        observer.assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue(item);
    }

    @Test
    public void lastCityIsStoredTemporarily() throws ParseException, IOException {
        searchService.searchByCity(CITY);
        assertEquals(searchService.getLastSearch(), CITY);
    }

}
