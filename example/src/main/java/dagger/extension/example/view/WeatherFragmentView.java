package dagger.extension.example.view;

import android.graphics.Bitmap;

public interface WeatherFragmentView {
    void requestStarted();
    void requestFinished();
    void showWeather(String city, String description, String temperature, String humidity);
    boolean isPermissionGranted(String permission);
    void requestPermission(String permission, int requestCode);
    void showIcon(Bitmap icon);
}
