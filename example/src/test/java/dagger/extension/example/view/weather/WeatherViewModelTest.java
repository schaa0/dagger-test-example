package dagger.extension.example.view.weather;

import java.io.IOException;
import java.text.ParseException;

public interface WeatherViewModelTest {
    void shouldRequestPermissionsIfRequired();
    void shouldNotInteractWithLocationManagerIfPermissionIsNotGranted();
    void mustNotRequestWeatherIfThereIsNoLastKnownLocation();
    void shouldRequestWeatherIfPermissionIsGrantedAndLocationIsPresent() throws IOException;
    void shouldNotLoadForecastsIfNoLocationIsPresent();
    void shouldLoadForecastsIfLocationIsPresent() throws ParseException, IOException;
    void locationChangeShouldTriggerReloadOfWeatherData();
    void grantingPermissionsShouldTriggerReloadOfWeatherDataIfGpsIsEnabled();
    void denieingPermissionsShouldNotTriggerReloadOfWeatherData();
    void refreshesIfNecessary();
    void loadsDataWhenGpsGotActivated();
}
