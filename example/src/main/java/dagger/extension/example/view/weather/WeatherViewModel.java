package dagger.extension.example.view.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.location.Location;

import dagger.extension.example.service.PermissionResult;
import dagger.extension.example.model.Weather;
import dagger.extension.example.service.LocationProvider;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.PermissionManager;
import dagger.extension.example.service.WeatherService;
import dagger.extension.example.service.filter.WeatherResponseFilter;
import dagger.extension.example.view.main.NavigationViewModel;
import io.reactivex.internal.disposables.ListCompositeDisposable;

public abstract class WeatherViewModel extends NavigationViewModel
{

    public static final int REQUEST_CODE_PERM_ACCESS_FINE_LOCATION = 45;
    public static final int REQUEST_CODE_PERM_ACCESS_COARSE_LOCATION = 23;

    protected final PermissionManager permissionManager;
    protected final LocationProvider locationProvider;
    protected final WeatherService weatherService;
    protected final WeatherResponseFilter weatherParser;

    public final ObservableField<String> temperature = new ObservableField<>();
    public final ObservableField<String> humidity = new ObservableField<>();
    public final ObservableField<String> city = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();

    private Location lastLocation = null;

    public final ObservableField<Boolean> progressVisibility = new ObservableField<>(false);
    public final ObservableField<Bitmap> icon = new ObservableField<>(null);

    protected final ListCompositeDisposable disposables = new ListCompositeDisposable();

    public WeatherViewModel(NavigationController navigation, PermissionManager permissionManager, LocationProvider locationProvider, WeatherService weatherService, WeatherResponseFilter weatherParser)
    {
        super(navigation);
        this.permissionManager = permissionManager;
        this.locationProvider = locationProvider;
        this.weatherService = weatherService;
        this.weatherParser = weatherParser;
    }

    protected abstract void loadWeather(double longitude, double latitude);

    public void onViewAttached()
    {
        disposables.add(permissionManager.onPermissionGranted().subscribe(this::onPermissionsResult));
        disposables.add(locationProvider.onNewLocation().subscribe(this::onLocationChanged));
        requestPermissionsIfNeeded();
        if (hasAllPermissions())
        {
            locationProvider.requestLocationUpdates();
            loadWeatherIfAllPermissionsGranted();
        }
    }

    private void onPermissionsResult(PermissionResult e) {
        this.onPermissionsResult(e.getRequestCode(), e.getPermissions(), e.getGrantResults());
    }

    public void onViewDetached()
    {
        disposables.dispose();
        disposables.clear();
        locationProvider.disposeIfNotObserved();
    }

    public void loadWeatherIfAllPermissionsGranted()
    {
        locationProvider.requestLocationUpdates();
        if (!requestPermissionsIfNeeded())
        {
            Location lastKnownLocation = this.locationProvider.lastLocation();
            this.loadWeather(lastKnownLocation);
        }
    }

    private void loadWeather(Location location)
    {
        if (location != null)
        {
            final double longitude = location.getLongitude();
            final double latitude = location.getLatitude();
            loadWeather(longitude, latitude);
        }
    }

    protected void dispatchRequestStarted()
    {
        progressVisibility.set(true);
    }

    protected void dispatchRequestFinished()
    {
        progressVisibility.set(false);
    }

    private boolean requestPermissionsIfNeeded()
    {
        boolean result = false;
        if (!permissionManager.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION))
        {
            permissionManager.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_CODE_PERM_ACCESS_FINE_LOCATION);
            result = true;
        }
        if (!permissionManager.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION))
        {
            permissionManager.requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_CODE_PERM_ACCESS_COARSE_LOCATION);
            result = true;
        }
        return result;
    }

    protected void updateState(Weather weather)
    {
        this.description.set(weather.description());
        this.city.set(weather.city());
        this.temperature.set(weather.temperature());
        this.humidity.set(weather.humidity());
    }

    private boolean hasAllPermissions()
    {
        if (!permissionManager.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION))
            return false;
        if (!permissionManager.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION))
            return false;
        return true;
    }

    public void onPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == REQUEST_CODE_PERM_ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if (permissionManager.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION))
                loadWeather(locationProvider.lastLocation());
        } else if (requestCode == REQUEST_CODE_PERM_ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if (permissionManager.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION))
                loadWeather(locationProvider.lastLocation());
        }
    }

    public void onLocationChanged(Location location)
    {
        if (hasAllPermissions() && isNewLocation(location))
        {
            this.lastLocation = location;
            loadWeather(location);
        }
    }

    private boolean isNewLocation(Location location)
    {
        if (lastLocation == null) {
            return true;
        }

        if (lastLocation.getLongitude() != location.getLongitude()) {
            return true;
        }

        if (lastLocation.getLatitude() != location.getLatitude()) {
            return true;
        }

        return false;
    }

    @Override
    public void showError(String title, String errorMessage) {
        dispatchRequestFinished();
        super.showError(title, errorMessage);
    }
}
