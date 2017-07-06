package dagger.extension.example.view.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.text.ParseException;

import dagger.extension.example.model.forecast.threehours.ThreeHoursForecastWeather;
import dagger.extension.example.scheduler.CurrentThreadExecutor;
import dagger.extension.example.service.LocationService;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.PermissionResult;
import dagger.extension.example.service.PermissionService;
import dagger.extension.example.service.WeatherService;
import dagger.extension.example.service.filter.TodayWeatherResponseFilter;
import dagger.extension.example.stubs.Responses;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

import static dagger.extension.example.stubs.Fakes.fakeResponse;
import static dagger.extension.example.stubs.Fakes.location;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings("MissingPermission")
//@Config(sdk = 21, constants = BuildConfig.class, application = TestWeatherApplication.class)
@RunWith(MockitoJUnitRunner.class)
public class TestTodayWeatherViewModel implements WeatherViewModelTest
{

    private static final int RC_PERM_FINE_LOCATION = TodayWeatherViewModel.REQUEST_CODE_PERM_ACCESS_FINE_LOCATION;
    private static final int RC_PERM_COARSE_LOCATION = TodayWeatherViewModel.REQUEST_CODE_PERM_ACCESS_COARSE_LOCATION;
    private static final String PERM_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERM_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;

    @Mock NavigationController navigation;
    @Mock PermissionService pm;
    @Mock LocationService locationService;
    @Mock TodayWeatherResponseFilter weatherParser;
    @Mock WeatherService weatherService;

    private TodayWeatherViewModel vm;
    private PublishSubject<PermissionResult> permissionSubject;
    private PublishSubject<Location> locationSubject;
    private PublishSubject<Integer> pageChanged;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        permissionSubject = PublishSubject.create();
        locationSubject = PublishSubject.create();
        pageChanged = PublishSubject.create();

        doReturn(permissionSubject).when(pm).onPermissionGranted();
        doReturn(locationSubject).when(locationService).onNewLocation();
        doReturn(true).when(locationService).isGpsProviderEnabled();
        doReturn(Observable.empty()).when(weatherService).getCurrentWeather(anyDouble(), anyDouble());

        final Scheduler androidScheduler = Schedulers.from(new CurrentThreadExecutor());
        vm = new TodayWeatherViewModel(navigation, pageChanged, pm, locationService, weatherService, weatherParser, androidScheduler);
        vm.state = new WeatherViewModel.WeatherViewModelState();
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
        reset(locationService);
        vm.onPermissionsResult(RC_PERM_COARSE_LOCATION, new String[]{PERM_COARSE_LOCATION},
                new int[]{PackageManager.PERMISSION_DENIED});
        verifyZeroInteractions(locationService);
        vm.onPermissionsResult(TodayWeatherViewModel.REQUEST_CODE_PERM_ACCESS_FINE_LOCATION,
                new String[]{PERM_FINE_LOCATION}, new int[]{PackageManager.PERMISSION_DENIED});
        verifyZeroInteractions(locationService);
    }

    @Test
    public void mustNotRequestWeatherIfThereIsNoLastKnownLocation() {
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(true);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(true);
        when(locationService.lastLocation()).thenReturn(null);
        vm.onViewAttached();
        verifyZeroInteractions(weatherService);
    }

    @Test
    public void shouldRequestWeatherIfPermissionIsGrantedAndLocationIsPresent() throws IOException
    {
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(true);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(true);
        Location location = mockLocation(mock(Location.class), 1.0, 1.0);
        when(weatherService.getCurrentWeather(1.0, 1.0))
                .thenReturn(Observable.just(fakeResponse(Responses.JSON.TODAY_WEATHER)));
        when(locationService.lastLocation()).thenReturn(location);
        vm.onViewAttached();
        verify(pm, atLeastOnce()).isPermissionGranted(anyString());
    }

    @Test
    public void shouldNotLoadForecastsIfNoLocationIsPresent() {
        when(locationService.lastLocation()).thenReturn(null);
        vm.onViewAttached();
        vm.loadForecastWeatherDataForToday();
        verifyZeroInteractions(weatherService);
    }

    @Test
    public void shouldLoadForecastsIfLocationIsPresent() throws ParseException, IOException
    {
        double longitude = 1.0;
        double latitude = 1.0;
        final Location location = mockLocation(mock(Location.class), 1.0, 1.0);
        when(locationService.lastLocation()).thenReturn(location);
        String expectedResult = Responses.createExpectedFilteredResult();
        when(weatherParser.parse(any(ThreeHoursForecastWeather.class))).thenReturn(expectedResult);
        when(weatherService.getForecastWeather(longitude, latitude))
                .thenReturn(Observable.just(fakeResponse(Responses.JSON.THREE_HOUR_FORECAST)));
        vm.loadForecastWeatherDataForToday();
        verify(navigation).toForecastActivity(expectedResult);
    }

    @Test
    public void locationChangeShouldTriggerReloadOfWeatherData() {
        vm.onViewAttached();
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(true);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(true);
        double longlat = 6.0;
        locationSubject.onNext(mockLocation(mock(Location.class), longlat, longlat));
        verify(weatherService).getCurrentWeather(longlat, longlat);
    }

    @Test
    public void grantingPermissionsShouldTriggerReloadOfWeatherDataIfGpsIsEnabled() {
        final Location location = mockLocation(mock(Location.class), 1.0, 1.0);
        when(locationService.lastLocation()).thenReturn(location);
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
        permissionSubject.onNext(new PermissionResult(
                TodayWeatherViewModel.REQUEST_CODE_PERM_ACCESS_COARSE_LOCATION,
                new String[]{PERM_COARSE_LOCATION},
                new int[]{PackageManager.PERMISSION_GRANTED}
        ));
        verify(weatherService).getCurrentWeather(1.0, 1.0);
    }

    @Test
    public void denieingPermissionsShouldNotTriggerReloadOfWeatherData() {
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

    @Test
    public void refreshesIfNecessary() {
        when(pm.isPermissionGranted(PERM_COARSE_LOCATION)).thenReturn(true);
        when(pm.isPermissionGranted(PERM_FINE_LOCATION)).thenReturn(true);
        when(weatherService.getCurrentWeather(1.0, 1.0)).thenReturn(Observable.empty());
        final Location location = mockLocation(mock(Location.class), 1.0, 1.0);
        when(locationService.lastLocation()).thenReturn(location);
        vm.onViewAttached();
        reset(weatherService);
        when(weatherService.getCurrentWeather(1.0, 1.0)).thenReturn(Observable.empty());
        pageChanged.onNext(0);
        verify(weatherService).getCurrentWeather(1.0, 1.0);
    }

    @Test
    public void loadsDataWhenGpsGotActivated() {
        doReturn(false).when(locationService).isGpsProviderEnabled();
        vm.onViewAttached();
        reset(locationService);
    }

    private static Location mockLocation(Location location, double lng, double lat) {
        doReturn(lng).when(location).getLongitude();
        doReturn(lat).when(location).getLatitude();
        return location;
    }

}