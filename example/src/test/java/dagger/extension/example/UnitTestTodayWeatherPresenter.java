package dagger.extension.example;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import java.io.IOException;
import java.text.ParseException;

import dagger.extension.example.di.TestWeatherApplication;
import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.model.today.TodayWeather;
import dagger.extension.example.service.LocationProvider;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.PermissionManager;
import dagger.extension.example.service.PermissionResult;
import dagger.extension.example.service.WeatherService;
import dagger.extension.example.service.filter.TodayWeatherResponseFilter;
import dagger.extension.example.stubs.Responses;
import dagger.extension.example.view.weather.TodayWeatherViewModel;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings("MissingPermission")
@Config(sdk = 21, constants = BuildConfig.class, application = TestWeatherApplication.class)
@RunWith(RobolectricTestRunner.class)
public class UnitTestTodayWeatherPresenter
{

    static final int RC_PERM_FINE_LOCATION = TodayWeatherViewModel.REQUEST_CODE_PERM_ACCESS_FINE_LOCATION;
    static final int RC_PERM_COARSE_LOCATION = TodayWeatherViewModel.REQUEST_CODE_PERM_ACCESS_COARSE_LOCATION;
    static final String PERM_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    static final String PERM_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    @Mock NavigationController navigation;
    @Mock PermissionManager pm;
    @Mock LocationProvider locationProvider;
    @Mock TodayWeatherResponseFilter weatherParser;
    @Mock WeatherService weatherService;

    private TodayWeatherViewModel vm;
    private PublishSubject<PermissionResult> permissionSubject;
    private PublishSubject<Location> locationSubject;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        permissionSubject = PublishSubject.create();
        locationSubject = PublishSubject.create();
        doReturn(permissionSubject).when(pm).onPermissionGranted();
        doReturn(locationSubject).when(locationProvider).onNewLocation();
        doReturn(Observable.empty()).when(weatherService).getCurrentWeather(anyDouble(), anyDouble());
        vm = new TodayWeatherViewModel(navigation, pm, locationProvider, weatherService, weatherParser);
    }

    @Test
    public void shouldRequestPermissionsIfRequired() {
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(false);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(false);
        vm.onViewAttached();
        verify(pm).requestPermission(PERM_FINE_LOCATION, RC_PERM_FINE_LOCATION);
        verify(pm).requestPermission(PERM_COARSE_LOCATION, RC_PERM_COARSE_LOCATION);
    }

    @Test
    public void shouldNotInteractWithLocationManagerIfPermissionIsNotGranted() {
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(false);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(false);
        vm.onViewAttached();
        reset(locationProvider);
        vm.onPermissionsResult(RC_PERM_COARSE_LOCATION, new String[]{PERM_COARSE_LOCATION},
                new int[]{PackageManager.PERMISSION_DENIED});
        verifyZeroInteractions(locationProvider);
        vm.onPermissionsResult(TodayWeatherViewModel.REQUEST_CODE_PERM_ACCESS_FINE_LOCATION,
                new String[]{PERM_FINE_LOCATION}, new int[]{PackageManager.PERMISSION_DENIED});
        verifyZeroInteractions(locationProvider);
    }

    @Test
    public void mustNotRequestWeatherIfThereIsNoLastKnownLocation() {
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(true);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(true);
        when(locationProvider.lastLocation()).thenReturn(null);
        vm.onViewAttached();
        verifyZeroInteractions(weatherService);
    }

    @Test
    public void shouldRequestWeatherIfPermissionIsGrantedAndLocationIsPresent() throws IOException
    {
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(true);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(true);
        Location location = createLocation(1.0, 1.0);
        when(weatherService.getCurrentWeather(1.0, 1.0))
                .thenReturn(Observable.just(new Gson().fromJson(Responses.TODAY_WEATHER, TodayWeather.class)));
        when(locationProvider.lastLocation()).thenReturn(location);
        vm.onViewAttached();
        verify(pm, atLeastOnce()).isPermissionGranted(anyString());
    }

    @NonNull
    private Location createLocation(double longitude, double latitude) {
        Location location = new Location("");
        location.setLatitude(longitude);
        location.setLongitude(latitude);
        return location;
    }

    @Test
    public void shouldNotLoadForecastsIfNoLocationIsPresent() {
        vm.onViewAttached();
        reset(locationProvider, pm);
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(true);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(true);
        when(locationProvider.lastLocation()).thenReturn(null);
        vm.loadForecastWeatherDataForToday();
        verifyZeroInteractions(weatherService);
    }

    @Test
    public void shouldLoadForecastsIfLocationIsPresent() throws ParseException, IOException
    {
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(true);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(true);
        double longitude = 1.0;
        double latitude = 1.0;
        when(locationProvider.lastLocation()).thenReturn(createLocation(longitude, latitude));
        String expectedResult = Responses.createExpectedResult();
        when(weatherParser.parse(any(ThreeHoursForecastWeather.class))).thenReturn(expectedResult);
        when(weatherService.getForecastWeather(longitude, latitude))
                .thenReturn(Observable.just(new Gson().fromJson(Responses.FORECAST_RESULT, ThreeHoursForecastWeather.class)));
        vm.loadForecastWeatherDataForToday();
        verify(navigation).toForecastActivity(expectedResult);
    }

    @Test
    public void locationChangeShouldTriggerReloadOfWeatherData() {
        vm.onViewAttached();
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(true);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(true);
        double longlat = 6.0;
        doReturn(Observable.empty()).when(weatherService).getCurrentWeather(longlat, longlat);
        locationSubject.onNext(createLocation(longlat, longlat));
        verify(weatherService).getCurrentWeather(longlat, longlat);
    }

    @Test
    public void grantingPermissionsShouldTriggerReloadOfWeatherData() {
        when(locationProvider.lastLocation()).thenReturn(createLocation(1.0, 1.0));
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(false);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(false);
        vm.onViewAttached();
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(true);
        permissionSubject.onNext(new PermissionResult(
                TodayWeatherViewModel.REQUEST_CODE_PERM_ACCESS_FINE_LOCATION,
                new String[]{PERM_FINE_LOCATION},
                new int[]{PackageManager.PERMISSION_GRANTED}
        ));
        verifyZeroInteractions(weatherService);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(true);
        permissionSubject.onNext(new PermissionResult(
                TodayWeatherViewModel.REQUEST_CODE_PERM_ACCESS_COARSE_LOCATION,
                new String[]{PERM_COARSE_LOCATION},
                new int[]{PackageManager.PERMISSION_GRANTED}
        ));
        verify(weatherService).getCurrentWeather(1.0, 1.0);
    }

    @Test
    public void denieingPermissionsShouldNotTriggerReloadOfWeatherData() {
        when(locationProvider.lastLocation()).thenReturn(createLocation(1.0, 1.0));
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(false);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(false);
        vm.onViewAttached();
        PermissionResult result = new PermissionResult(
                TodayWeatherViewModel.REQUEST_CODE_PERM_ACCESS_FINE_LOCATION,
                new String[]{PERM_COARSE_LOCATION},
                new int[]{PackageManager.PERMISSION_DENIED}
        );
        permissionSubject.onNext(result);
        verifyZeroInteractions(weatherService);
    }
}