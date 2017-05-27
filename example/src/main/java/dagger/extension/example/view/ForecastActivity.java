package dagger.extension.example.view;

import android.content.DialogInterface;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.extension.example.R;
import dagger.extension.example.di.WeatherApplication;
import dagger.extension.example.vm.ForecastViewModel;

public class ForecastActivity extends AppCompatActivity implements ForecastActivityView, DialogInterface.OnClickListener{

    public static final String KEY_FORECAST = "forecast";

    @Inject
    Provider<ForecastViewModel> vmProvider;

    public String forecastWeather() {
        return getIntent().getStringExtra(KEY_FORECAST);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        app().inject(this, this.forecastWeather());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_three_hour_forecast_activity);
        showThreeHourForecast(this.forecastWeather());
    }

    private WeatherApplication app() {
        return (WeatherApplication)getApplication();
    }

    @Override
    public void showThreeHourForecast(String forecastWeather) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Three Hour Forecast")
                .setMessage(forecastWeather)
                .setPositiveButton("OK", this)
                .show();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        onBackPressed();
    }

}
