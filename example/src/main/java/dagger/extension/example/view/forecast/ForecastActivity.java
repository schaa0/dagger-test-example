package dagger.extension.example.view.forecast;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.extension.example.R;
import dagger.extension.example.databinding.LayoutThreeHourForecastActivityBinding;

public class ForecastActivity extends DaggerAppCompatActivity {

    public static final String KEY_FORECAST = "forecast";

    @Inject
    ForecastViewModel vm;

    String forecastWeather;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        forecastWeather = getIntent().getStringExtra(KEY_FORECAST);
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        LayoutThreeHourForecastActivityBinding binding =
                DataBindingUtil.setContentView(this, R.layout.layout_three_hour_forecast_activity);
        binding.setActivity(this);
        binding.setVm(vm);
        binding.executePendingBindings();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
