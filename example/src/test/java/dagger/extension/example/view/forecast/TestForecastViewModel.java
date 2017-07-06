package dagger.extension.example.view.forecast;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dagger.extension.example.service.NavigationController;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TestForecastViewModel {

    @Mock ForecastViewModel forecastViewModel;
    @Mock NavigationController navigationController;
    private String forecastWeatherData;

    @Before
    public void setUp() throws Exception {
        forecastWeatherData = "test data";
        forecastViewModel = new ForecastViewModel(navigationController, forecastWeatherData);
    }

    @Test
    public void weatherDataIsSetAfterCreationOfViewModel() {
        String actual = forecastViewModel.forecastWeatherData.get();
        assertEquals(forecastWeatherData, actual);
    }

}
