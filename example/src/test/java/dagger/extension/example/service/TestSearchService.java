package dagger.extension.example.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.text.ParseException;

import dagger.extension.example.scheduler.CurrentThreadExecutor;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;
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

    @Before
    public void setUp() throws Exception {
        doReturn(Observable.empty()).when(weatherApi).searchWeather(anyString());
        searchService = new SearchService(weatherApi, Schedulers.from(new CurrentThreadExecutor()));
    }

    @Test
    public void searchApiFunctionIsCalled() {
        searchService.searchByCity(CITY);
        verify(weatherApi).searchWeather(CITY);
    }

    @Test
    public void lastCityIsStoredTemporarily() throws ParseException, IOException {
        searchService.searchByCity(CITY);
        assertEquals(searchService.getLastSearch(), CITY);
    }

}
