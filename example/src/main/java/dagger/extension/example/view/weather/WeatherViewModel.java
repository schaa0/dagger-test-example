package dagger.extension.example.view.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;

import javax.inject.Inject;

import dagger.extension.example.model.EmptyWeather;
import dagger.extension.example.model.Weather;
import dagger.extension.example.service.LocationService;
import dagger.extension.example.service.NavigationController;
import dagger.extension.example.service.PermissionResult;
import dagger.extension.example.service.PermissionService;
import dagger.extension.example.service.WeatherService;
import dagger.extension.example.service.filter.WeatherResponseFilter;
import dagger.extension.example.view.main.NavigationViewModel;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import retrofit2.HttpException;

public abstract class WeatherViewModel extends NavigationViewModel
{

    public static final int REQUEST_CODE_PERM_ACCESS_FINE_LOCATION = 45;
    public static final int REQUEST_CODE_PERM_ACCESS_COARSE_LOCATION = 23;

    protected final PermissionService permissionService;
    protected final LocationService locationService;
    protected final WeatherService weatherService;
    protected final WeatherResponseFilter weatherParser;
    protected final Scheduler androidScheduler;
    protected final Observable<Integer> pageChangeObservable;

    public final ObservableField<Weather> weather = new ObservableField<>();
    public final ObservableField<Boolean> progressVisibility = new ObservableField<>(false);
    public final ObservableField<Bitmap> icon = new ObservableField<>(null);

    private Location lastLocation = null;

    protected final CompositeDisposable disposables = new CompositeDisposable();

    @Inject WeatherViewModelState state;

    public WeatherViewModel(NavigationController navigation, Observable<Integer> pageChangeObservable,
                            PermissionService permissionService, LocationService locationService,
                            WeatherService weatherService, WeatherResponseFilter weatherParser,
                            Scheduler androidScheduler)
    {
        super(navigation);
        this.pageChangeObservable = pageChangeObservable;
        this.permissionService = permissionService;
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.weatherParser = weatherParser;
        this.androidScheduler = androidScheduler;
    }

    protected abstract void loadWeather(double longitude, double latitude);

    public void onViewAttached()
    {
        disposables.add(permissionService.onPermissionGranted().subscribe(this::onPermissionsResult));
        disposables.add(locationService.onNewLocation().subscribe(this::onLocationChanged));
        disposables.add(pageChangeObservable
                                     .filter(this::isOwnPosition)
                                     .subscribe(this::refreshIfNecessary));

        if (!locationService.isGpsProviderEnabled()) {
            navigate().toLocationSettings();
        }

        if (this.isStatePresent()) {
            this.restoreState();
        }else {
            requestPermissionsIfNeeded();
            if (hasAllPermissions())
            {
                locationService.requestLocationUpdates();
                loadWeatherIfAllPermissionsGranted();
            }
        }
    }

    protected abstract boolean isOwnPosition(int position);

    protected void restoreState() {
        this.weather.set(this.state.weather);
        this.icon.set(this.state.icon);
    }

    private void refreshIfNecessary(int position) {
        if (this.weather.get() == null || this.weather.get() instanceof EmptyWeather) {
            this.loadWeatherIfAllPermissionsGranted();
        }
    }

    private boolean isStatePresent() {
        return state.isPresent();
    }

    private void onPermissionsResult(PermissionResult e) {
        this.onPermissionsResult(e.getRequestCode(), e.getPermissions(), e.getGrantResults());
    }

    public void onViewDetached()
    {
        disposables.dispose();
        disposables.clear();
        locationService.disposeIfNotObserved();
    }

    public void loadWeatherIfAllPermissionsGranted()
    {
        locationService.requestLocationUpdates();
        if (!requestPermissionsIfNeeded())
        {
            Location lastKnownLocation = this.locationService.lastLocation();
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
        if (!permissionService.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION))
        {
            permissionService.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_CODE_PERM_ACCESS_FINE_LOCATION);
            result = true;
        }
        if (!permissionService.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION))
        {
            permissionService.requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, REQUEST_CODE_PERM_ACCESS_COARSE_LOCATION);
            result = true;
        }
        return result;
    }

    protected void updateState(Weather weather)
    {
        this.weather.set(weather);
    }

    private boolean hasAllPermissions()
    {
        if (!permissionService.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION))
            return false;
        if (!permissionService.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION))
            return false;
        return true;
    }

    public void onPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == REQUEST_CODE_PERM_ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if (permissionService.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION))
                loadWeather(locationService.lastLocation());
        } else if (requestCode == REQUEST_CODE_PERM_ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if (permissionService.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION))
                loadWeather(locationService.lastLocation());
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

    protected void showError(Throwable t) throws IOException {
        String message = t.getMessage();
        if (t instanceof HttpException) {
            message = ((HttpException) t).response().errorBody().string();
        }
        this.showError("", message);
    }

    public WeatherViewModelState saveState() {
        return new WeatherViewModelState(this.weather.get(), this.icon.get());
    }

    protected void clearView() {
        this.weather.set(new EmptyWeather());
        this.icon.set(null);
    }

    public static class WeatherViewModelState implements Parcelable {

        private final Weather weather;
        private final Bitmap icon;

        public WeatherViewModelState(){
            this(new EmptyWeather(), null);
        }

        public WeatherViewModelState(Weather weather, Bitmap icon) {
            this.weather = weather;
            this.icon = icon;
        }

        protected WeatherViewModelState(Parcel in) {
            this.weather = in.readParcelable(Weather.class.getClassLoader());
            this.icon = in.readParcelable(Bitmap.class.getClassLoader());
        }

        public static final Creator<WeatherViewModelState> CREATOR = new
                Creator<WeatherViewModelState>() {
            @Override
            public WeatherViewModelState createFromParcel(Parcel in) {
                return new WeatherViewModelState(in);
            }

            @Override
            public WeatherViewModelState[] newArray(int size) {
                return new WeatherViewModelState[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.weather, flags);
            dest.writeParcelable(this.icon, flags);
        }

        public boolean isPresent() {
            return !(this.weather instanceof EmptyWeather);
        }
    }

}
